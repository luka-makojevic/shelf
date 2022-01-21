package com.htec.account.service;

import com.htec.account.filter.JwtStorageFilter;
import com.htec.account.security.SecurityConstants;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Service
public class FolderService {

    private static final String INITIALIZE_FOLDERS_URL = "http://localhost:8082/folder";
    private final RestTemplate restTemplate = new RestTemplate();

    void initializeFolder(Long userId) {

        URI apiUrl = URI.create(INITIALIZE_FOLDERS_URL + "/initialize/" + userId);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(SecurityConstants.AUTHORIZATION_HEADER_STRING,
                JwtStorageFilter.jwtThreadLocal.get());

        HttpEntity<Object> request = new HttpEntity<>(headers);

        restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                request,
                Void.class
        );
    }
}
