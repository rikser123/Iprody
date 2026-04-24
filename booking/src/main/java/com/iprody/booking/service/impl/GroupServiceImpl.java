package com.iprody.booking.service.impl;

import com.iprody.booking.dto.CreateGroupDto;
import com.iprody.booking.dto.GroupFilterDto;
import com.iprody.booking.exception.InquiryGroupException;
import com.iprody.booking.mapper.GroupMapper;
import com.iprody.booking.repository.GroupRepository;
import com.iprody.booking.repository.entity.Group;
import com.iprody.booking.repository.entity.GroupInquiry;
import com.iprody.booking.repository.specification.GroupSpecification;
import com.iprody.booking.service.GroupService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupServiceImpl implements GroupService {
  private final GroupRepository groupRepository;
  private final GroupMapper groupMapper;

  @Override
  public Group createGroup(CreateGroupDto dto) {
    var group = groupMapper.mapToEntity(dto);
    group = groupRepository.save(group);

    return group;
  }

  @Override
  @Transactional
  public Group increaseCount(UUID id, UUID inquiryId) {
    var currentGroup = findById(id);

    if (isInquiryInGroup(currentGroup, inquiryId)) {
      throw new InquiryGroupException(String.format("Inquiry %s is already in group %s", inquiryId, id));
    }
    var newCount = currentGroup.getCurrentCount() + 1;

    if (newCount > currentGroup.getLimit()) {
      throw new IllegalStateException("Group is full");
    }

    var groupInquiry = new GroupInquiry();
    groupInquiry.setGroup(currentGroup);
    groupInquiry.setInquiryId(inquiryId);
    currentGroup.getInquiries().add(groupInquiry);

    return changeGroupCount(currentGroup, newCount);
  }

  @Override
  @Transactional
  public Group decreaseCount(UUID id, UUID inquiryId) {
    var currentGroup = findById(id);
    var newCount = currentGroup.getCurrentCount() - 1;

    if (!isInquiryInGroup(currentGroup, inquiryId)) {
      throw new InquiryGroupException(String.format("Inquiry %s is already in group %s", inquiryId, id));
    }

    if (newCount < 0) {
      throw new IllegalStateException("Group count is negative");
    }

    currentGroup
      .getInquiries()
      .removeIf(gr ->  gr.getInquiryId().equals(inquiryId));

    return changeGroupCount(currentGroup, newCount);
  }

  @Override
  public Page<Group> findAll(GroupFilterDto dto) {
    var specification = new GroupSpecification(dto);
    var pageRequest = PageRequest.of(dto.getPage(), dto.getPageSize());

    return groupRepository.findAll(specification, pageRequest);
  }

  private Group findById(UUID id) {
    return groupRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Group with id" + id + " not found"));
  }

  private Group changeGroupCount(Group currentGroup, Integer newCount) {
    currentGroup.setCurrentCount(newCount);
    return groupRepository.save(currentGroup);
  }

  private boolean isInquiryInGroup(Group group,  UUID inquiryId) {
    return group.getInquiries()
      .stream()
      .map(GroupInquiry::getInquiryId)
      .anyMatch(id -> id.equals(inquiryId));
  }
}
