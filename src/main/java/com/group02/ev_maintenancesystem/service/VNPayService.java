package com.group02.ev_maintenancesystem.service;

import com.group02.ev_maintenancesystem.dto.request.VNPayRequest;
import com.group02.ev_maintenancesystem.dto.response.VNPayResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.Map;

public interface VNPayService {
    VNPayResponse createPayment(VNPayRequest request, HttpServletRequest httpServletRequest);
    boolean vnpayCallBack(Map<String, String> param);
}
