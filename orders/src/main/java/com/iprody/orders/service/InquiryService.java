package com.iprody.orders.service;

import com.iprody.orders.dto.CreateInquiryDto;
import com.iprody.orders.dto.InquiryFilterDto;
import com.iprody.orders.dto.UpdateInquiryDto;
import com.iprody.orders.repository.entity.Inquiry;
import org.springframework.data.domain.Page;

import java.util.UUID;

/**
 * Service interface for managing inquiry-related operations.
 * Provides business logic for creating, updating, and retrieving inquiries.
 */
public interface InquiryService {

  /**
   * Creates a new inquiry based on the provided data.
   *
   * @param dto the data transfer object containing inquiry creation information
   * @return the created Inquiry entity
   */
  Inquiry create(CreateInquiryDto dto);

  /**
   * Updates an existing inquiry identified by the given ID.
   *
   * @param id the unique identifier of the inquiry to update
   * @param dto the data transfer object containing updated inquiry information
   * @return the updated Inquiry entity
   */
  Inquiry update(UUID id, UpdateInquiryDto dto);

  /**
   * Retrieves an inquiry by its unique identifier.
   *
   * @param id the unique identifier of the inquiry
   * @return the Inquiry entity
   */
  Inquiry findById(UUID id);

  /**
   * Retrieves a paginated list of inquiries based on the provided filter criteria.
   *
   * @param dto the filter data transfer object containing pagination parameters,
   *            sorting criteria, and filtering conditions
   * @return a Page object containing inquiries matching the filter criteria
   */
  Page<Inquiry> findAll(InquiryFilterDto dto);
}