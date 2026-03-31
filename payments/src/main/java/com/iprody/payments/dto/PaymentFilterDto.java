package com.iprody.payments.dto;

import com.iprody.payments.repository.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentFilterDto {
  private UUID id;
  private PaymentStatus status;
  private UUID inquiryRefId;
  private Instant fromDate;
  private Instant toDate;
  private LocalDate date;
  private int pageSize;
  private int pageNumber;
  private Map<String, String> sort;
}
