package com.htec.gateway.controller;

import com.htec.gateway.service.GatewayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shelfserver")
public class ShelfServerController {

    private final GatewayService gatewayService;
    private final String apiUrl;

    public ShelfServerController(GatewayService gatewayService,
                                 @Value("${shelfserverApiUrl}") String apiUrl) {
        this.gatewayService = gatewayService;
        this.apiUrl = apiUrl;
    }

    @RequestMapping(value = "/**")
    public ResponseEntity handle(RequestEntity<byte[]> entity) {

        return gatewayService.sendRequest(entity, apiUrl);

    }

}
