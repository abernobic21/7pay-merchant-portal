package com.sevenpay.merchantportal.core_service.util.mapper;

import com.sevenpay.merchantportal.core_service.model.dto.external.RawTerminalDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.TerminalDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.TerminalDto;
import com.sevenpay.merchantportal.core_service.model.enums.TerminalStatus;

public class TerminalMapper {
    private TerminalMapper() {
    }

    public static TerminalDto toTerminalDto(RawTerminalDto rawTerminalDto) {
        return TerminalDto.builder()
                .tid(rawTerminalDto.getTid())
                .terminalType(rawTerminalDto.getTerminalType())
                .status(rawTerminalDto.getStatus())
                .midReference(rawTerminalDto.getMidReference())
                .merchantCategoryCode(rawTerminalDto.getMerchantCategoryCode())
                .currency(rawTerminalDto.getCurrency())
                .build();
    }

    public static TerminalDetailDto toTerminalDetailDto(RawTerminalDto rawTerminalDto) {
        return TerminalDetailDto.builder()
                .tid(rawTerminalDto.getTid())
                .terminalType(rawTerminalDto.getTerminalType())
                .status(TerminalStatus.getByName(rawTerminalDto.getStatus()))
                .midReference(rawTerminalDto.getMidReference())
                .phoneNumber(rawTerminalDto.getPhoneNumber())
                .emailContact(rawTerminalDto.getEmailContact())
                .currency(rawTerminalDto.getCurrency())
                .merchantCategoryCode(rawTerminalDto.getMerchantCategoryCode())
                .authenticationMode(rawTerminalDto.getAuthenticationMode())
                .softPosId(rawTerminalDto.getSoftPosId())
                .softPosStatus(rawTerminalDto.getSoftPosStatus())
                .acquirerTerminalData(rawTerminalDto.getAcquirerTerminalData())
                .tipEnabled(rawTerminalDto.getTipEnabled())
                .build();
    }
}
