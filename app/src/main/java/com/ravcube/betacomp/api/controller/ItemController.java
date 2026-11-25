package com.ravcube.betacomp.api.controller;

import com.ravcube.betacomp.api.dto.CreateItemRequest;
import com.ravcube.betacomp.api.dto.ItemHistoryEntryResponse;
import com.ravcube.betacomp.api.dto.ItemListResponse;
import com.ravcube.betacomp.api.dto.ItemResponse;
import com.ravcube.betacomp.api.dto.PatchItemRequest;
import com.ravcube.betacomp.api.dto.ShareItemRequest;
import com.ravcube.betacomp.api.dto.ShareItemResponse;
import com.ravcube.betacomp.api.dto.UpdatedItemResponse;
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

    @PostMapping
    public ResponseEntity<ItemResponse> create(@RequestBody CreateItemRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping
    public ResponseEntity<List<ItemListResponse>> list() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdatedItemResponse> update(
            @PathVariable("id") UUID id,
            @RequestBody PatchItemRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<ItemHistoryEntryResponse>> history(@PathVariable("id") UUID id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<ShareItemResponse> share(
            @PathVariable("id") UUID id,
            @RequestBody ShareItemRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @DeleteMapping("/{id}/share/{userId}")
    public ResponseEntity<Void> unshare(
            @PathVariable("id") UUID id,
            @PathVariable("userId") UUID userId) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
