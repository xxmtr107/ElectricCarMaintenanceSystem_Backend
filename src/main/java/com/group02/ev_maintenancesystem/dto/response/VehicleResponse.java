package com.group02.ev_maintenancesystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponse {
      Long id;
      String licensePlate;
      String vin;
      Integer currentKm;
      YearMonth purchaseYear;
      Long modelId;
      Long customerId;
      LocalDateTime createdAt;
      String createBy;
      LocalDateTime updateAt;
      String updateBy;
}
