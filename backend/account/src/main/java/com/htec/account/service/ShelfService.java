package com.htec.account.service;

import com.htec.account.dto.ShelfDTO;
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
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShelfService {

    private final String GET_SHELVES_BY_USER_ID_URL = "http://localhost:8082/shelf/";
    private final String GET_SHELVES_BY_ID = "http://localhost:8082/shelf/record/";
    private final String DELETE_USER_SHELVES = "http://localhost:8082/shelf/";
    private final RestTemplate restTemplate;

    public ShelfService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Long> getUsersShelfIds(Long userId, Boolean delete) {

        URI apiUrl;

        if (Boolean.TRUE.equals(delete)) {
            apiUrl = URI.create(GET_SHELVES_BY_USER_ID_URL + "user/" + userId);
        } else {

            apiUrl = URI.create(GET_SHELVES_BY_USER_ID_URL);
        }

        HttpEntity<Object> request = getObjectHttpEntity();

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

        HttpEntity<Object> request = getObjectHttpEntity();

        return restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                request,
                ShelfDTO.class
        ).getBody();
    }

    private HttpEntity<Object> getObjectHttpEntity() {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(SecurityConstants.AUTHORIZATION_HEADER_STRING,
                JwtStorageFilter.jwtThreadLocal.get());

        return new HttpEntity<>(headers);
    }

    public void deleteUsersShelves(Long shelfId, Long userId) {

        URI apiUrl = URI.create(DELETE_USER_SHELVES + shelfId + "/" + userId);

        HttpEntity<Object> request = getObjectHttpEntity();

        restTemplate.exchange(
                apiUrl,
                HttpMethod.DELETE,
                request,
                Void.class
        );
    }
}
