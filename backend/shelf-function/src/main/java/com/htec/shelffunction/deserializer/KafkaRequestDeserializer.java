package com.htec.shelffunction.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.htec.shelffunction.exception.ExceptionSupplier;
import com.htec.shelffunction.model.request.KafkaRequestModel;
import org.apache.kafka.common.serialization.Deserializer;


public class KafkaRequestDeserializer implements Deserializer<KafkaRequestModel> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public KafkaRequestModel deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                return null;
            }

            return objectMapper.readValue(new String(data, "UTF-8"), KafkaRequestModel.class);
        } catch (Exception e) {
            throw ExceptionSupplier.deserializationException.get();
        }
    }
}

