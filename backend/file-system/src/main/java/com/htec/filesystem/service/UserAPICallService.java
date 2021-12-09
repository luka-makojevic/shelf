package com.htec.filesystem.service;

import com.htec.filesystem.exception.ExceptionSupplier;
import com.htec.filesystem.model.request.UpdateUserPhotoByIdRequestModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class UserAPICallService {

    RestTemplate restTemplate = new RestTemplate();
    @Value("${userServiceUrl}")
    private String host;
    private final String UPDATE_PHOTO_URL = "/users/update/photo";

    public void updateUserPhotoById(Long id, String pictureName) {

        try {
            UpdateUserPhotoByIdRequestModel updateUserPhotoByIdRequestModel = new UpdateUserPhotoByIdRequestModel();
            updateUserPhotoByIdRequestModel.setId(id);
            updateUserPhotoByIdRequestModel.setPictureName(pictureName);
            HttpEntity<UpdateUserPhotoByIdRequestModel> httpEntityRequestBody = new HttpEntity<>(updateUserPhotoByIdRequestModel);
            restTemplate.exchange(host + UPDATE_PHOTO_URL, HttpMethod.PUT, httpEntityRequestBody, Void.class);

        } catch (RestClientException ex) {
            throw ExceptionSupplier.couldNotUpdateUser.get();
        }
    }
}
