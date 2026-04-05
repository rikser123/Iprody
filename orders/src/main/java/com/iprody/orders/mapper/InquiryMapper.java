package com.iprody.orders.mapper;

import com.iprody.orders.dto.CreateInquiryDto;
import com.iprody.orders.repository.entity.Inquiry;
import com.iprody.orders.repository.entity.InquiryStatus;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InquiryMapper {
  Inquiry mapToEntity(CreateInquiryDto dto);

  @AfterMapping
  default void afterMapToEntity(CreateInquiryDto dto, @MappingTarget Inquiry entity) {
    entity.setStatus(InquiryStatus.NEW);
  }
}
