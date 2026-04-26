package com.iprody.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InquiryStatusMessageDto {
  private UUID order;
  private InquiryStatus status;
}
