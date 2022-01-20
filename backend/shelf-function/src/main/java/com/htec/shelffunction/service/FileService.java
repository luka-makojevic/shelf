package com.htec.shelffunction.service;

import com.htec.shelffunction.filter.JwtStorageFilter;
import com.htec.shelffunction.security.SecurityConstants;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class FileService {
    private final String GET_LOG_FILE_ID_URL = "http://localhost:8082/file/log/";
    private final RestTemplate restTemplate;

    public FileService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Long getLogFileId(Long shelfId, String logFileName) {

        URI apiUrl = URI.create(GET_LOG_FILE_ID_URL + shelfId);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(SecurityConstants.AUTHORIZATION_HEADER_STRING,
                JwtStorageFilter.jwtThreadLocal.get());

        HttpEntity<String> request = new HttpEntity<>(logFileName, headers);

        return restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                request,
                Long.class
        ).getBody();
    }
}
