package com.htec.shelffunction.service;

import com.htec.shelffunction.dto.FunctionDto;
import com.htec.shelffunction.entity.FunctionEntity;
import com.htec.shelffunction.exception.ExceptionSupplier;
import com.htec.shelffunction.filter.JwtStorageFilter;
import com.htec.shelffunction.mapper.FunctionMapper;
import com.htec.shelffunction.model.request.PredefinedFunctionRequestModel;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class FunctionService {

    private final String CHECK_SHELF_ACCESS_URL = "http://localhost:8082/shelf/check/";
    private final String JAVA_COMPILE_CMD = "javac -cp ";
    private final String homePath = System.getProperty("user.home");
    private final String pathSeparator = FileSystems.getDefault().getSeparator();
    private final String userPath = pathSeparator + "shelf-files" + pathSeparator + "user-data" + pathSeparator;

    private final String PREDEFINED_FUNCTION_FOLDER = "predefined_functions";
    private final String JAVA_EXTENSION = ".java";

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

        functionRepository.saveAndFlush(functionEntity);

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

            Process compileProcess = runTime.exec(JAVA_COMPILE_CMD + tempFolderPath +
                    ":/home/stefan/shelf-files/user-data/predefined_functions/jars/*" +
                    " " + tempSourceFilePath);

            compileProcess.waitFor(5, TimeUnit.SECONDS);

            InputStream inputStream = compileProcess.getErrorStream();

            String result = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));

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

    public List<FunctionDto> getAllFunctionsByUserId(Long userId) {

        List<Long> shelfIds = shelfService.getUsersShelfIds();

        List<FunctionEntity> functionEntities = functionRepository.findAllByShelfIdIn(shelfIds);

        return FunctionMapper.INSTANCE.functionEntitiesToFunctionDtos(functionEntities);
    }
}