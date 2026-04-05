package com.iprody.clients.repository.specification;

import com.iprody.clients.dto.CustomerFilterDto;
import com.iprody.clients.repository.entity.Customer;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

@RequiredArgsConstructor
public class CustomerSpecification implements Specification<Customer> {
  private final CustomerFilterDto filterDto;

  @Override
  public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    var predicates = new ArrayList<Predicate>();

    var fullName = filterDto.getFullName();

    if (StringUtils.isNotEmpty(fullName)) {
      predicates.add(criteriaBuilder.like(
          criteriaBuilder.lower(root.get("fullName")),
          "%" + fullName.toLowerCase() + "%"
      ));;
    }

    root.fetch("contactDetails");

    return criteriaBuilder.and(predicates.toArray((Predicate[]::new)));
  }
}
