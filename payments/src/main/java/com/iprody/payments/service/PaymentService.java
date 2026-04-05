package com.iprody.payments.service;

import com.iprody.payments.dto.CreatePaymentDto;
import com.iprody.payments.dto.PaymentFilterDto;
import com.iprody.payments.repository.entity.Payment;
import org.springframework.data.domain.Page;

import java.util.UUID;

/**
 * Service interface for managing payment-related operations.
 * Provides business logic for creating, retrieving, and filtering payments.
 */
public interface PaymentService {

  /**
   * Creates a new payment based on the provided data.
   *
   * @param dto the data transfer object containing payment creation information
   * @return the created Payment entity
   */
  Payment create(CreatePaymentDto dto);

  /**
   * Retrieves a payment by its unique identifier.
   *
   * @param id the unique identifier of the payment
   * @return the Payment entity
   */
  Payment findById(UUID id);

  /**
   * Retrieves a paginated list of payments based on the provided filter criteria.
   *
   * @param dto the filter data transfer object containing pagination parameters,
   *            sorting criteria, and filtering conditions
   * @return a Page object containing payments matching the filter criteria
   */
  Page<Payment> findAll(PaymentFilterDto dto);
}
