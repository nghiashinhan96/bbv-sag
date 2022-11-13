package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.Branch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Integer> {

  Optional<Branch> findOneByBranchNr(final Integer branchNr);

  @Query(value = "select case when count(b) > 0 then 'true' else 'false' end "
      + "from Branch b where b.branchNr = :branchNr")
  boolean checkExistingByBranchNr(@Param("branchNr") final Integer branchNr);

}
