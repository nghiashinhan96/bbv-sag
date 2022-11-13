package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.PaymentMethod;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
}
