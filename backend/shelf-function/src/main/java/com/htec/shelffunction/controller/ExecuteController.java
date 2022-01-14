package com.htec.shelffunction.controller;

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

    @GetMapping("/{lang}/{functionName}")
    public ResponseEntity<Object> execute(@PathVariable("lang") String lang, @PathVariable("functionName") String functionName) {

        String result = "";

        if ("java".equals(lang)) {

            result = runJavaCode(functionName);
        } else if ("cs".equals(lang)) {

            result = runCsCode(functionName);
        }

        return ResponseEntity.ok().body(result);
    }

    private String runCsCode(String functionName) {
        try {
            Runtime runTime = Runtime.getRuntime();

            Process compileProcess = runTime.exec("mcs /home/luka/Desktop/testFolder/" +
                    functionName +
                    ".cs");

            compileProcess.waitFor(5, TimeUnit.SECONDS);


            compileProcess.destroy();

            Process process = runTime.exec("mono /home/luka/Desktop/testFolder/" + functionName + ".exe");

            process.waitFor(5, TimeUnit.SECONDS);

            InputStream inputStream = process.getInputStream();

            String result = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));

            process.destroy();

            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String runJavaCode(String functionName) {
        try {
            Runtime runTime = Runtime.getRuntime();

            Process compileProcess = runTime.exec("javac -cp /home/luka/Desktop/testFolder:/home/luka/Desktop/testFolder /home/luka/Desktop/testFolder/" +
                    functionName +
                    ".java");

            compileProcess.waitFor(5, TimeUnit.SECONDS);


            compileProcess.destroy();

            Process process = runTime.exec("java -cp /home/luka/Desktop/testFolder:. " + functionName);

            process.waitFor(5, TimeUnit.SECONDS);

            InputStream inputStream = process.getInputStream();

            String result = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));

            process.destroy();

            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return "";
    }
}
