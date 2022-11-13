package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

  @Query("select c.id from Currency c where c.isoCode = :iso")
  Integer findIdByIso(@Param("iso") String iso);
}
