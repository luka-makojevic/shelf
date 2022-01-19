package com.htec.shelffunction.service;

import com.htec.shelffunction.dto.FunctionDTO;
import com.htec.shelffunction.dto.ShelfDTO;
import com.htec.shelffunction.entity.FunctionEntity;
import com.htec.shelffunction.exception.ExceptionSupplier;
import com.htec.shelffunction.filter.JwtStorageFilter;
import com.htec.shelffunction.mapper.FunctionMapper;
import com.htec.shelffunction.model.request.CustomFunctionRequestModel;
import com.htec.shelffunction.model.request.PredefinedFunctionRequestModel;
import com.htec.shelffunction.model.request.RenameFunctionRequestModel;
import com.htec.shelffunction.model.request.UpdateCodeFunctionRequestModel;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


import static com.htec.shelffunction.util.LanguageConstants.*;
import static com.htec.shelffunction.util.PathConstants.*;

@Service
public class FunctionService {

    private final String CHECK_SHELF_ACCESS_URL = "http://localhost:8082/shelf/check/";


    private final String PREDEFINED_FUNCTION_FOLDER = "predefined_functions";

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

        if (checkFunctionNameExists(functionRequestModel.getName(), functionRequestModel.getShelfId())) {
            throw ExceptionSupplier.functionAlreadyExists.get();
        }

        FunctionEntity functionEntity = FunctionMapper.INSTANCE
                .predefinedFunctionRequestModelToFunctionEntity(functionRequestModel);

        functionEntity.setLanguage("java");

        functionRepository.saveAndFlush(functionEntity);

        functionEntity.setPath(userId + PATH_SEPARATOR + "functions" + PATH_SEPARATOR + "Function" + functionEntity.getId());

        functionRepository.save(functionEntity);

