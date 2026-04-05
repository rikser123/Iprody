package com.iprody.orders.service.impl;

import com.iprody.orders.dto.InquiryCreateRequestDto;
import com.iprody.orders.dto.InquiryFilterDto;
import com.iprody.orders.dto.InquiryResponseDto;
import com.iprody.orders.dto.UpdateInquiryDto;
import com.iprody.orders.mapper.InquiryMapper;
import com.iprody.orders.repository.InquiryRepository;
import com.iprody.orders.repository.entity.Inquiry;
import com.iprody.orders.repository.specification.InquirySpecification;
import com.iprody.orders.service.InquiryService;
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
public class InquiryServiceImpl implements InquiryService {
  private final InquiryRepository inquiryRepository;
  private final InquiryMapper inquiryMapper;

  @Override
  public InquiryResponseDto create(InquiryCreateRequestDto dto) {
    var entity = inquiryMapper.mapToEntity(dto);
    entity = inquiryRepository.save(entity);

    return inquiryMapper.mapToDto(entity);
  }

  @Override
  public Inquiry update(UUID id, UpdateInquiryDto dto) {
    var currentInquiry = findById(id);
    currentInquiry.setManagerRefId(dto.getManagerRefId());
    currentInquiry.setStatus(dto.getStatus());
    inquiryRepository.save(currentInquiry);

    return currentInquiry;
  }

  @Override
  public InquiryResponseDto findByIdRequest(UUID id) {
    var existingInquiry = findById(id);

    return inquiryMapper.mapToDto(existingInquiry);
  }

  @Override
  public Page<InquiryResponseDto> findAll(InquiryFilterDto dto) {
    var specification = new InquirySpecification(dto);
    var pageRequest = PageRequest.of(dto.getPageNumber(), dto.getPageSize(), buildSort(dto.getSort()));

    return inquiryRepository.findAll(specification, pageRequest).map(inquiryMapper::mapToDto);
  }

  private Inquiry findById(UUID id) {
    return inquiryRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Inquiry with id " + id + "not found"));
  }

  private Sort buildSort(Map<String, String> sort) {
    return Optional.ofNullable(sort)
    .orElse(Collections.emptyMap())
    .entrySet()
    .stream()
    .map(entry -> {
      var field = entry.getKey();
      var direction = entry.getValue();

      return Sort.by(direction, field);

    }).reduce(Sort::and)
   .orElse(Sort.unsorted());
  }
}
