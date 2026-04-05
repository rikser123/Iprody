package com.iprody.clients.controller;

import com.iprody.clients.dto.CreateCustomerRequestDto;
import com.iprody.clients.dto.CustomerFilterRequestDto;
import com.iprody.clients.dto.CustomerResponseDto;
import com.iprody.clients.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Tag(name = "API for customers")
@ApiResponses(
    value = {
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
  private final CustomerService customerService;

  @PutMapping("/{id}")
  @Operation(description = "Update customer details")
  public CustomerResponseDto update(
     @Parameter(description = "Customer id", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
     @PathVariable
     UUID id,

     @Valid
     @RequestBody
     @Parameter(description = "Parameters for customer update", required = true)
     CreateCustomerRequestDto dto
  ) {
    return customerService.update(id, dto);
  }

  @PutMapping("/list")
  @Operation(description = "Get a list of clients")
  public Page<CustomerResponseDto> getList(
    @Valid
    @RequestBody
    @Parameter(description = "Parameters for customer filtration", required = true)
    CustomerFilterRequestDto dto
  ) {
    return customerService.findAll(dto);
  }

  @PostMapping
  @Operation(description = "Create new Customer")
  public CustomerResponseDto create(
    @Valid
    @RequestBody
    @Parameter(description = "Parameters for customer creation", required = true)
    CreateCustomerRequestDto dto
  ) {
    return customerService.createCustomer(dto);
  }

  @GetMapping("/{id}")
  @Operation(description = "Find customer by ID")
  public CustomerResponseDto findById(
    @Parameter(description = "Customer id", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
    @PathVariable
    UUID id
  ) {
    return customerService.findByIdRequest(id);
  }
}
