package com.group02.ev_maintenancesystem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VNPayRequest {
    private Long inVoiceId;
    private String bankCode;
    private BigDecimal amount;
}
