package com.htec.filesystem.controller;

import com.htec.filesystem.annotation.AuthUser;
import com.htec.filesystem.annotation.AuthenticationUser;
import com.htec.filesystem.model.request.CreateShelfRequestModel;
import com.htec.filesystem.model.response.TextResponseMessage;
import com.htec.filesystem.service.ShelfService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shelf")
public class ShelfController {

    private final ShelfService shelfService;

    public ShelfController(ShelfService shelfService) {
        this.shelfService = shelfService;
    }

    @PostMapping("/create")
    public ResponseEntity createShelf(@RequestBody CreateShelfRequestModel createShelfRequestModel,
                                      @AuthenticationUser AuthUser authUser) {

        shelfService.createShelf(createShelfRequestModel, authUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(new TextResponseMessage("Created shelf", HttpStatus.OK.value()));
    }

    @PutMapping("/move-to-trash")
    public ResponseEntity<TextResponseMessage> softDeleteShelf(@AuthenticationUser AuthUser user, @RequestBody List<Long> shelfIds) {

        boolean isDeleted = true;
        shelfService.updateIsDeletedShelf(user, shelfIds, isDeleted);
        return ResponseEntity.ok().body(new TextResponseMessage("Shelf moved to trash.", HttpStatus.OK.value()));
    }

    @GetMapping
    public ResponseEntity getAllShelves(@AuthenticationUser AuthUser authUser) {

        return ResponseEntity.ok(shelfService.getAllShelvesById(authUser.getId()));
    }
}
