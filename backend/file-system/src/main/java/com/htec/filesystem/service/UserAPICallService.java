package com.htec.filesystem.service;

import com.htec.filesystem.exception.ExceptionSupplier;
import com.htec.filesystem.filters.JwtStorageFilter;
import com.htec.filesystem.model.request.UpdateUserPhotoByIdRequestModel;
import com.htec.filesystem.security.SecurityConstants;
import com.htec.filesystem.util.ErrorMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@Slf4j
public class UserAPICallService {

    RestTemplate restTemplate = new RestTemplate();
    @Value("${userServiceUrl}")
    private String host;
    private final String UPDATE_PHOTO_URL = "/users/update/photo";
    private final String GET_PHOTO_NAME = "/users/picture-name/";

    public void updateUserPhotoById(Long id, String pictureName) {

        try {
            UpdateUserPhotoByIdRequestModel updateUserPhotoByIdRequestModel = new UpdateUserPhotoByIdRequestModel();
            updateUserPhotoByIdRequestModel.setId(id);
            updateUserPhotoByIdRequestModel.setPictureName(pictureName);

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add(SecurityConstants.AUTHORIZATION_HEADER_STRING,
                    JwtStorageFilter.jwtThreadLocal.get());

            HttpEntity<UpdateUserPhotoByIdRequestModel> httpEntityRequestBody = new HttpEntity<>(updateUserPhotoByIdRequestModel, headers);

            restTemplate.exchange(host + UPDATE_PHOTO_URL, HttpMethod.PUT, httpEntityRequestBody, Void.class);

        } catch (RestClientException ex) {

            if (Objects.requireNonNull(ex.getMessage()).contains(ErrorMessages.COULD_NOT_FIND_USER_BY_ID.getErrorMessage()))
                throw ExceptionSupplier.couldNotFindUserById.get();

            else
                throw ExceptionSupplier.couldNotUpdateUser.get();
        }
    }

    public String getUserPhotoPath(Long userId) {

//        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
//        headers.add(SecurityConstants.AUTHORIZATION_HEADER_STRING,
//                JwtStorageFilter.jwtThreadLocal.get());
//
//        HttpEntity<String> httpEntityRequestBody = new HttpEntity<>(headers);

        ResponseEntity<String> path = restTemplate.getForEntity(host + GET_PHOTO_NAME + userId, String.class);
        return path.getBody();
    }

}
