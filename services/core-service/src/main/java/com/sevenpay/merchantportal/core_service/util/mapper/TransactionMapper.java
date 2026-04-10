package com.sevenpay.merchantportal.core_service.util.mapper;

import com.sevenpay.merchantportal.core_service.model.dto.external.RawTransactionDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.TransactionDetailDto;
import com.sevenpay.merchantportal.core_service.model.dto.response.TransactionDto;
import com.sevenpay.merchantportal.core_service.model.enums.*;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    private TransactionMapper(){}

    public static TransactionDto toTransactionDto(RawTransactionDetailDto rawDto) {
        return TransactionDto.builder()
                .transactionId(rawDto.getTransactionId())
                .transactionType(TransactionType.getByName(rawDto.getTransactionType()))
                .amount(rawDto.getAmount())
                .currency(rawDto.getCurrency())
                .authorizationState(TransactionAuthorizationState.getByName(rawDto.getAuthorizationState()))
                .validity(TransactionValidity.getByName(rawDto.getValidity()))
                .cardBrand(CardBrand.getByName(rawDto.getCardBrand()))
                .acquirerHost(rawDto.getAcquirerHost())
                .authNumber(rawDto.getAuthNumber())
                .rrn(rawDto.getRrn())
                .timestamp(rawDto.getTimestamp())
                .build();
    }

    public static TransactionDetailDto toTransactionDetailDto(RawTransactionDetailDto rawDto) {
        return TransactionDetailDto.builder()
                .transactionId(rawDto.getTransactionId())
                .transactionType(TransactionType.getByName(rawDto.getTransactionType()))
                .amount(rawDto.getAmount())
                .currency(rawDto.getCurrency())
                .authorizationState(TransactionAuthorizationState.getByName(rawDto.getAuthorizationState()))
                .responseCode(rawDto.getResponseCode())
                .validity(TransactionValidity.getByName(rawDto.getValidity()))
                .cardBrand(CardBrand.getByName(rawDto.getCardBrand()))
                .acquirerHost(rawDto.getAcquirerHost())
                .acquirerTid(rawDto.getAcquirerTid())
                .acquirerMid(rawDto.getAcquirerMid())
                .authNumber(rawDto.getAuthNumber())
                .rrn(rawDto.getRrn())
                .acquirerRrn(rawDto.getAcquirerRrn())
                .timestamp(rawDto.getTimestamp())
                .posTimestamp(rawDto.getPosTimestamp())
                .acquirerTimestamp(rawDto.getAcquirerTimestamp())
                .panMasked(rawDto.getPanMasked())
                .panExpireDate(rawDto.getPanExpireDate())
                .amount2(rawDto.getAmount2())
                .currency2(rawDto.getCurrency2())
                .stan(rawDto.getStan())
                .acquirerStan(rawDto.getAcquirerStan())
                .tid(rawDto.getTid())
                .mid(rawDto.getMid())
                .paymentType(rawDto.getPaymentType())
                .installmentNumber(rawDto.getInstallmentNumber())
                .transactionStateMDCProcessing(TransactionStateMDCProcessing.getByName(rawDto.getTransactionStateMDCProcessing()))
                .transactionReference(rawDto.getTransactionReference())
                .tipAmount(rawDto.getTipAmount())
                .build();
    }
}
