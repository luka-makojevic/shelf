package com.htec.filesystem.service;

import com.htec.filesystem.model.request.KafkaRequestModel;
import com.htec.filesystem.util.FunctionEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final String TOPIC_NAME = "event";

    private final FunctionService functionService;

    @Autowired
    private KafkaTemplate<String, KafkaRequestModel> kafkaTemplate;

    public EventService(FunctionService functionService) {
        this.functionService = functionService;
    }

    public void reportEvent(FunctionEvents event, List<Long> fileIds, Long userId, Long shelfId) {

        List<Long> functionToBeExecutedIds = functionService.getUserFunctionsByShelfId(shelfId, event.getValue());

        for (Long fileId : fileIds) {
            KafkaRequestModel kafkaRequestModel = new KafkaRequestModel();

            kafkaRequestModel.setEvent(event);
            kafkaRequestModel.setFileId(fileId);
            kafkaRequestModel.setUserId(userId);

            kafkaRequestModel.setFunctionIds(functionToBeExecutedIds);

            kafkaTemplate.send(TOPIC_NAME, kafkaRequestModel);
        }
    }
}
