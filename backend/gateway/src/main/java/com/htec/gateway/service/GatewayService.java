package com.htec.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class GatewayService {

    private final String AUTH_MICROSERVICE_NAME = "account";
    private final String AUTH_ENDPOINT_PATH = "/account/auth/authenticate";

    private final Map<String, String> apiServerUrls;

    private RestTemplate restTemplate;

    public GatewayService(@Value("#{${apiServers}}") Map<String, String> apiServerUrls,
                          RestTemplate restTemplate) {
        this.apiServerUrls = apiServerUrls;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<byte[]> sendRequest(RequestEntity<byte[]> entity) {

        String microserviceName = getMicroserviceName(entity.getUrl().getPath());

        if (!AUTH_MICROSERVICE_NAME.equals(microserviceName)) {

            String accountApiUrl = getApiUrl(AUTH_MICROSERVICE_NAME, AUTH_ENDPOINT_PATH);

            ResponseEntity<byte[]> authRet = send(accountApiUrl, HttpMethod.GET, new HttpEntity<>(entity.getHeaders()));

            if (!authRet.getStatusCode().equals(HttpStatus.OK)) {
                return authRet;
            }
        }

        String apiUrl = getApiUrl(microserviceName, entity.getUrl().getPath());

        return send(apiUrl, Objects.requireNonNull(entity.getMethod()), entity);
    }

    private ResponseEntity<byte[]> send(String apiUrl, HttpMethod httpMethod, HttpEntity<byte[]> request) {

        try {
            return restTemplate.exchange(
                    apiUrl,
                    httpMethod,
                    request,
                    byte[].class
            );
        } catch (HttpClientErrorException ex) {

            return ResponseEntity.status(ex.getStatusCode())
                    .headers(filterResponseHeaders(ex.getResponseHeaders()))
                    .body(ex.getResponseBodyAsByteArray());
        }
    }

    private HttpHeaders filterResponseHeaders(HttpHeaders headers) {

        Optional.ofNullable(headers).ifPresent(header -> headers.remove("Transfer-Encoding"));

        return headers;
    }

    private String getApiUrl(String microserviceName, String path) {

        String apiServer = apiServerUrls.get(microserviceName);

        String[] arr = path.split("/", 3);

        String urlPath = "";

        if (arr.length > 0) {
            urlPath = arr[arr.length - 1];
        }

        return apiServer + "/" + urlPath;
    }

    private String getMicroserviceName(String path) {

        String[] arr = path.split("/");
        String microserviceName = "";

        if (arr.length > 0) {
            microserviceName = arr[1];
        }

        return microserviceName;
    }
}
