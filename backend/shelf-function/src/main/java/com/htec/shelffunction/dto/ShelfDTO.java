package com.htec.shelffunction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShelfDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}
