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
    private final String CS_EXECUTE_CMD = "mcs ";

    public ListenerService(FunctionRepository functionRepository) {
        this.functionRepository = functionRepository;
    }

    @KafkaListener(topics = "event", groupId = "event")
    public void listenGroupEvent(KafkaRequestModel message) {

        List<FunctionEntity> functionEntities = functionRepository.findAllByIdIn(message.getFunctionIds());

        for (FunctionEntity functionEntity : functionEntities) {

            if (JAVA.equals(functionEntity.getLanguage())) {

                executeFunction(message.getUserId(), functionEntity.getId(), JAVA);

            } else if (CS.equals(functionEntity.getLanguage())) {

                executeFunction(message.getUserId(), functionEntity.getId(), CS);

            }
        }
    }

    private void executeFunction(Long userId, Long functionId, String lang) {
        try {
            String executeCmd = (CS.equals(lang) ? CS_EXECUTE_CMD : JAVA_EXECUTE_CMD);
            String folderPath = homePath + userPath + userId + pathSeparator + "functions";

            String cmd = executeCmd + folderPath +
                    (CS.equals(lang) ? pathSeparator : " ") +
                    "Function" + functionId + (CS.equals(lang) ? ".exe" : "");

            Process process = runTime.exec(cmd);

            process.waitFor(5, TimeUnit.SECONDS);

            process.destroy();

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
