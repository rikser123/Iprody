package com.iprody.orders.dto;

import com.iprody.orders.repository.entity.InquirySource;
import com.iprody.orders.repository.entity.InquiryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Inquiry response DTO")
public class InquiryResponseDto {
  @Schema(description = "Inquiry id", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID id;

  @Schema(description = "Inquiry product id", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID productRefId;

  @Schema(description = "Group product id", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID groupRefId;

  @Schema(description = "Inquiry customer id", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID customerRefId;

  @Schema(description = "Inquiry source id", example = "TELEGRAM")
  private InquirySource source;

  @Schema(description = "Inquiry comment", example = "comment")
  private String comment;

  @Schema(description = "Inquiry status", example = "NEW")
  private InquiryStatus status;

  @Schema(description = "Inquiry note", example = "note")
  private String note;

  @Schema(description = "Inquiry created time", example = "12345")
  private Instant createdAt;

  @Schema(description = "Inquiry updated time", example = "12345")
  private Instant updatedAt;
}
