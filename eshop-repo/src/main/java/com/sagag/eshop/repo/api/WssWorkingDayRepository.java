package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.WssWorkingDay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * WSS Working day JPA Repository interface.
 */
public interface WssWorkingDayRepository
    extends JpaRepository<WssWorkingDay, Integer>, JpaSpecificationExecutor<WssWorkingDay> {

  Optional<WssWorkingDay> findOneByCode(String code);

  @Query("select w from WssWorkingDay w where w.code IN :codes")
  List<WssWorkingDay> findAllByCodes(@Param("codes") List<String> codes);
}
