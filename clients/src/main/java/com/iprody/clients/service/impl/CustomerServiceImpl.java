package com.iprody.clients.service.impl;

import com.iprody.clients.dto.CreateCustomerRequestDto;
import com.iprody.clients.dto.CustomerFilterRequestDto;
import com.iprody.clients.dto.CustomerResponseDto;
import com.iprody.clients.mapper.CustomerMapper;
import com.iprody.clients.repository.CustomerRepository;
import com.iprody.clients.repository.entity.Customer;
import com.iprody.clients.repository.specification.CustomerSpecification;
import com.iprody.clients.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;

  @Override
  public CustomerResponseDto createCustomer(CreateCustomerRequestDto dto) {
    var entity = customerMapper.mapToEntity(dto);
    entity = customerRepository.save(entity);

    return customerMapper.mapToDto(entity);
  }

  @Override
  public CustomerResponseDto update(UUID id, CreateCustomerRequestDto dto) {
    var existingCustomer = findById(id);
    customerMapper.update(existingCustomer, dto);

    customerRepository.save(existingCustomer);
    return customerMapper.mapToDto(existingCustomer);
  }

  @Override
  public Page<CustomerResponseDto> findAll(CustomerFilterRequestDto dto) {
    var specification = new CustomerSpecification(dto);
    var pageRequest = PageRequest.of(dto.getPageNumber(), dto.getPageSize());

    return customerRepository.findAll(specification, pageRequest).map(customerMapper::mapToDto);
  }

  public CustomerResponseDto findByIdRequest(UUID id) {
    var existingCustomer = findById(id);
    return customerMapper.mapToDto(existingCustomer);
  }

  private Customer findById(UUID id) {
    return customerRepository.findById(id).
        orElseThrow(() -> new EntityNotFoundException("Customer with id" + id + " not found"));
  }
}
