package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.Country;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interfacing for {@link Country}.
 *
 */
public interface CountryRepository extends JpaRepository<Country, Integer> {

  Optional<Country> findByCode(String code);

  List<Country> findByShortCode(String shortCode);

  @Query(value = "select c from Country c where c.shortCode IS NOT NULL")
  List<Country> findAllWithShortCode();

  @Query(value = "select c.id from Country c where c.shortCode = :shortCode")
  List<Integer> findIdsByShortCode(@Param("shortCode") String shortCode);

  Optional<Country> findByShortName(String shortName);

  @Query("select c from Country c where c.shortName IN :countryNames")
  List<Country> findAllByCountryNames(@Param("countryNames") List<String> countryNames);

  List<Country> findByCodeIn(List<String> codes);

  @Query("select c.shortCode from Country c, SupportedAffiliate sa "
      + "where sa.shortName = :affiliate and c.id = sa.countryId")
  Optional<String> findShortCodeByAffiliate(@Param("affiliate") String affiliate);
}
