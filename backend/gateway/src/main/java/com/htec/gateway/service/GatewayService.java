package com.htec.gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Service
public class GatewayService {


    private final static String AUTH_MICROSERVICE_NAME = "account";
    private final static String AUTH_ENDPOINT_PATH = "/account/auth/authenticate";

    public final static String SHELF_HEADER = "Shelf-Header";
    public final static String FILE_REQUEST_SHELF_HEADER = "File-request";

    private final Map<String, String> apiServerUrls;

    private final RestTemplate restTemplate;

    public GatewayService(@Value("#{${apiServers}}") Map<String, String> apiServerUrls,
                          RestTemplate restTemplate) {
        this.apiServerUrls = apiServerUrls;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<byte[]> sendRequest(RequestEntity<byte[]> request, HttpServletRequest multipartRequest) throws IOException {

        String microserviceName = getMicroserviceName(request.getUrl().getPath());

        if (!AUTH_MICROSERVICE_NAME.equals(microserviceName)) {

            String accountApiUrl = getApiUrl(AUTH_MICROSERVICE_NAME, AUTH_ENDPOINT_PATH);

            ResponseEntity<byte[]> authRet = send(accountApiUrl, HttpMethod.GET, new HttpEntity<>(request.getHeaders()));

            if (!authRet.getStatusCode().equals(HttpStatus.OK)) {
                return authRet;
            }
        }

        String apiUrl = getApiUrl(microserviceName, request.getUrl().getPath());

        HttpEntity<byte[]> forwardingRequest = buildHttpEntity(request, multipartRequest);

        return send(apiUrl, Objects.requireNonNull(request.getMethod()), forwardingRequest);
    }

    private HttpEntity<byte[]> buildHttpEntity(RequestEntity<byte[]> request, HttpServletRequest multipartRequest) throws IOException {

        List<String> strings = request.getHeaders().get(SHELF_HEADER);

        if (multipartRequest != null && strings != null && strings.get(0).contains(FILE_REQUEST_SHELF_HEADER)) {

            Map<String, byte[]> map = new HashMap<>();

            MultipartHttpServletRequest multipartReq = null;
            if (multipartRequest instanceof MultipartHttpServletRequest) {
                multipartReq = ((MultipartHttpServletRequest) multipartRequest);
            } else {
                return request;
            }

            Iterator<String> filesIterator = multipartReq.getFileNames();

            while (filesIterator.hasNext()) {

                String tempFileName = filesIterator.next();

                MultipartFile file = multipartReq.getFile(tempFileName);

                if (file != null && !file.isEmpty()) {
                    map.put(tempFileName, file.getBytes());
                }
            }

            HttpHeaders headers = new HttpHeaders(filterRequestHeaders(request.getHeaders()));

            ObjectMapper mapper = new ObjectMapper();
            byte[] bytes = mapper.writeValueAsBytes(map);

            return new HttpEntity<>(bytes, headers);
        }

        return request;
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

    private HttpHeaders filterRequestHeaders(HttpHeaders headers) {

        HttpHeaders newHeaders = new HttpHeaders();

        for (String headerName : headers.keySet()) {
            if (HttpHeaders.CONTENT_TYPE.equals(headerName)) {
                continue;
            }
            newHeaders.put(headerName, Collections.singletonList(headers.getFirst(headerName)));
        }

        newHeaders.setContentType(MediaType.APPLICATION_JSON);

        return newHeaders;
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
