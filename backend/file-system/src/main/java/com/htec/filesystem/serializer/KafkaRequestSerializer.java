package com.htec.filesystem.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.htec.filesystem.exception.ExceptionSupplier;
import com.htec.filesystem.model.request.KafkaRequestModel;
import org.apache.kafka.common.serialization.Serializer;

public class KafkaRequestSerializer implements Serializer<KafkaRequestModel> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, KafkaRequestModel data) {
        try {
            if (data == null) {
                return null;
            }

            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw ExceptionSupplier.serializationException.get();
        }
    }
}
