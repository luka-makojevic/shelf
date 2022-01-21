package com.htec.filesystem.service;

import com.htec.filesystem.filters.JwtStorageFilter;
import com.htec.filesystem.security.SecurityConstants;
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
public class FunctionService {

    private static final String GET_USER_FUNCTIONS_BY_SHELF_ID_AND_EVENT_ID_URL = "http://localhost:8083/functions/";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Long> getUserFunctionsByShelfId(Long shelfId, Long eventId) {

        URI apiUrl = URI.create(GET_USER_FUNCTIONS_BY_SHELF_ID_AND_EVENT_ID_URL + shelfId + "/" + eventId);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(SecurityConstants.AUTHORIZATION_HEADER_STRING,
                JwtStorageFilter.jwtThreadLocal.get());

        HttpEntity<Object> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<Long>>() {
                }
        ).getBody();
    }
}
