package com.iprody.orders.service.impl;

import com.iprody.orders.dto.CreateInquiryDto;
import com.iprody.orders.dto.InquiryFilterDto;
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
  public Inquiry create(CreateInquiryDto dto) {
    var entity = inquiryMapper.mapToEntity(dto);
    entity = inquiryRepository.save(entity);

    return entity;
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
  public Inquiry findById(UUID id) {
    return inquiryRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Inquiry with id " + id + "not found"));
  }

  @Override
  public Page<Inquiry> findAll(InquiryFilterDto dto) {
    var specification = new InquirySpecification(dto);
    var pageRequest = PageRequest.of(dto.getPageNumber(), dto.getPageSize(), buildSort(dto.getSort()));

    return inquiryRepository.findAll(specification, pageRequest);
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
