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

        HttpStatus retStatus = HttpStatus.OK;

        if (!shelfService.createShelf(createShelfRequestModel, authUser.getId())) {
            retStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(retStatus).body(new TextResponseMessage("Shelf created", retStatus.value()));
    }

    @PutMapping("/move-to-trash")
    public ResponseEntity<TextResponseMessage> softDeleteShelf(@AuthenticationUser AuthUser user, @RequestBody List<Long> shelfIds) {

        shelfService.updateIsDeletedShelf(user, shelfIds, true);
        return ResponseEntity.ok().body(new TextResponseMessage("Shelf moved to trash.", HttpStatus.OK.value()));
    }

    @GetMapping
    public ResponseEntity getAllShelves(@AuthenticationUser AuthUser authUser) {

        return ResponseEntity.ok(shelfService.getAllShelvesById(authUser.getId()));
    }

    @DeleteMapping("/{shelfId}")
    public ResponseEntity deleteShelf(@AuthenticationUser AuthUser authUser,
                                      @PathVariable Long shelfId) {

        shelfService.hardDeleteShelf(shelfId, authUser.getId());
        return ResponseEntity.ok().body(new TextResponseMessage("Successfully deleted shelves.", HttpStatus.OK.value()));
    }
}
