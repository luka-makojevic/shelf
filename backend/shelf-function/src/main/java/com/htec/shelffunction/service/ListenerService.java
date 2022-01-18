package com.htec.shelffunction.service;

import com.htec.shelffunction.entity.FunctionEntity;
import com.htec.shelffunction.model.request.KafkaRequestModel;
import com.htec.shelffunction.repository.FunctionRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListenerService {

    private final FunctionRepository functionRepository;
    private final Runtime runTime = Runtime.getRuntime();
    private final ExecuteService executeService;

    public ListenerService(FunctionRepository functionRepository,
                           ExecuteService executeService) {
        this.functionRepository = functionRepository;
        this.executeService = executeService;
    }

    @KafkaListener(topics = "event", groupId = "event")
    public void listenGroupEvent(KafkaRequestModel message) {

        List<FunctionEntity> functionEntities = functionRepository.findAllByIdIn(message.getFunctionIds());

        for (FunctionEntity functionEntity : functionEntities) {
            executeService.executeFunction(functionEntity.getId(), functionEntity.getLanguage());
        }
    }
}
