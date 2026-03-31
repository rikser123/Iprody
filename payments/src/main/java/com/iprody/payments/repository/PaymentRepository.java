package com.iprody.payments.repository;

import com.iprody.payments.repository.entity.Payment;
import com.iprody.payments.repository.specification.PaymentSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID>,
    JpaSpecificationExecutor<PaymentSpecification> {
}
