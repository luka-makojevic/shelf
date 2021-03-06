package com.htec.shelffunction.service;

import com.htec.shelffunction.entity.FunctionEntity;
import com.htec.shelffunction.exception.ExceptionSupplier;
import com.htec.shelffunction.repository.FunctionRepository;
import com.htec.shelffunction.util.FunctionEvents;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.htec.shelffunction.util.LanguageConstants.*;
import static com.htec.shelffunction.util.PathConstants.*;

@Service
public class ExecuteService {

    private final Runtime runTime = Runtime.getRuntime();

    private final FunctionRepository functionRepository;

    public ExecuteService(FunctionRepository functionRepository) {
        this.functionRepository = functionRepository;
    }

    public Object execute(String lang, Long functionId) {

        return executeFunction(functionId, lang, true, null, null, null);
    }

    Object executeFunction(Long functionId, String lang, boolean sync, Long userId, Long fileId, String fileName) {
        try {
            FunctionEntity functionEntity = functionRepository.findById(functionId)
                    .orElseThrow(ExceptionSupplier.functionNotFound);

            if (!FunctionEvents.SYNCHRONIZED.getValue().equals(functionEntity.getEvent().getId()) && sync) {
                throw ExceptionSupplier.functionIsNotSynchronized.get();
            }

            String cmd = (CS.equals(lang) ? CS_EXECUTE_CMD : JAVA_EXECUTE_CMD);

            if (CS.equals(lang)) {

                cmd += HOME_PATH + USER_PATH + functionEntity.getPath() + BINARY_CSHARP_EXTENSION;

            } else if (JAVA.equals(lang)) {

                String folderPath = HOME_PATH + USER_PATH +
                        functionEntity.getPath().replace(PATH_SEPARATOR + "Function" + functionId, "");

                cmd += folderPath + " " + "Function" + functionId;

                if (fileId != null) {
                    cmd += " " + fileId;
                }

                if (userId != null) {
                    cmd += " " + userId;
                }

                if (functionEntity.getEvent().getId() != null) {
                    cmd += " " + functionEntity.getEvent().getId();
                }

                if(fileName != null) {
                    cmd += "  '" + fileName + "'";
                }
            }

            Process process = runTime.exec(cmd);

            process.waitFor(PROCESS_EXECUTE_TIME_OUT, TimeUnit.SECONDS);

            InputStream inputStream = (process.exitValue() == 0 ? process.getInputStream() : process.getErrorStream());

            Object result = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));

            process.destroy();

            return result;

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
