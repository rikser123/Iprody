package com.iprody.orders.dto;

import com.iprody.orders.repository.entity.InquiryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InquiryFilterDto {
  private InquiryStatus status;
  private UUID customerId;
  private UUID managerId;
  private int pageSize = 25;
  private int pageNumber = 0;
  private Map<String, String> sort;
}
