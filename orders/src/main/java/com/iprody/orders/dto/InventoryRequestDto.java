package com.iprody.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequestDto {
  private UUID inquiryId;
  private UUID groupRefId;
}
