package com.iprody.orders.dto;

import com.iprody.orders.repository.entity.InquiryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Parameters for inquiry filtration")
public class InquiryFilterDto {
  @Schema(description = "Inquiry status", example = "New")
  private InquiryStatus status;

  @Schema(description = "Customer id for inquiry", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID customerId;

  @Schema(description = "Manager id for inquiry", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID managerId;

  @Min(message = "Parameter pagSize must be greater or equal 10", value = 10)
  @Schema(description = "Page size", example = "25")
  private int pageSize = 25;

  @PositiveOrZero(message = "Parameter pageNumber must be positive")
  @Schema(description = "Page number", example = "1")
  private int pageNumber = 0;

  @Schema(description = "Sort", example = "{ customerId: 'asc'}")
  private Map<String, String> sort;
}
