package com.ravcube.betacom.api.dto;

import org.springframework.http.HttpStatus;

public record ShareItemResponseWithStatusDTO(ShareItemResponseDTO body, HttpStatus status) {

}
