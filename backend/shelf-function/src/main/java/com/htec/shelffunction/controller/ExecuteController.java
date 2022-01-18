package com.htec.shelffunction.controller;

import com.htec.shelffunction.service.ExecuteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/execute")
public class ExecuteController {

    private final ExecuteService executeService;

    public ExecuteController(ExecuteService executeService) {
        this.executeService = executeService;
    }

    @GetMapping("/{lang}/{functionId}")
    public ResponseEntity<Object> execute(@PathVariable("lang") String lang, @PathVariable("functionId") Long functionId) {

        return ResponseEntity.ok().body(executeService.execute(lang, functionId));
    }
}
