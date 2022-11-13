package com.sagag.services.copydb.repo.dest;

import com.sagag.services.copydb.domain.dest.DestPaymentMethod;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DestPaymentMethodRepository extends JpaRepository<DestPaymentMethod, Integer> {
}
