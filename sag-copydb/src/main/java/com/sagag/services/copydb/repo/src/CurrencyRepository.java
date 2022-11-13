package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.Currency;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
}
