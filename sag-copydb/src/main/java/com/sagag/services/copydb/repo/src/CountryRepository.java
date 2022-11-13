package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.Country;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Integer> {
}
