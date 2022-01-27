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
public class FunctionService {

    private static final String FUNCTION_URL = "http://localhost:8083/functions/";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Long> getUserFunctionsByShelfId(Long shelfId, Long eventId) {

        URI apiUrl = URI.create(FUNCTION_URL + shelfId + "/" + eventId);

        HttpEntity<Object> request = getObjectHttpEntity();

        return restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<Long>>() {
                }
        ).getBody();
    }

    public List<Long> getUsersFunctionByShelfIdToDelete(Long userId) {

        URI apiUrl = URI.create(FUNCTION_URL + "user/" + userId);

        HttpEntity<Object> request = getObjectHttpEntity();

        return restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<Long>>() {
                }
        ).getBody();
    }

    private HttpEntity<Object> getObjectHttpEntity() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(SecurityConstants.AUTHORIZATION_HEADER_STRING,
                JwtStorageFilter.jwtThreadLocal.get());

        return new HttpEntity<>(headers);
    }

    public void deleteUsersFunctions(Long userFunctionId, Long userId) {

        URI apiUrl = URI.create(FUNCTION_URL + userFunctionId + "/" + userId);

        HttpEntity<Object> request = getObjectHttpEntity();

        restTemplate.exchange(
                apiUrl,
                HttpMethod.DELETE,
                request,
                Void.class
        );
    }
}
