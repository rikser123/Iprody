package com.iprody.payments.repository.specification;

import com.iprody.payments.dto.PaymentFilterDto;
import com.iprody.payments.repository.entity.Payment;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Objects;

@RequiredArgsConstructor
public class PaymentSpecification implements Specification<Payment> {
  private final PaymentFilterDto filterDto;

  @Override
  public Predicate toPredicate(Root<Payment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    var predicates = new ArrayList<Predicate>();

    if (!Objects.isNull(filterDto.getId())) {
      predicates.add(criteriaBuilder.equal(root.get("id"), filterDto.getId()));
    }

    if (!Objects.isNull(filterDto.getInquiryRefId())) {
      predicates.add(criteriaBuilder.equal(root.get("inquiryRefId"), filterDto.getInquiryRefId()));
    }

    if (!Objects.isNull(filterDto.getStatus())) {
      predicates.add(criteriaBuilder.equal(root.get("status"), filterDto.getStatus()));
    }

    if (!Objects.isNull(filterDto.getDate())) {
      var start = filterDto.getDate().atStartOfDay().toInstant(ZoneOffset.UTC);
      var end = filterDto.getDate().plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
      predicates.add(criteriaBuilder.between(root.get("createdAt"), start, end));
    } else if (!Objects.isNull(filterDto.getToDate()) && !Objects.isNull(filterDto.getFromDate())) {
      predicates.add(criteriaBuilder.between(root.get("createdAt"), filterDto.getFromDate(), filterDto.getToDate()));
    } else if (!Objects.isNull(filterDto.getToDate())) {
      predicates.add(criteriaBuilder.lessThan(root.get("createdAt"), filterDto.getToDate()));
    } else if (!Objects.isNull(filterDto.getFromDate())) {
      predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), filterDto.getFromDate()));
    }

      return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
  }
}
