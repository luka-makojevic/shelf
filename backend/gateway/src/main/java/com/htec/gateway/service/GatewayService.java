package com.htec.gateway.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

@Service
public class GatewayService {


    public ResponseEntity<byte[]> sendRequest(RequestEntity<byte[]> entity, String apiServer) {

        String apiUrl = getApiUrl(entity.getUrl().getPath(), apiServer);

        RestTemplate restTemplate = new RestTemplate();

        try {
            return restTemplate.exchange(
                    apiUrl,
                    Objects.requireNonNull(entity.getMethod()),
                    entity,
                    byte[].class
            );
        } catch (HttpClientErrorException ex) {
            HttpHeaders responseHeaders = ex.getResponseHeaders();
            Optional.ofNullable(responseHeaders).ifPresent(headers -> headers.remove("Transfer-Encoding"));

            return ResponseEntity.status(ex.getStatusCode()).headers(responseHeaders).body(ex.getResponseBodyAsByteArray());
        }

    }

    private String getApiUrl(String path, String apiServer) {

        String[] arr = path.split("/", 3);
        String urlPath = arr[2];

        return apiServer + "/" + urlPath;
    }
}
