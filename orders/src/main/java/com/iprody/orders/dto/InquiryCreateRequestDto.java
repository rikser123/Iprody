package com.iprody.orders.dto;

import com.iprody.orders.repository.entity.InquirySource;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Parameters for inquiry creation")
public class InquiryCreateRequestDto {
  @NotNull
  @Schema(description = "Product id for inquiry", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID productRefId;

  @NotNull
  @Schema(description = "Customer id for inquiry", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID customerRefId;

  @NotNull
  @Schema(description = "group id for inquiry", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID groupRefId;

  @NotNull
  @Schema(description = "Source of inquiry", example = "LANDING_PAGE")
  private InquirySource source;

  @Schema(description = "Comment for inquiry", example = "Comment")
  private String comment;

  @Schema(description = "Note for inquiry", example = "Note")
  private String note;
}
