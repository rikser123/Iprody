package com.iprody.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateInquiryDto {
  private UUID productRefId;
  private UUID customerRefId;
  private UUID groupRefId;
  private UUID managerRefId;
  private String source;
  private String comment;
  private String note;
}
