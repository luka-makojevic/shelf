package com.htec.filesystem.controller;

import com.htec.filesystem.annotation.AuthUser;
import com.htec.filesystem.annotation.AuthenticationUser;
import com.htec.filesystem.dto.ShelfDTO;
import com.htec.filesystem.model.request.CreateShelfRequestModel;
import com.htec.filesystem.model.request.ShelfEditRequestModel;
import com.htec.filesystem.model.response.ShelfContentResponseModel;
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

    @PostMapping
    public ResponseEntity<TextResponseMessage> createShelf(@RequestBody CreateShelfRequestModel createShelfRequestModel,
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
    public ResponseEntity<List<ShelfDTO>> getAllShelves(@AuthenticationUser AuthUser authUser) {

        return ResponseEntity.ok(shelfService.getAllShelvesById(authUser.getId()));
    }

    @GetMapping("/record/{shelfId}")
    public ResponseEntity<ShelfDTO> getShelf(@AuthenticationUser AuthUser authUser,  @PathVariable Long shelfId) {

        return ResponseEntity.ok(shelfService.getShelfById(authUser.getId(), shelfId));
    }

    @DeleteMapping("/{shelfId}")
    public ResponseEntity<TextResponseMessage> deleteShelf(@AuthenticationUser AuthUser authUser,
                                                           @PathVariable Long shelfId) {

        shelfService.hardDeleteShelf(shelfId, authUser.getId());
        return ResponseEntity.ok().body(new TextResponseMessage("Successfully deleted shelves.", HttpStatus.OK.value()));
    }

    @GetMapping("/{shelfId}")
    public ResponseEntity<ShelfContentResponseModel> getFirstLevelContent(@AuthenticationUser AuthUser authUser, @PathVariable Long shelfId) {

        return ResponseEntity.ok(shelfService.getShelfContent(shelfId, authUser.getId()));
    }

    @PutMapping("/rename")
    public ResponseEntity<TextResponseMessage> updateShelfName(@AuthenticationUser AuthUser authUser, @RequestBody ShelfEditRequestModel shelfEditRequestModel) {

        shelfService.updateShelfName(shelfEditRequestModel, authUser.getId());

        return ResponseEntity.ok().body(new TextResponseMessage("Shelf name updated.", HttpStatus.OK.value()));
    }

    @GetMapping("/trash")
    public ResponseEntity<ShelfContentResponseModel> getFilesAndFoldersFromTrash(@AuthenticationUser AuthUser authUser) {

        return ResponseEntity.ok(shelfService.getFirstLevelTrash(authUser.getId()));
    }

    @GetMapping("/check/{shelfId}")
    public ResponseEntity<Object> checkShelfAccessRights(@AuthenticationUser AuthUser authUser, @PathVariable Long shelfId) {

        shelfService.checkShelfAccessRights(shelfId, authUser.getId());

        return ResponseEntity.ok().build();
    }
}
