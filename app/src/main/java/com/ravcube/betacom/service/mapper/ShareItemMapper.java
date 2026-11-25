package com.ravcube.betacom.service.mapper;

import com.ravcube.betacom.api.dto.ShareItemResponseDTO;
import com.ravcube.betacom.domain.spec.ShareItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ShareItemMapper {

    ShareItemResponseDTO toResponse(ShareItemResponse response);
}
