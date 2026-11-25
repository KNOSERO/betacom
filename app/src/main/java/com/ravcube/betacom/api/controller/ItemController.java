package com.ravcube.betacom.api.controller;

import com.ravcube.betacom.service.ItemService;
import com.ravcube.betacom.domain.spec.ItemResponse;
import com.ravcube.betacom.api.dto.CreateItemRequest;
import com.ravcube.betacom.api.dto.ItemHistoryEntryResponse;
import com.ravcube.betacom.api.dto.ItemListResponseDTO;
import com.ravcube.betacom.api.dto.ItemResponseDTO;
import com.ravcube.betacom.api.dto.PatchItemRequest;
import com.ravcube.betacom.api.dto.ShareItemRequest;
import com.ravcube.betacom.api.dto.ShareItemResponse;
import com.ravcube.betacom.api.dto.UpdatedItemResponseDTO;
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

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
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
    public ResponseEntity<ShareItemResponse> share(
            @PathVariable("id") UUID id,
            @Valid @RequestBody ShareItemRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @DeleteMapping("/{id}/share/{userId}")
    public ResponseEntity<Void> unshare(
            @PathVariable("id") UUID id,
            @PathVariable("userId") UUID userId) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
