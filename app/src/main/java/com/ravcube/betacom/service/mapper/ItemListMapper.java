package com.ravcube.betacom.service.mapper;

import com.ravcube.betacom.api.dto.ItemListResponseDTO;
import com.ravcube.betacom.database.projection.ItemListProjection;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ItemListMapper {

    ItemListResponseDTO toResponse(ItemListProjection projection);
}
