package com.iprody.payments.service;

import com.iprody.payments.dto.CreatePaymentDto;
import com.iprody.payments.dto.PaymentFilterDto;
import com.iprody.payments.mapper.PaymentMapper;
import com.iprody.payments.repository.PaymentRepository;
import com.iprody.payments.repository.entity.Payment;
import com.iprody.payments.repository.specification.PaymentSpecification;
import com.iprody.payments.service.impl.PaymentServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PaymentServiceTest {
  private PaymentRepository paymentRepository;
  private PaymentMapper mapper = Mappers.getMapper(PaymentMapper.class);
  private PaymentService paymentService;

  @BeforeEach
  void init() {
    paymentRepository = Mockito.mock(PaymentRepository.class);
    paymentService = new PaymentServiceImpl(paymentRepository, mapper);
  }

  @Test
  void shouldCreateInquiry() {
    var dto = createPaymentDto();

    when(paymentRepository.save(any())).thenReturn(createPayment());
    var result = paymentService.create(dto);

    assertThat(result.getCurrency()).isEqualTo(dto.getCurrency());
    assertThat(result.getNote()).isEqualTo(dto.getNote());
  }

  @Test
  void shouldFindById() {
    var entity = createPayment();

    when(paymentRepository.findById(any())).thenReturn(Optional.of(entity));
    var result = paymentService.findById(UUID.randomUUID());

    assertThat(result.getCurrency()).isEqualTo(entity.getCurrency());
    assertThat(result.getNote()).isEqualTo(entity.getNote());
  }

  @Test
  void findByIdThrowExpIfEmpty() {
    when(paymentRepository.findById(any())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> paymentService.findById(UUID.randomUUID()))
      .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void findAll() {
    var filterDto = new PaymentFilterDto();
    paymentService.findAll(filterDto);

    verify(paymentRepository).findAll(any(PaymentSpecification.class), any(PageRequest.class));
  }

  private static CreatePaymentDto createPaymentDto() {
    var dto = new CreatePaymentDto();
    dto.setCurrency("USD");
    dto.setNote("note");

    return dto;
  }

  private static Payment createPayment() {
    var payment = new Payment();
    payment.setCurrency("USD");
    payment.setNote("note");

    return payment;
  }
}
