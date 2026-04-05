package com.iprody.orders.dto;

import com.iprody.orders.repository.entity.InquiryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInquiryDto {
  private UUID managerRefId;
  private InquiryStatus status;
}
