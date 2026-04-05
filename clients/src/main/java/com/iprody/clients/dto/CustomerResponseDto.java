package com.iprody.clients.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Customer response sto")
public class CustomerResponseDto {
  @Schema(description = "Customer id", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID id;

  @Schema(description = "Customer fullName", example = "Ivan Ivanov")
  private String fullName;

  @Schema(description = "Customer email", example = "rar@rar.ru")
  private String email;

  @Schema(description = "Customer phone number", example = "+ 7 916 1345 56 78")
  private String phoneNumber;

  @Schema(description = "Customer phone number", example = "Date of record creation")
  private Instant createdAt;

  @Schema(description = "Customer phone number", example = "Date of record update")
  private Instant updatedAt;
}
