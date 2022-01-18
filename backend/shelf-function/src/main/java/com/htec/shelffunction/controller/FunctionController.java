package com.htec.shelffunction.controller;

import com.htec.shelffunction.annotation.AuthUser;
import com.htec.shelffunction.annotation.AuthenticationUser;
import com.htec.shelffunction.dto.FunctionDTO;
import com.htec.shelffunction.model.request.CustomFunctionRequestModel;
import com.htec.shelffunction.model.request.PredefinedFunctionRequestModel;
import com.htec.shelffunction.model.response.FunctionResponseModel;
import com.htec.shelffunction.model.response.TextResponseMessage;
import com.htec.shelffunction.service.FunctionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/functions")
public class FunctionController {

    public final String FUNCTION_CREATED = "Function created";
    public final String FUNCTION_DELETED = "Function deleted";

    private final FunctionService functionService;

    public FunctionController(FunctionService functionService) {
        this.functionService = functionService;
    }

    @PostMapping("/predefined")
    public ResponseEntity<TextResponseMessage> createPredefinedFunction(@RequestBody PredefinedFunctionRequestModel functionRequestModel,
                                                                        @AuthenticationUser AuthUser authUser) {

        functionService.createPredefinedFunction(functionRequestModel, authUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage(FUNCTION_CREATED, HttpStatus.OK.value()));
    }

    @PostMapping("/custom")
    public ResponseEntity<TextResponseMessage> createCustomFunction(@RequestBody CustomFunctionRequestModel customFunctionRequestModel,
                                                                    @AuthenticationUser AuthUser authUser) {

        functionService.createCustomFunction(customFunctionRequestModel, authUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage(FUNCTION_CREATED, HttpStatus.OK.value()));
    }

    @GetMapping
    public ResponseEntity<List<FunctionDTO>> getAllFunctions(@AuthenticationUser AuthUser authUser) {

        return ResponseEntity.ok(functionService.getAllFunctionsByUserId());
    }

    @GetMapping("/{shelfId}/{eventId}")
    public ResponseEntity<List<Long>> getAllFunctionIdsByShelfIdAndEventId(@PathVariable Long shelfId, @PathVariable Long eventId) {

        return ResponseEntity.ok(functionService.getAllFunctionIdsByShelfIdAndEventId(shelfId, eventId));
    }

    @DeleteMapping("/{functionId}")
    public ResponseEntity<TextResponseMessage> deleteFunction(@AuthenticationUser AuthUser user, @PathVariable Long functionId) {

        functionService.deleteFunction(functionId);

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage(FUNCTION_DELETED, HttpStatus.OK.value()));
    }

    @GetMapping("/{functionId}")
    public ResponseEntity<FunctionResponseModel> getFunction(@AuthenticationUser AuthUser authUser, @PathVariable Long functionId) {

        return ResponseEntity.ok(functionService.getFunction(functionId, authUser.getId()));
    }
}
