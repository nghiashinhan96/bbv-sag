package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.BranchOpeningTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BranchOpeningTimeRepository
    extends JpaRepository<BranchOpeningTime, Integer>, JpaSpecificationExecutor<BranchOpeningTime> {

  void deleteAllByBranchId(Integer branchId);

  List<BranchOpeningTime> findByBranchId(Integer branchId);

  @Query(value = "select bot.* from BRANCH_OPENING_TIME bot "
      + "inner join BRANCH b on b.ID = bot.BRANCH_ID where b.BRANCH_NUMBER = :branchNr",
      nativeQuery = true)
  List<BranchOpeningTime> findByBranchNr(@Param("branchNr") Integer branchNr);

  @Query(value = "select bot.* from BRANCH_OPENING_TIME bot "
      + "inner join BRANCH b on b.ID = bot.BRANCH_ID where b.BRANCH_NUMBER IN (:branchNrs)",
      nativeQuery = true)
  List<BranchOpeningTime> findByBranchNrList(@Param("branchNrs") List<Integer> branchNrs);
}
