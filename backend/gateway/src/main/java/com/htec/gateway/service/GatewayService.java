package com.htec.gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
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

    public final static String FILE_NOT_FOUND_MESSAGE = "File not found";

    private final List<String> allowedRouteUrls;

    private final Map<String, String> apiServerUrls;

    private final RestTemplate restTemplate;

    public GatewayService(@Value("#{${allowedRoutes}}") List<String> allowedRouteUrls,
                          @Value("#{${apiServers}}") Map<String, String> apiServerUrls,
                          RestTemplate restTemplate) {
        this.allowedRouteUrls = allowedRouteUrls;
        this.apiServerUrls = apiServerUrls;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<byte[]> sendRequest(RequestEntity<byte[]> request, HttpServletRequest servletRequest) throws IOException {

        String microserviceName = getMicroserviceName(request.getUrl().getPath());

        if (!AUTH_MICROSERVICE_NAME.equals(microserviceName) && !isAllowedRoute(request.getUrl().getPath())) {

            String accountApiUrl = getApiUrl(AUTH_MICROSERVICE_NAME, AUTH_ENDPOINT_PATH, null);

            ResponseEntity<byte[]> authRet = send(accountApiUrl, HttpMethod.GET, new HttpEntity<>(request.getHeaders()));

            if (!authRet.getStatusCode().equals(HttpStatus.OK)) {
                return authRet;
            }
        }

        String apiUrl = getApiUrl(microserviceName, request.getUrl().getPath(), request.getUrl().getQuery());

        HttpEntity<byte[]> forwardingRequest;

        try {
            forwardingRequest = buildHttpEntity(request, servletRequest);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode().value()).body(e.getStatusText().getBytes());
        }

        return send(apiUrl, Objects.requireNonNull(request.getMethod()), forwardingRequest);
    }

    private boolean isAllowedRoute(String path) {
        for (String allowedRouteUrl : allowedRouteUrls) {
            if (path.contains(allowedRouteUrl)) {
                return true;
            }
        }
        return false;
    }

    private HttpEntity<byte[]> buildHttpEntity(RequestEntity<byte[]> request, HttpServletRequest servletRequest) throws IOException {

        String shelfHeader = request.getHeaders().getFirst(SHELF_HEADER);

        if (servletRequest != null && FILE_REQUEST_SHELF_HEADER.equals(shelfHeader)) {

            MultipartHttpServletRequest multipartRequest;

            if (servletRequest instanceof MultipartHttpServletRequest) {
                multipartRequest = ((MultipartHttpServletRequest) servletRequest);
            } else {
                throw HttpClientErrorException.create(HttpStatus.BAD_REQUEST, FILE_NOT_FOUND_MESSAGE, request.getHeaders(), null, null);
            }

            byte[] entityBody = buildHttpEntityBody(multipartRequest);

            HttpHeaders headers = new HttpHeaders(filterRequestHeaders(request.getHeaders()));

            return new HttpEntity<>(entityBody, headers);
        }

        return request;
    }

    private byte[] buildHttpEntityBody(MultipartHttpServletRequest multipartRequest) throws IOException {
      
        Map<String, Pair<String, byte[]>> map = new HashMap<>();

        Iterator<String> filesIterator = multipartRequest.getFileNames();

        while (filesIterator.hasNext()) {

            String tempFileName = filesIterator.next();

            MultipartFile file = multipartRequest.getFile(tempFileName);

            if (file != null && !file.isEmpty()) {
                map.put(tempFileName, Pair.of(file.getOriginalFilename(), file.getBytes()));
            }
        }

        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsBytes(map);
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

        HttpHeaders newHeaders = HttpHeaders.writableHttpHeaders(headers);
        newHeaders.setContentType(MediaType.APPLICATION_JSON);

        return newHeaders;
    }

    private HttpHeaders filterResponseHeaders(HttpHeaders headers) {

        Optional.ofNullable(headers).ifPresent(header -> headers.remove("Transfer-Encoding"));

        return headers;
    }

    private String getApiUrl(String microserviceName, String path, String queryString) {

        String apiServer = apiServerUrls.get(microserviceName);

        String[] arr = path.split("/", 3);

        String urlPath = "";

        if (arr.length > 0) {
            urlPath = arr[arr.length - 1];
        }

        if (queryString != null && !queryString.isEmpty()) {
            urlPath = urlPath + "?" + queryString;
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
