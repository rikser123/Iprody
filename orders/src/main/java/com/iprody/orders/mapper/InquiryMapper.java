package com.iprody.orders.mapper;

import com.iprody.orders.dto.InquiryCreateRequestDto;
import com.iprody.orders.dto.InquiryResponseDto;
import com.iprody.orders.repository.entity.Inquiry;
import com.iprody.orders.repository.entity.InquiryStatus;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InquiryMapper {
  Inquiry mapToEntity(InquiryCreateRequestDto dto);

  InquiryResponseDto mapToDto(Inquiry inquiry);

  @AfterMapping
  default void afterMapToEntity(InquiryCreateRequestDto dto, @MappingTarget Inquiry entity) {
    entity.setStatus(InquiryStatus.NEW);
  }
}
