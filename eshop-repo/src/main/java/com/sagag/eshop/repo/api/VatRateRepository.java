package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.VatRate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interfacing for {@link VatRate}.
 *
 */
public interface VatRateRepository extends JpaRepository<VatRate, Integer> {
}
