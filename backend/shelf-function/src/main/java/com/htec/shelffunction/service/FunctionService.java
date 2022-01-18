package com.htec.shelffunction.service;

import com.htec.shelffunction.dto.FunctionDTO;
import com.htec.shelffunction.dto.ShelfDTO;
import com.htec.shelffunction.entity.FunctionEntity;
import com.htec.shelffunction.exception.ExceptionSupplier;
import com.htec.shelffunction.filter.JwtStorageFilter;
import com.htec.shelffunction.mapper.FunctionMapper;
import com.htec.shelffunction.model.request.CustomFunctionRequestModel;
import com.htec.shelffunction.model.request.PredefinedFunctionRequestModel;
import com.htec.shelffunction.model.response.FunctionResponseModel;
import com.htec.shelffunction.repository.FunctionRepository;
import com.htec.shelffunction.security.SecurityConstants;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class FunctionService {

    private final String CHECK_SHELF_ACCESS_URL = "http://localhost:8082/shelf/check/";
    private final String JAVA_COMPILE_CMD = "javac -cp ";
    private final String CSHARP_COMPILE_CMD = "mcs ";
    private final String homePath = System.getProperty("user.home");
    private final String pathSeparator = FileSystems.getDefault().getSeparator();
    private final String userPath = pathSeparator + "shelf-files" + pathSeparator + "user-data" + pathSeparator;
    private final String JARS_PATH = homePath + userPath + "predefined_functions/jars/*";

    private final String PREDEFINED_FUNCTION_FOLDER = "predefined_functions";
    private final String JAVA_EXTENSION = ".java";
    private final String CSHARP_EXTENSION = ".cs";

    private final FunctionRepository functionRepository;
    private final RestTemplate restTemplate;
    private final ShelfService shelfService;

    public FunctionService(FunctionRepository functionRepository,
                           RestTemplate restTemplate,
                           ShelfService shelfService) {
        this.functionRepository = functionRepository;
        this.restTemplate = restTemplate;
        this.shelfService = shelfService;
    }

    public void createPredefinedFunction(PredefinedFunctionRequestModel functionRequestModel, Long userId) {

        checkAccessRights(functionRequestModel.getShelfId());

        FunctionEntity functionEntity = FunctionMapper.INSTANCE
                .predefinedFunctionRequestModelToFunctionEntity(functionRequestModel);

        functionEntity.setLanguage("java");

        functionRepository.saveAndFlush(functionEntity);

        functionEntity.setPath(userId + pathSeparator + "functions" + pathSeparator + "Function" + functionEntity.getId());

        functionRepository.save(functionEntity);

        compilePredefinedFunction(functionEntity.getId(), functionRequestModel, userId);
    }

    private void compilePredefinedFunction(Long newFunctionId, PredefinedFunctionRequestModel functionRequestModel, Long userId) {
        try {
            String sourceFilePath = homePath +
                    userPath +
                    PREDEFINED_FUNCTION_FOLDER +
                    pathSeparator +
                    functionRequestModel.getFunction() +
                    JAVA_EXTENSION;

            String sourceFileContent = "";

            sourceFileContent = Files.readString(Path.of(sourceFilePath));

            sourceFileContent = sourceFileContent.replace("${functionParameter}", functionRequestModel.getFunctionParam().toString());
            sourceFileContent = sourceFileContent.replace("${className}", "Function" + newFunctionId);

            String tempFolderPath = homePath +
                    userPath +
                    userId +
                    pathSeparator +
                    "functions";

            String tempSourceFilePath = tempFolderPath +
                    pathSeparator +
                    "Function" + newFunctionId +
                    JAVA_EXTENSION;

            Files.writeString(Path.of(tempSourceFilePath), sourceFileContent);
            Runtime runTime = Runtime.getRuntime();

            Process compileProcess = runTime.exec(JAVA_COMPILE_CMD + tempFolderPath + ":" +
                    JARS_PATH + " " + tempSourceFilePath);

            compileProcess.waitFor(5, TimeUnit.SECONDS);

            compileProcess.destroy();

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void checkAccessRights(Long shelfId) {
        try {
            URI apiUrl = URI.create(CHECK_SHELF_ACCESS_URL + shelfId);

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add(SecurityConstants.AUTHORIZATION_HEADER_STRING,
                    JwtStorageFilter.jwtThreadLocal.get());

            HttpEntity<Object> request = new HttpEntity<>(headers);

            restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    request,
                    ResponseEntity.class
            );
        } catch (HttpClientErrorException ex) {
            throw ExceptionSupplier.userNotAllowedToAccessShelf.get();
        }
    }

    public List<FunctionDTO> getAllFunctionsByUserId() {

        List<Long> shelfIds = shelfService.getUsersShelfIds();

        List<FunctionEntity> functionEntities = functionRepository.findAllByShelfIdIn(shelfIds);

        return FunctionMapper.INSTANCE.functionEntitiesToFunctionDtos(functionEntities);
    }

    public void deleteFunction(Long functionId) {

        if (getAllFunctionsByUserId().isEmpty()) {
            throw ExceptionSupplier.userNotAllowedToDeleteFunction.get();
        }

        FunctionEntity functionEntity = functionRepository.findById(functionId)
                .orElseThrow(ExceptionSupplier.functionNotFound);

        String sourcePath = homePath + userPath + functionEntity.getPath();
        String binaryPath = homePath + userPath + functionEntity.getPath();

        if (Objects.equals(functionEntity.getLanguage(), "java")) {

            sourcePath += ".java";
            binaryPath += ".class";


        } else {
            sourcePath += ".cs";
            binaryPath += ".exe";
        }

        if (!(new File(sourcePath)).delete()) {
            throw ExceptionSupplier.couldNotDeleteFile.get();
        }

        if (!(new File(binaryPath)).delete()) {
            throw ExceptionSupplier.couldNotDeleteFile.get();
        }

        functionRepository.delete(functionEntity);
    }

    public void createCustomFunction(CustomFunctionRequestModel customFunctionRequestModel, Long userId) {

        FunctionEntity functionEntity = FunctionMapper.INSTANCE
                .customFunctionRequestModelToFunctionEntity(customFunctionRequestModel);

        functionEntity.setLanguage(customFunctionRequestModel.getLanguage());
        functionEntity.setCustom(true);

        functionRepository.saveAndFlush(functionEntity);

        functionEntity.setPath(userId + pathSeparator + "functions" + pathSeparator + "Function" + functionEntity.getId());

        functionRepository.save(functionEntity);

        compileCustomFunction(functionEntity.getId(), customFunctionRequestModel, userId);
    }

    public void compileCustomFunction(Long functionEntityId, CustomFunctionRequestModel customFunctionRequestModel, Long userId) {

        try {
            String extension = "";
            String compileCommand = "";

            if (Objects.equals(customFunctionRequestModel.getLanguage(), "java")) {
                extension += JAVA_EXTENSION;
            } else {
                extension += CSHARP_EXTENSION;
            }

            String sourceFilePath = homePath +
                    userPath +
                    PREDEFINED_FUNCTION_FOLDER +
                    pathSeparator +
                    "hello" +
                    extension;

            String sourceFileContent = Files.readString(Path.of(sourceFilePath))
                    .replace("${className}", "Function" + functionEntityId);

            String tempFolderPath = homePath +
                    userPath +
                    userId +
                    pathSeparator +
                    "functions";

            String tempSourceFilePath = tempFolderPath +
                    pathSeparator +
                    "Function" + functionEntityId +
                    extension;

            Files.writeString(Path.of(tempSourceFilePath), sourceFileContent);
            Runtime runTime = Runtime.getRuntime();

            if (Objects.equals(customFunctionRequestModel.getLanguage(), "java")) {
                compileCommand += JAVA_COMPILE_CMD + tempFolderPath + ":" + JARS_PATH + " " + tempSourceFilePath;
            } else {
                compileCommand += CSHARP_COMPILE_CMD + tempSourceFilePath;
            }

            Process compileProcess = runTime.exec(compileCommand);

            compileProcess.waitFor(5, TimeUnit.SECONDS);

            compileProcess.destroy();

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public FunctionResponseModel getFunction(Long functionId, Long userId) {

        if (getAllFunctionsByUserId().isEmpty()) {
            throw ExceptionSupplier.userNotAllowedToGetFunction.get();
        }

        FunctionEntity functionEntity = functionRepository.findById(functionId)
                .orElseThrow(ExceptionSupplier.functionNotFound);

        FunctionResponseModel functionResponseModel = FunctionMapper.INSTANCE.functionEntityToFunctionResponseModel(functionEntity);
        functionResponseModel.setEventName(functionEntity.getEvent().getName());

        ShelfDTO shelfDTO = shelfService.getShelf(functionEntity.getShelfId());
        functionResponseModel.setShelfName(shelfDTO.getName());

        String extension = "";

        if (Objects.equals(functionEntity.getLanguage(), "java")) {
            extension += JAVA_EXTENSION;
        } else {
            extension += CSHARP_EXTENSION;
        }

        String sourceFilePath = homePath +
                userPath +
                userId +
                pathSeparator +
                "functions" +
                pathSeparator +
                "Function" + functionEntity.getId() +
                extension;

        try {
            functionResponseModel.setCode(Files.readString(Path.of(sourceFilePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return functionResponseModel;
    }
}