package com.iprody.orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Error dto")
public class ErrorDto {
  @Schema(description = "Time when the error occurred")
  @NotNull
  private Instant timestamp;

  @Schema(description = "HTTP status code")
  @NotNull
  private HttpStatus status;

  @Schema(description = "Short error title")
  private String error;

  @Schema(description = "Detailed error description")
  @NotNull
  private String message;

  @Schema(description = "The request URI that caused the error")
  private String path;
}
