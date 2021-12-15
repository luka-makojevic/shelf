package com.htec.filesystem.controller;

import com.htec.filesystem.model.request.CreateShelfRequestModel;
import com.htec.filesystem.model.response.TextResponseMessage;
import com.htec.filesystem.service.FolderService;
import com.htec.filesystem.service.ShelfService;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/shelf")
public class ShelfController {

    private final ShelfService shelfService;

    public ShelfController(ShelfService shelfService) {
        this.shelfService = shelfService;
    }

    @PostMapping("/create")
    public ResponseEntity createShelf(@RequestBody CreateShelfRequestModel createShelfRequestModel) {


        shelfService.createShelf(createShelfRequestModel);

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage("Created shelf", HttpStatus.OK.value()));
    }

}
