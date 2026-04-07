package com.iprody.clients.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Parameters for customer creation")
public class CreateCustomerRequestDto {
  @NotEmpty(message = "FullName must not be empty")
  @Size(min = 1, max = 30, message = "FullName must be between 1 and 30 characters")
  @Schema(description = "Customer fullName", example = "Ivan")
  private String fullName;

  @NotEmpty(message = "Email must not be empty")
  @Size(max = 50, message = "Email must be less than 50 characters")
  @Schema(description = "Customer email", example = "rar@rar.ru")
  @Email
  private String email;

  @Pattern(regexp = "^(\\+\\d{1,2}\\s?)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$",
      message = "Phone number must have correct format")
  @Schema(description = "Customer phone number", example = "+7 916 134 45 67")
  private String phoneNumber;
}
