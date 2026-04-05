package com.iprody.clients.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Parameters for customer creation")
public class CreateCustomerRequestDto {
  @NotEmpty(message = "FullName must not be empty")
  @Max(message = "FullName must be less then 30 characters", value = 30)
  @Min(message = "FullName must be greater then 1 characters", value = 1)
  @Schema(description = "Customer fullName", example = "Ivan")
  private String fullName;

  @NotEmpty(message = "Email must not be empty")
  @Max(message = "Email must be less then 50 characters", value = 50)
  @Schema(description = "Customer email", example = "rar@rar.ru")
  private String email;

  @Pattern(regexp = "^(\\+\\d{1,2}\\s?)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$", message = "Phone number must have correct format")
  @Schema(description = "Customer phone number", example = "+7 916 134 45 67")
  private String phoneNumber;
}
