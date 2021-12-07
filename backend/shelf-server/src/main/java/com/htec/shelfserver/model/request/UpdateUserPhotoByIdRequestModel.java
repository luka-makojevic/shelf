package com.htec.shelfserver.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserPhotoByIdRequestModel {
    private String pictureName;
    private Long id;
}
