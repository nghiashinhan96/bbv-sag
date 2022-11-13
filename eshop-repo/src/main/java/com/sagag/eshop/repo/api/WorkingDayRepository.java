package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.WorkingDay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Working day JPA Repository interface.
 */
public interface WorkingDayRepository
    extends JpaRepository<WorkingDay, Integer>, JpaSpecificationExecutor<WorkingDay> {

  Optional<WorkingDay> findOneByCode(String code);

  @Query("select w from WorkingDay w where w.code IN :codes")
  List<WorkingDay> findAllByCodes(@Param("codes") List<String> codes);
}
