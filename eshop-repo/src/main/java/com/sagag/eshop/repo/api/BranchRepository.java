package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.Branch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BranchRepository
    extends JpaRepository<Branch, Integer>, JpaSpecificationExecutor<Branch> {

  Optional<Branch> findOneByBranchNr(@Param("branchNr") final Integer branchNr);

  @Query(value = "select case when count(b) > 0 then 'true' else 'false' end "
      + "from Branch b where b.branchNr = :branchNr")
  boolean checkExistingBranchByBranchNr(@Param("branchNr") final Integer branchNr);

  @Query(value = "select case when count(b) > 0 then 'true' else 'false' end "
      + "from Branch b where b.branchCode = :branchCode")
  boolean checkExistingBranchByBranchCode(@Param("branchCode") final String branchCode);

  @Query(value = "select b.id from Branch b where b.branchNr = :branchNr")
  Optional<Integer> findIdByBranchNr(@Param("branchNr") final Integer branchNr);

  @Query(value = "select b from Branch b where b.countryId IN :countryId")
  List<Branch> findByCountries(@Param("countryId") final List<Integer> countryId);

  @Query(value = "select b.branchNr from Branch b")
  List<Integer> findAllBranchNrs();
}
