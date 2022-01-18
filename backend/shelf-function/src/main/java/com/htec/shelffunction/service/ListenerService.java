package com.htec.shelffunction.service;

import com.htec.shelffunction.entity.FunctionEntity;
import com.htec.shelffunction.exception.ExceptionSupplier;
import com.htec.shelffunction.model.request.KafkaRequestModel;
import com.htec.shelffunction.repository.FunctionRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.htec.shelffunction.util.LanguangeConstatns.*;
import static com.htec.shelffunction.util.PathConstants.*;

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
