package com.iprody.orders.repository.specification;

import com.iprody.orders.dto.InquiryFilterDto;
import com.iprody.orders.repository.entity.Inquiry;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Objects;

@RequiredArgsConstructor
public class InquirySpecification implements Specification<Inquiry> {
  private final InquiryFilterDto filterDto;

  @Override
  public Predicate toPredicate(Root<Inquiry> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    var predicates = new ArrayList<Predicate>();

    var status = filterDto.getStatus();
    if (!Objects.isNull(status)) {
      predicates.add(criteriaBuilder.equal(root.get("status"), status));
    }

    var customerId = filterDto.getCustomerId();
    if (!Objects.isNull(customerId)) {
      predicates.add(criteriaBuilder.equal(root.get("customerRefId"), customerId));
    }

    var managerId = filterDto.getManagerId();
    if (!Objects.isNull(managerId)) {
      predicates.add(criteriaBuilder.equal(root.get("managerRefId"), managerId));
    }

    return criteriaBuilder.and(predicates.toArray(Predicate[]::new));

  }
}
