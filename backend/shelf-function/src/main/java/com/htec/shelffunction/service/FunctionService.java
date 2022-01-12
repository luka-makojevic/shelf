package com.htec.shelffunction.service;

import com.htec.shelffunction.entity.FunctionEntity;
import com.htec.shelffunction.mapper.FunctionMapper;
import com.htec.shelffunction.model.request.PredefinedFunctionRequestModel;
import com.htec.shelffunction.repository.FunctionRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Service
public class FunctionService {

    private final String JAVA_COMPILE_CMD = "javac -cp ";
    private final String homePath = System.getProperty("user.home");
    private final String pathSeparator = FileSystems.getDefault().getSeparator();
    private final String userPath = pathSeparator + "shelf-files" + pathSeparator + "user-data" + pathSeparator;

    private final String PREDEFINED_FUNCTION_FOLDER = "predefined_functions";
    private final String JAVA_EXTENSION = ".java";

    private final FunctionRepository functionRepository;

    public FunctionService(FunctionRepository functionRepository) {
        this.functionRepository = functionRepository;
    }

    public void createPredefinedFunction(PredefinedFunctionRequestModel functionRequestModel, Long userId) {

        //todo: check if shelf belongs to user

        FunctionEntity functionEntity = FunctionMapper.INSTANCE
                .predefinedFunctionRequestModelToFunctionEntity(functionRequestModel);

        functionRepository.saveAndFlush(functionEntity);


        Long newFunctionId = functionEntity.getId();

        compilePredefinedFunction(newFunctionId, functionRequestModel, userId);


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

            sourceFileContent.replace("${functionParameter}", functionRequestModel.getFunctionParam().toString());

            String tempFolderPath = homePath +
                    userPath +
                    userId +
                    pathSeparator +
                    "functions";

            String tempSourceFilePath = tempFolderPath +
                    pathSeparator +
                    newFunctionId +
                    JAVA_EXTENSION;

            Files.writeString(Path.of(tempSourceFilePath), sourceFileContent);

            Runtime runTime = Runtime.getRuntime();

            Process compileProcess = runTime.exec(JAVA_COMPILE_CMD + tempFolderPath + ":" + tempFolderPath +
                    tempSourceFilePath +
                    JAVA_EXTENSION);

            compileProcess.waitFor(5, TimeUnit.SECONDS);

            compileProcess.destroy();

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}

