package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.VNPayRequest;
import com.group02.ev_maintenancesystem.dto.response.VNPayResponse;

import java.math.BigDecimal;
import java.util.Map;

public interface VNPayService {
    VNPayResponse createPayment(VNPayRequest request);
    boolean vnpayCallBack(Map<String, String> param);
}
