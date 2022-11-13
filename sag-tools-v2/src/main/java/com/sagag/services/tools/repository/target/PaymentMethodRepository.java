package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.PaymentMethod;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link PaymentMethod}.
 */
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {

  Optional<PaymentMethod> findOneByDescCode(String descCode);

  Optional<PaymentMethod> findOneById(int id);

  @Query(value = "SELECT p FROM PaymentMethod p ORDER BY p.orderDisplay ASC")
  List<PaymentMethod> findAllOrderByOrderDisplayAsc();
}
