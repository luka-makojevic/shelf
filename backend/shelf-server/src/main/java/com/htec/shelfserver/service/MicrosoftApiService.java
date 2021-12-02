package com.htec.shelfserver.service;

import com.htec.shelfserver.exception.ExceptionSupplier;
import com.htec.shelfserver.model.response.UserRegisterMicrosoftResponseModel;
import com.htec.shelfserver.security.SecurityConstants;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class MicrosoftApiService {

    private final String MICROSOFT_GRAPH_URL = "https://graph.microsoft.com/v1.0/me";

    public Optional<UserRegisterMicrosoftResponseModel> getUserInfo(String bearerToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstants.AUTHORIZATION_HEADER_STRING, SecurityConstants.BEARER_TOKEN_PREFIX + bearerToken);

        ResponseEntity<UserRegisterMicrosoftResponseModel> response;

        try {
            response = restTemplate.exchange(MICROSOFT_GRAPH_URL,
                    HttpMethod.GET, new HttpEntity<>(headers),
                    UserRegisterMicrosoftResponseModel.class);

        } catch (RestClientException ex) {
            throw ExceptionSupplier.accessTokenNotActive.get();
        }

        return Optional.ofNullable(response.getBody());
    }

}
