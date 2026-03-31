package com.iprody.booking.repository.specification;

import com.iprody.booking.dto.GroupFilterDto;
import com.iprody.booking.repository.entity.Group;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Objects;

@RequiredArgsConstructor
public class GroupSpecification implements Specification<Group> {
  private final GroupFilterDto filterDto;

  @Override
  public Predicate toPredicate(Root<Group> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    var predicates = new ArrayList<Predicate>();

    var groupRefId = filterDto.getGroupRefId();

    if (!Objects.isNull(groupRefId)) {
      predicates.add(criteriaBuilder.equal(root.get("groupRefId"), groupRefId));
    }

    var availablePlaces = filterDto.getAvailablePlaces();

    if (!Objects.isNull(availablePlaces)) {
      Expression<Integer> availableCount = criteriaBuilder.diff(root.get("limit"), root.get("currentCount"));
      predicates.add(criteriaBuilder.lessThanOrEqualTo(availableCount, availablePlaces));
    }

    return criteriaBuilder.and(predicates.toArray((Predicate[]::new)));
  }
}

