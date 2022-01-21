package com.htec.filesystem.service;

import com.htec.filesystem.exception.ExceptionSupplier;
import com.htec.filesystem.model.request.KafkaRequestModel;
import com.htec.filesystem.repository.FileRepository;
import com.htec.filesystem.util.FunctionEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EventService {

    private final String TOPIC_NAME = "event";

    private final FunctionService functionService;
    private final FileRepository fileRepository;
    private final FileService fileService;

    @Autowired
    private KafkaTemplate<String, KafkaRequestModel> kafkaTemplate;

    public EventService(FunctionService functionService,
                        FileRepository fileRepository,
                        FileService fileService) {
        this.functionService = functionService;
        this.fileRepository = fileRepository;
        this.fileService = fileService;
    }

    public void reportEvent(FunctionEvents event, List<Long> fileIds, Long userId, Long shelfId, Map<Long, String> fileNames) {

        if (shelfId == null && !fileIds.isEmpty()) {
            shelfId = fileRepository.findById(fileIds.get(0))
                    .orElseThrow(ExceptionSupplier.fileNotFound).getShelfId();
        }

        if (fileNames == null) {
            fileNames = fileService.getFileNamesFromIds(fileIds);
        }

        List<Long> functionToBeExecutedIds = functionService.getUserFunctionsByShelfId(shelfId, event.getValue());

        for (Long fileId : fileIds) {
            KafkaRequestModel kafkaRequestModel = new KafkaRequestModel();

            kafkaRequestModel.setEvent(event);
            kafkaRequestModel.setFileId(fileId);
            kafkaRequestModel.setUserId(userId);
            kafkaRequestModel.setFunctionIds(functionToBeExecutedIds);
            kafkaRequestModel.setFileName(fileNames.get(fileId));

            kafkaTemplate.send(TOPIC_NAME, kafkaRequestModel);
        }
    }
}
