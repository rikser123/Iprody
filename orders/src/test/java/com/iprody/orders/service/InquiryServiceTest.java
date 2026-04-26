package com.iprody.orders.service;

import com.iprody.orders.dto.InquiryCreateRequestDto;
import com.iprody.orders.dto.InquiryFilterDto;
import com.iprody.orders.dto.UpdateInquiryDto;
import com.iprody.orders.kafka.InquiryProducer;
import com.iprody.orders.mapper.InquiryMapper;
import com.iprody.orders.repository.InquiryRepository;
import com.iprody.orders.repository.entity.Inquiry;
import com.iprody.orders.repository.entity.InquirySource;
import com.iprody.orders.repository.entity.InquiryStatus;
import com.iprody.orders.repository.specification.InquirySpecification;
import com.iprody.orders.service.impl.InquiryServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InquiryServiceTest {
  private InquiryRepository inquiryRepository;
  private InquiryMapper mapper = Mappers.getMapper(InquiryMapper.class);
  private InquiryProducer inquiryProducer;
  private InquiryService inquiryService;

  @BeforeEach
  void init() {
    inquiryRepository = Mockito.mock(InquiryRepository.class);
    inquiryProducer = Mockito.mock(InquiryProducer.class);
    inquiryService = new InquiryServiceImpl(inquiryRepository, mapper, inquiryProducer);
  }

  @Test
  void shouldCreate() {
    var dto = createInquiryDto();

    when(inquiryRepository.save(any())).thenReturn(createInquiry());
    var result = inquiryService.create(dto);

    assertThat(result.getComment()).isEqualTo(dto.getComment());
    assertThat(result.getNote()).isEqualTo(dto.getNote());
    assertThat(result.getSource()).isEqualTo(dto.getSource());
    verify(inquiryProducer, times(1)).sendInventoryMessage(result.getId(), result.getGroupRefId());
  }


  @Test
  void shouldCancel() {
    var inquiry = createInquiry();

    when(inquiryRepository.findById(any())).thenReturn(Optional.of(inquiry));
    inquiryService.cancel(UUID.randomUUID());

    verify(inquiryProducer, times(1)).sendCancellationRequestMessage(inquiry.getId(), inquiry.getGroupRefId());
  }

  @Test
  void shouldUpdate() {
    var updateDto = new UpdateInquiryDto();
    updateDto.setManagerRefId(UUID.randomUUID());
    updateDto.setStatus(InquiryStatus.IN_PROGRESS);

    when(inquiryRepository.findById(any())).thenReturn(Optional.of(createInquiry()));
    when(inquiryRepository.save(argThat(inquiry -> {
      assertThat(inquiry.getStatus()).isEqualTo(updateDto.getStatus());
      return true;
    }))).thenReturn(createInquiry());

    var result = inquiryService.update(UUID.randomUUID(), updateDto);

    assertThat(result.getStatus()).isEqualTo(updateDto.getStatus());
  }

  @Test
  void shouldFindById() {
    var entity = createInquiry();

    when(inquiryRepository.findById(any())).thenReturn(Optional.of(entity));
    var result = inquiryService.findByIdRequest(UUID.randomUUID());

    assertThat(result.getComment()).isEqualTo(entity.getComment());
    assertThat(result.getNote()).isEqualTo(entity.getNote());
    assertThat(result.getSource()).isEqualTo(entity.getSource());
  }

  @Test
  void findByIdThrowExpIfEmpty() {
    when(inquiryRepository.findById(any())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> inquiryService.findByIdRequest(UUID.randomUUID()))
     .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void findAll() {
    var filterDto = new InquiryFilterDto();

    when(inquiryRepository.findAll(any(InquirySpecification.class), any(PageRequest.class)))
        .thenReturn(new PageImpl(Collections.emptyList(), PageRequest.of(1, 1), 5));
    inquiryService.findAll(filterDto);

    verify(inquiryRepository).findAll(any(InquirySpecification.class), any(PageRequest.class));
  }

  @Test
  void shouldChangeStatus() {
    var inquiry = createInquiry();
    when(inquiryRepository.findById(any())).thenReturn(Optional.of(inquiry));
    inquiryService.changeStatus(UUID.randomUUID(), InquiryStatus.PAYMENT);

    assertThat(inquiry.getStatus()).isEqualTo(InquiryStatus.PAYMENT);
  }

  private static InquiryCreateRequestDto createInquiryDto() {
    var dto = new InquiryCreateRequestDto();
    dto.setSource(InquirySource.TELEGRAM);
    dto.setComment("comment");
    dto.setNote("note");
    dto.setGroupRefId(UUID.randomUUID());

    return dto;
  }

  private static Inquiry createInquiry() {
    var entity = new Inquiry();
    entity.setSource(InquirySource.TELEGRAM);
    entity.setComment("comment");
    entity.setNote("note");

    return entity;
  }
}
