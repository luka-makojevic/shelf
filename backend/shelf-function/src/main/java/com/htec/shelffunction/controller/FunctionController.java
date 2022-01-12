package com.htec.shelffunction.controller;

import com.htec.shelffunction.annotation.AuthUser;
import com.htec.shelffunction.annotation.AuthenticationUser;
import com.htec.shelffunction.model.request.PredefinedFunctionRequestModel;
import com.htec.shelffunction.model.response.TextResponseMessage;
import com.htec.shelffunction.service.FunctionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/function")
public class FunctionController {

    public final String FUNCTION_CREATED = "Function created";

    private final FunctionService functionService;

    public FunctionController(FunctionService functionService) {
        this.functionService = functionService;
    }

    @PostMapping
    public ResponseEntity<TextResponseMessage> createShelf(@RequestBody PredefinedFunctionRequestModel functionRequestModel,
                                                           @AuthenticationUser AuthUser authUser) {

        functionService.createPredefinedFunction(functionRequestModel, authUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage(FUNCTION_CREATED, HttpStatus.OK.value()));
    }
}
