package com.iprody.orders.controller;

import com.iprody.orders.dto.InquiryCreateRequestDto;
import com.iprody.orders.dto.InquiryFilterDto;
import com.iprody.orders.dto.InquiryResponseDto;
import com.iprody.orders.service.InquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "API for inquiry")
@ApiResponses(
    value = {
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
@RequestMapping("/api/v1/inquiries")
@RequiredArgsConstructor
@RestController
public class InquiryController {
  private final InquiryService inquiryService;

  @GetMapping()
  @Operation(description = "Get a list of inquiries")
  public Page<InquiryResponseDto> getList(
      @Valid
      @ParameterObject()
      InquiryFilterDto dto
  ) {
    return inquiryService.findAll(dto);
  }

  @PostMapping
  @Operation(description = "Create new Inquiry")
  public InquiryResponseDto create(
      @Valid
      @RequestBody
      @Parameter(description = "Parameters for inquiry creation", required = true)
      InquiryCreateRequestDto dto
  ) {
    return inquiryService.create(dto);
  }

  @GetMapping("/{id}")
  @Operation(description = "Find inquiry by ID")
  public InquiryResponseDto findById(
      @Parameter(description = "Inquiry id", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
      @PathVariable
      UUID id
  ) {
    return inquiryService.findByIdRequest(id);
  }
}
