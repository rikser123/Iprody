package com.iprody.clients.service;

import com.iprody.clients.dto.CreateCustomerDto;
import com.iprody.clients.dto.CustomerFilterDto;
import com.iprody.clients.dto.CustomerWithContactsDto;
import com.iprody.clients.repository.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.UUID;

/**
 * Service interface for managing customer-related operations.
 * Provides business logic for creating, updating, and retrieving customers
 * with their associated contact information.
 */
public interface CustomerService {

  /**
   * Creates a new customer based on the provided data.
   *
   * @param dto the data transfer object containing customer creation information
   * @return a DTO containing the created customer with its contacts
   */
  CustomerWithContactsDto createCustomer(CreateCustomerDto dto);

  /**
   * Updates an existing customer identified by the given ID.
   *
   * @param id the unique identifier of the customer to update
   * @param dto the data transfer object containing updated customer information
   * @return a DTO containing the updated customer with its contacts
   */
  CustomerWithContactsDto update(UUID id, CreateCustomerDto dto);

  /**
   * Retrieves a customer by their unique identifier.
   *
   * @param id the unique identifier of the customer
   * @return the Customer entity
   */
  Customer findById(UUID id);

  /**
   * Retrieves a paginated list of customers based on the provided filter criteria.
   *
   * @param dto the filter data transfer object containing pagination parameters
   *            and filtering conditions
   * @return a Page object containing customers matching the filter criteria

   */
  Page<Customer> findAll(CustomerFilterDto dto);
}