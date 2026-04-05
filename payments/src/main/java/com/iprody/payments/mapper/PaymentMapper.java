package com.iprody.payments.mapper;

import com.iprody.payments.dto.CreatePaymentDto;
import com.iprody.payments.repository.entity.Payment;
import com.iprody.payments.repository.entity.PaymentStatus;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
  Payment mapToEntity(CreatePaymentDto dto);

  @AfterMapping
  default void afterEntity(CreatePaymentDto dto, @MappingTarget Payment entity) {
    entity.setStatus(PaymentStatus.RECEIVED);
  }
}
