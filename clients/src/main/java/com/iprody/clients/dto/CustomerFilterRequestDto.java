package com.iprody.clients.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Parameters for customer list filter")
public class CustomerFilterRequestDto {
  @NotEmpty(message = "Parameter fullName must not be empty")
  @Schema(description = "FullName filter")
  private String fullName;

  @PositiveOrZero(message = "Parameter page size must be greater or equal 0")
  @Schema(description = "Page size")
  private int pageSize = 25;

  @PositiveOrZero(message = "Parameter pageNumber size must be greater or equal 0")
  @Schema(description = "Page number")
  private int pageNumber;
}
