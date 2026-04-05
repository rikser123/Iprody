package com.iprody.clients.service;

import com.iprody.clients.dto.CreateCustomerRequestDto;
import com.iprody.clients.dto.CustomerFilterRequestDto;
import com.iprody.clients.dto.CustomerResponseDto;
import com.iprody.clients.mapper.CustomerMapper;
import com.iprody.clients.repository.CustomerRepository;
import com.iprody.clients.repository.entity.ContactDetails;
import com.iprody.clients.repository.entity.Customer;
import com.iprody.clients.repository.specification.CustomerSpecification;
import com.iprody.clients.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;



public class CustomerServiceTest {
  private CustomerRepository customerRepository;
  private CustomerMapper mapper = Mappers.getMapper(CustomerMapper.class);
  private CustomerService customerService;

  @BeforeEach
  void init() {
    customerRepository = Mockito.mock(CustomerRepository.class);
    customerService = new CustomerServiceImpl(customerRepository, mapper);
  }

  @Test
  void createCustomer() {
    var dto = createCustomerDto();

    when(customerRepository.save(any())).thenReturn(createCustomerEntity());

    var result = customerService.createCustomer(dto);

    assertThat(result.getEmail()).isEqualTo(dto.getEmail());
    assertThat(result.getFullName()).isEqualTo(dto.getFullName());
    assertThat(result.getPhoneNumber()).isEqualTo(dto.getPhoneNumber());
  }

  @Test
  void updateCustomer() {
    var dto = createCustomerDto();
    dto.setEmail("email1");
    var entity = createCustomerEntity();

    when(customerRepository.findById(any())).thenReturn(Optional.of(entity));
    when(customerRepository.save(argThat(arg -> {
      assertThat(arg.getContactDetails().getEmail()).isEqualTo(dto.getEmail());
      return true;
    }))).thenReturn(createCustomerEntity());

    var result = customerService.update(UUID.randomUUID(), dto);

    assertThat(result.getEmail()).isEqualTo(dto.getEmail());
    assertThat(result.getFullName()).isEqualTo(dto.getFullName());
    assertThat(result.getPhoneNumber()).isEqualTo(dto.getPhoneNumber());
  }

  @Test
  void findAll() {
    var filterDto = new CustomerFilterRequestDto();
    filterDto.setFullName("fullName");
    filterDto.setPageSize(25);
    filterDto.setPageNumber(0);

    when(customerRepository.findAll(any(CustomerSpecification.class), any(PageRequest.class)))
        .thenReturn(new PageImpl(Collections.emptyList(), PageRequest.of(1, 1), 5));
    customerService.findAll(filterDto);

    verify(customerRepository).findAll(any(CustomerSpecification.class), any(PageRequest.class));
  }

  private static Customer createCustomerEntity() {
    var customer = new Customer();
    customer.setFullName("fullName");
    var details = new ContactDetails();
    details.setEmail("email");
    details.setPhoneNumber("phone");
    customer.setContactDetails(details);

    return customer;
  }

  private static CreateCustomerRequestDto createCustomerDto() {
    var dto = new CreateCustomerRequestDto();
    dto.setEmail("email");
    dto.setFullName("fullName");
    dto.setPhoneNumber("phone");

    return dto;
  }

  private static CustomerResponseDto createWithContactsDto() {
    var dto = new CustomerResponseDto();
    dto.setEmail("email");
    dto.setPhoneNumber("phone");
    dto.setFullName("fullName");

    return dto;
  }
}