        compilePredefinedFunction(functionEntity.getId(), functionRequestModel, userId);
    }

    private boolean checkFunctionNameExists(String functionName, Long shelfId) {
         return getAllFunctionsByUserId().stream().filter(functionDTO -> Objects.equals(functionDTO.getShelfId(), shelfId))
                .anyMatch(functionDTO -> functionDTO.getName().equals(functionName));
    }

    private void compilePredefinedFunction(Long newFunctionId, PredefinedFunctionRequestModel functionRequestModel, Long userId) {

        try {
            String sourceFilePath = HOME_PATH +
                    USER_PATH +
                    PREDEFINED_FUNCTION_FOLDER +
                    PATH_SEPARATOR +
                    functionRequestModel.getFunction() +
                    JAVA_EXTENSION;

            String sourceFileContent = "";

            sourceFileContent = Files.readString(Path.of(sourceFilePath));

            sourceFileContent = sourceFileContent.replace("${functionParameter}", functionRequestModel.getFunctionParam().toString());
            sourceFileContent = sourceFileContent.replace("${className}", "Function" + newFunctionId);

            String tempFolderPath = HOME_PATH +
                    USER_PATH +
                    userId +
                    PATH_SEPARATOR +
                    "functions";

            String tempSourceFilePath = tempFolderPath +
                    PATH_SEPARATOR +
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

        String sourcePath = HOME_PATH + USER_PATH + functionEntity.getPath();
        String binaryPath = HOME_PATH + USER_PATH + functionEntity.getPath();

        if (Objects.equals(functionEntity.getLanguage(), "java")) {

            sourcePath += ".java";
            binaryPath += ".class";


        } else {
            sourcePath += ".cs";
            binaryPath += ".exe";
        }

        functionRepository.delete(functionEntity);

        if (!(new File(sourcePath)).delete()) {
            throw ExceptionSupplier.couldNotDeleteFile.get();
        }

        if (!(new File(binaryPath)).delete()) {
            throw ExceptionSupplier.couldNotDeleteFile.get();
        }
    }

    public void createCustomFunction(CustomFunctionRequestModel customFunctionRequestModel, Long userId) {

        checkAccessRights(customFunctionRequestModel.getShelfId());

        if (checkFunctionNameExists(customFunctionRequestModel.getName(), customFunctionRequestModel.getShelfId())) {
            throw ExceptionSupplier.functionAlreadyExists.get();
        }

        FunctionEntity functionEntity = FunctionMapper.INSTANCE
                .customFunctionRequestModelToFunctionEntity(customFunctionRequestModel);

        functionEntity.setLanguage(customFunctionRequestModel.getLanguage());
        functionEntity.setCustom(true);

        functionRepository.saveAndFlush(functionEntity);

        functionEntity.setPath(userId + PATH_SEPARATOR + "functions" + PATH_SEPARATOR + "Function" + functionEntity.getId());

        functionRepository.save(functionEntity);

        compileCustomFunction(functionEntity.getId(), customFunctionRequestModel, userId);
    }

    public void compileCustomFunction(Long functionEntityId, CustomFunctionRequestModel customFunctionRequestModel, Long userId) {

        try {
            String extension = "";

            if (Objects.equals(customFunctionRequestModel.getLanguage(), "java")) {
                extension += JAVA_EXTENSION;
            } else {
                extension += CSHARP_EXTENSION;
            }

            String sourceFilePath = HOME_PATH +
                    USER_PATH +
                    PREDEFINED_FUNCTION_FOLDER +
                    PATH_SEPARATOR +
                    "hello" +
                    extension;

            String sourceFileContent = Files.readString(Path.of(sourceFilePath))
                    .replace("${className}", "Function" + functionEntityId);

            createCompileProcess(functionEntityId, customFunctionRequestModel.getLanguage(), userId, extension, sourceFileContent);

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void createCompileProcess(Long functionEntityId, String language, Long userId, String extension, String sourceFileContent) throws IOException, InterruptedException {
        String compileCommand = "";

        String tempFolderPath = HOME_PATH +
                USER_PATH +
                userId +
                PATH_SEPARATOR +
                "functions";

        String tempSourceFilePath = tempFolderPath +
                PATH_SEPARATOR +
                "Function" + functionEntityId +
                extension;

        Files.writeString(Path.of(tempSourceFilePath), sourceFileContent);
        Runtime runTime = Runtime.getRuntime();

        if (Objects.equals(language, "java")) {
            compileCommand += JAVA_COMPILE_CMD + tempFolderPath + ":" + JARS_PATH + " " + tempSourceFilePath;
        } else {
            compileCommand += CSHARP_COMPILE_CMD + tempSourceFilePath;
        }

        Process compileProcess = runTime.exec(compileCommand);

        compileProcess.waitFor(5, TimeUnit.SECONDS);

        int exitValue = compileProcess.exitValue();

        compileProcess.destroy();

        if (exitValue != 0) {
            throw ExceptionSupplier.errorInFunctionCode.get();
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

        String sourceFilePath = HOME_PATH +
                USER_PATH +
                userId +
                PATH_SEPARATOR +
                "functions" +
                PATH_SEPARATOR +
                "Function" + functionEntity.getId() +
                extension;

        try {
            functionResponseModel.setCode(Files.readString(Path.of(sourceFilePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return functionResponseModel;
    }

    public List<Long> getAllFunctionIdsByShelfIdAndEventId(Long shelfId, Long eventId) {
        return functionRepository.findAllByShelfIdAndEventId(shelfId, eventId)
                .stream().map(FunctionEntity::getId).collect(Collectors.toList());
    }

    public void updateFunctionName(RenameFunctionRequestModel renameFunctionRequestModel) {

        FunctionEntity functionEntity = functionRepository.findById(renameFunctionRequestModel.getFunctionId())
                .orElseThrow(ExceptionSupplier.functionNotFound);

        checkAccessRights(functionEntity.getShelfId());

        if (checkFunctionNameExists(renameFunctionRequestModel.getNewName(), functionEntity.getShelfId())) {
            throw ExceptionSupplier.functionAlreadyExists.get();
        }

        functionEntity.setName(renameFunctionRequestModel.getNewName());

        functionRepository.save(functionEntity);
    }

    public void updateFunctionCode(UpdateCodeFunctionRequestModel updateCodeFunctionRequestModel, Long userId) {

        FunctionEntity functionEntity = functionRepository.findById(updateCodeFunctionRequestModel.getFunctionId())
                .orElseThrow(ExceptionSupplier.functionNotFound);

        checkAccessRights(functionEntity.getShelfId());

        String extension = "";
        if (Objects.equals(functionEntity.getLanguage(), "java")) {
            extension += JAVA_EXTENSION;
        } else {
            extension += CSHARP_EXTENSION;
        }

        String sourceFilePath = HOME_PATH +
                USER_PATH +
                functionEntity.getPath() +
                extension;

        try {
            Files.writeString(Path.of(sourceFilePath), updateCodeFunctionRequestModel.getCode(), StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            createCompileProcess(functionEntity.getId(), functionEntity.getLanguage(), userId, extension, updateCodeFunctionRequestModel.getCode());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}