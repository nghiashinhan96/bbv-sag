package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.TargetTourTime;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TargetTourTimeRepository extends JpaRepository<TargetTourTime, Integer> {

  Optional<TargetTourTime> findByCustomerNumberAndBranchIdAndTourName(String custNr,
      String branchId, String tourName);

}
