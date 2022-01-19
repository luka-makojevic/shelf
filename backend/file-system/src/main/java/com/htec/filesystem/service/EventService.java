package com.htec.filesystem.service;

import com.htec.filesystem.exception.ExceptionSupplier;
import com.htec.filesystem.model.request.KafkaRequestModel;
import com.htec.filesystem.repository.FileRepository;
import com.htec.filesystem.util.FunctionEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final String TOPIC_NAME = "event";

    private final FunctionService functionService;
    private final FileRepository fileRepository;

    @Autowired
    private KafkaTemplate<String, KafkaRequestModel> kafkaTemplate;

    public EventService(FunctionService functionService,
                        FileRepository fileRepository) {
        this.functionService = functionService;
        this.fileRepository = fileRepository;
    }

    public void reportEvent(FunctionEvents event, List<Long> fileIds, Long userId, Long shelfId) {

        for (Long fileId : fileIds) {
            KafkaRequestModel kafkaRequestModel = new KafkaRequestModel();

            Long eventId = event.getValue();

            kafkaRequestModel.setEventType(String.valueOf(eventId));
            kafkaRequestModel.setFileId(fileId);
            kafkaRequestModel.setUserId(userId);

            List<Long> functionToBeExecutedIds = functionService.getUserFunctionsByShelfId(shelfId, eventId);

            kafkaRequestModel.setFunctionIds(functionToBeExecutedIds);

            kafkaTemplate.send(TOPIC_NAME, kafkaRequestModel);
        }
    }
}
