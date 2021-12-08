package com.htec.gateway.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GatewayServiceTest {

    private final String TEST_AUTH_URL = "/test/auth/authenticate";

    @Mock
    private Map<String, String> apiServerUrls;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GatewayService gatewayService;

    @BeforeEach
    void setUp(){
        when(apiServerUrls.get(any())).thenReturn("/test");
    }

    @Test
    void sendRequest_AccountMicroservice() throws URISyntaxException {

        RequestEntity<byte[]> request = new RequestEntity<>(HttpMethod.POST, new URI("/account/testService"));

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(byte[].class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<byte[]> responseEntity = gatewayService.sendRequest(request);

        verify(restTemplate, times(0)).exchange(eq(TEST_AUTH_URL), eq(HttpMethod.GET), any(), eq(byte[].class));
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(byte[].class));

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    void sendRequest_NonAccountMicroservice() throws URISyntaxException {

        RequestEntity<byte[]> request = new RequestEntity<>(HttpMethod.POST, new URI("/nonAccount/testService"));

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(byte[].class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(byte[].class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<byte[]> responseEntity = gatewayService.sendRequest(request);

        verify(restTemplate, times(1)).exchange(eq(TEST_AUTH_URL), eq(HttpMethod.GET), any(), eq(byte[].class));
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(byte[].class));

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    void sendRequest_NonAccountMicroservice_NonAuthorised() throws URISyntaxException {

        RequestEntity<byte[]> request = new RequestEntity<>(HttpMethod.POST, new URI("/nonAccount/testService"));

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(byte[].class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.FORBIDDEN));

        ResponseEntity<byte[]> responseEntity = gatewayService.sendRequest(request);

        verify(restTemplate, times(1)).exchange(eq(TEST_AUTH_URL), eq(HttpMethod.GET), any(), eq(byte[].class));
        verify(restTemplate, times(0)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(byte[].class));

        Assertions.assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());

    }

    @Test
    void sendRequest_NonAccountMicroservice_BadRequest() throws URISyntaxException {

        RequestEntity<byte[]> request = new RequestEntity<>(HttpMethod.POST, new URI("/nonAccount/testService"));

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(byte[].class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(byte[].class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        ResponseEntity<byte[]> responseEntity = gatewayService.sendRequest(request);

        verify(restTemplate, times(1)).exchange(eq(TEST_AUTH_URL), eq(HttpMethod.GET), any(), eq(byte[].class));
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(), eq(byte[].class));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }
}
