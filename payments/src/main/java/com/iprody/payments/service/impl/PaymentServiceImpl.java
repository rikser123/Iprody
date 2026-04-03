package com.iprody.payments.service.impl;

import com.iprody.payments.dto.CreatePaymentDto;
import com.iprody.payments.dto.PaymentFilterDto;
import com.iprody.payments.mapper.PaymentMapper;
import com.iprody.payments.repository.PaymentRepository;
import com.iprody.payments.repository.entity.Payment;
import com.iprody.payments.repository.specification.PaymentSpecification;
import com.iprody.payments.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
  private final PaymentRepository paymentRepository;
  private final PaymentMapper paymentMapper;

  @Override
  public Payment create(CreatePaymentDto dto) {
    var entity = paymentMapper.mapToEntity(dto);
    entity = paymentRepository.save(entity);

    return entity;
  }

  @Override
  public Payment findById(UUID id) {
    return paymentRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Payment with id " + id + " not found"));
  }

  @Override
  public Page<Payment> findAll(PaymentFilterDto dto) {
    var specification = new PaymentSpecification(dto);
    var pageRequest = PageRequest.of(dto.getPageNumber(), dto.getPageSize(), boundSort(dto.getSort()));

    return paymentRepository.findAll(specification, pageRequest);
  }

  private Sort boundSort(Map<String, String> sort) {
    return Optional.ofNullable(sort)
        .orElse(Collections.emptyMap())
        .entrySet()
        .stream()
        .map(entry -> {
          var field = entry.getKey();
          var direction = entry.getValue();

          return Sort.by(direction, field);
        })
        .reduce(Sort::and)
        .orElse(Sort.unsorted());
  }
}
