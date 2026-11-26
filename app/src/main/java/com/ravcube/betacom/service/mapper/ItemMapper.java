package com.ravcube.betacom.service.mapper;

import com.ravcube.betacom.api.dto.ItemResponseDTO;
import com.ravcube.betacom.api.dto.UpdatedItemResponseDTO;
import com.ravcube.betacom.domain.spec.ItemResponse;
import com.ravcube.betacom.domain.spec.ItemHistoryEntry;
import com.ravcube.betacom.domain.spec.UpdatedItemResponse;
import com.ravcube.betacom.api.dto.ItemHistoryEntryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ItemMapper {

    ItemResponseDTO toResponseDTO(ItemResponse itemResponse);

    UpdatedItemResponseDTO toUpdatedResponseDTO(UpdatedItemResponse updatedItemResponse);

    ItemHistoryEntryResponse toHistoryResponse(ItemHistoryEntry itemHistoryEntry);
}
