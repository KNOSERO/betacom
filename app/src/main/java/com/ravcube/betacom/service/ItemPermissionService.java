package com.ravcube.betacom.service;

import com.ravcube.betacom.api.dto.ShareItemResponseWithStatusDTO;
import com.ravcube.betacom.domain.ItemPermissionProcessService;
import com.ravcube.betacom.domain.spec.ShareItemResponse;
import com.ravcube.betacom.service.mapper.ShareItemMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ItemPermissionService {

    private final ItemPermissionProcessService processService;
    private final ShareItemMapper shareItemMapper;

    public ItemPermissionService(ItemPermissionProcessService processService, ShareItemMapper shareItemMapper) {
        this.processService = processService;
        this.shareItemMapper = shareItemMapper;
    }

    @Transactional
    public ShareItemResponseWithStatusDTO share(UUID itemId, UUID userId, String role) {
        ShareItemResponse result = processService.share(itemId, userId, role);
        return new ShareItemResponseWithStatusDTO(shareItemMapper.toResponse(result), getStatus(result));
    }

    @Transactional
    public void delete(UUID itemId, UUID userId) {
        processService.delete(itemId, userId);
    }

    private HttpStatus getStatus(ShareItemResponse result) {
        return result.created() ? HttpStatus.CREATED : HttpStatus.OK;
    }
}
