package com.htec.filesystem.service;

import com.htec.filesystem.exception.ExceptionSupplier;
import com.htec.filesystem.model.request.UpdateUserPhotoByIdRequestModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class UserAPICallService {

    RestTemplate restTemplate = new RestTemplate();
    private final String UPDATE_PHOTO_URL = "http://localhost:8080/users/update/photo";

    public void updateUserPhotoById(Long id) {

        try {
            UpdateUserPhotoByIdRequestModel updateUserPhotoByIdRequestModel = new UpdateUserPhotoByIdRequestModel();
            updateUserPhotoByIdRequestModel.setId(id);
            updateUserPhotoByIdRequestModel.setPictureName(id.toString());
            HttpEntity<UpdateUserPhotoByIdRequestModel> httpEntityRequestBody = new HttpEntity<>(updateUserPhotoByIdRequestModel);
            restTemplate.exchange(UPDATE_PHOTO_URL, HttpMethod.PUT, httpEntityRequestBody, Void.class);

        } catch (RestClientException ex) {
            throw ExceptionSupplier.couldNotUpdateUser.get();
        }
    }
}
