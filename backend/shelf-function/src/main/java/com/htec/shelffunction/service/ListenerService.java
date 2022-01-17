package com.htec.shelffunction.service;

import com.htec.shelffunction.entity.FunctionEntity;
import com.htec.shelffunction.model.request.KafkaRequestModel;
import com.htec.shelffunction.repository.FunctionRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ListenerService {

    private final FunctionRepository functionRepository;
    private final Runtime runTime = Runtime.getRuntime();

    private final String homePath = System.getProperty("user.home");
    private final String pathSeparator = FileSystems.getDefault().getSeparator();
    private final String userPath = pathSeparator + "shelf-files" + pathSeparator + "user-data" + pathSeparator;

    private final String JAVA = "java";
    private final String CS = "csharp";

    private final String JAVA_EXECUTE_CMD = "java -cp ";

    public ListenerService(FunctionRepository functionRepository) {
        this.functionRepository = functionRepository;
    }

    @KafkaListener(topics = "test", groupId = "event")
    public void listenGroupEvent(KafkaRequestModel message) {

        List<FunctionEntity> functionEntities = functionRepository.findAllByIdIn(message.getFunctionIds());

        for (FunctionEntity functionEntity : functionEntities) {

            if (JAVA.equals(functionEntity.getLanguage())) {

                executeJavaFunction(message.getUserId(), functionEntity.getId());

            } else if (CS.equals(functionEntity.getLanguage())) {

                executeCsFunction(message.getUserId(), functionEntity.getId());

            }
        }
    }

    private void executeCsFunction(Long userId, Long id) {
        //todo: Implement function for executing C# code
    }

    private void executeJavaFunction(Long userId, Long functionId) {
        try {
            String classPath = homePath + userPath + userId + pathSeparator + "functions";

            Process process = runTime.exec(JAVA_EXECUTE_CMD +
                    classPath + " " +
                    "Function" + functionId);

            process.waitFor(5, TimeUnit.SECONDS);

            process.destroy();

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
