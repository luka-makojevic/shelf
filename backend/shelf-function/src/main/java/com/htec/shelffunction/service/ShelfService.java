package com.htec.shelffunction.service;

import com.htec.shelffunction.dto.ShelfDTO;
import com.htec.shelffunction.filter.JwtStorageFilter;
import com.htec.shelffunction.security.SecurityConstants;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShelfService {

    private final String GET_SHELVES_BY_USER_ID_URL = "http://localhost:8082/shelf/";
    private final String GET_SHELVES_BY_ID = "http://localhost:8082/shelf/record/";
    private final RestTemplate restTemplate;

    public ShelfService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Long> getUsersShelfIds() {

        URI apiUrl = URI.create(GET_SHELVES_BY_USER_ID_URL);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(SecurityConstants.AUTHORIZATION_HEADER_STRING,
                JwtStorageFilter.jwtThreadLocal.get());

        HttpEntity<Object> request = new HttpEntity<>(headers);

        List<ShelfDTO> responseBody = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<ShelfDTO>>() {
                }
        ).getBody();

        if (responseBody == null) {
            return new ArrayList<>();
        }

        return responseBody.stream().map(ShelfDTO::getId).collect(Collectors.toList());
    }

    public ShelfDTO getShelf(Long shelfId) {
        URI apiUrl = URI.create(GET_SHELVES_BY_ID + shelfId);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(SecurityConstants.AUTHORIZATION_HEADER_STRING,
                JwtStorageFilter.jwtThreadLocal.get());

        HttpEntity<Object> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                request,
                ShelfDTO.class
        ).getBody();
    }
}
