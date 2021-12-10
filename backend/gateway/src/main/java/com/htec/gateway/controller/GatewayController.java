package com.htec.gateway.controller;

import com.htec.gateway.service.GatewayService;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class GatewayController {

    private final GatewayService gatewayService;

    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @RequestMapping(value = "/**")
    public ResponseEntity<byte[]> handle(RequestEntity<byte[]> request, HttpServletRequest multipartRequest) throws IOException {

        return gatewayService.sendRequest(request, multipartRequest);

    }

}
