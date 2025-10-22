package com.group02.ev_maintenancesystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VNPayResponse {
    private String paymentUrl;
    private String transactionCode;
}
