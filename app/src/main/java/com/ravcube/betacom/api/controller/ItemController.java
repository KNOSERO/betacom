package com.ravcube.betacom.api.controller;

import com.ravcube.betacom.api.dto.*;
import com.ravcube.betacom.service.ItemPermissionService;
import com.ravcube.betacom.service.ItemService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemPermissionService itemPermissionService;

    public ItemController(ItemService itemService, ItemPermissionService itemPermissionService) {
        this.itemService = itemService;
        this.itemPermissionService = itemPermissionService;
    }

    @PostMapping
    public ResponseEntity<ItemResponseDTO> create(@Valid @RequestBody CreateItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(itemService.create(request.title(), request.content()));
    }

    @GetMapping
    public ResponseEntity<List<ItemListResponseDTO>> list() {
        return ResponseEntity.status(HttpStatus.OK).body(itemService.findAll());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdatedItemResponseDTO> update(
            @PathVariable("id") UUID id,
            @Valid @RequestBody PatchItemRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(itemService.update(
                id,
                request.title(),
                request.content(),
                request.version().longValue())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        itemService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<ItemHistoryEntryResponse>> history(@PathVariable("id") UUID id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<ShareItemResponseDTO> share(
            @PathVariable("id") UUID id,
            @Valid @RequestBody ShareItemRequest request) {
        ShareItemResponseWithStatusDTO result = itemPermissionService.share(id, request.userId(), request.role().name());
        return ResponseEntity.status(result.status()).body(result.body());
    }

    @DeleteMapping("/{id}/share/{userId}")
    public ResponseEntity<Void> unshare(
            @PathVariable("id") UUID id,
            @PathVariable("userId") UUID userId) {
        itemPermissionService.delete(id, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
