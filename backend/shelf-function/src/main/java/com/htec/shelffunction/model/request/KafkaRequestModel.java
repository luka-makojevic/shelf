package com.htec.shelffunction.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KafkaRequestModel {
    private List<Long> functionIds;
    private Long userId;
    private Long fileId;
    private String eventType;
}
