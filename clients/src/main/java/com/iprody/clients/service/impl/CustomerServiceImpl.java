package com.iprody.clients.service.impl;

import com.iprody.clients.dto.CreateCustomerDto;
import com.iprody.clients.dto.CustomerFilterDto;
import com.iprody.clients.dto.CustomerWithContactsDto;
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
  public CustomerWithContactsDto createCustomer(CreateCustomerDto dto) {
    var entity = customerMapper.mapToEntity(dto);
    entity = customerRepository.save(entity);

    return customerMapper.mapToDto(entity);
  }

  @Override
  public CustomerWithContactsDto update(UUID id, CreateCustomerDto dto) {
    var existingCustomer = findById(id);
    customerMapper.update(existingCustomer, dto);

    customerRepository.save(existingCustomer);
    return customerMapper.mapToDto(existingCustomer);
  }

  @Override
  public Customer findById(UUID id) {
    return customerRepository.findById(id).
        orElseThrow(() -> new EntityNotFoundException("Customer with id" + id + " not found"));
  }

  @Override
  public Page<Customer> findAll(CustomerFilterDto dto) {
    var specification = new CustomerSpecification(dto);
    var pageRequest = PageRequest.of(dto.getPageNumber(), dto.getPageSize());

    return customerRepository.findAll(specification, pageRequest);
  }
}
