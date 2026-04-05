package com.iprody.clients.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerDto {
  @NotEmpty(message = "FullName must not be empty")
  @Max(message = "FullName must be less then 30 characters", value = 30)
  private String fullName;

  @NotEmpty(message = "Email must not be empty")
  @Max(message = "Email must be less then 50 characters", value = 50)
  private String email;

  private String phoneNumber;
}
