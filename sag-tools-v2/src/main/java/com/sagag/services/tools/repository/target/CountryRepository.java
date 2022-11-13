package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.Country;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interfacing for {@link Country}.
 *
 */
public interface CountryRepository extends JpaRepository<Country, Integer> {

  Optional<Country> findByCode(String code);

  Optional<Country> findByShortCode(String shortCode);

}
