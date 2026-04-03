package com.iprody.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentDto {
  private UUID inquiryRefId;
  private BigDecimal amount;
  private String currency;
  private UUID transactionRefId;
  private String note;
}
