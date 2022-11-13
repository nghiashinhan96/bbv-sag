package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.WssBranch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WssBranchRepository
    extends JpaRepository<WssBranch, Integer>, JpaSpecificationExecutor<WssBranch> {

  @Query(value = "select b.branchNr from WssBranch b")
  List<Integer> findAllBranchNrs();

  @Query(value = "select b from WssBranch b where b.orgId = :orgId and b.branchNr = :branchNr")
  Optional<WssBranch> findOneByBranchNrAndOrgId(@Param("branchNr") final Integer branchNr,
      @Param("orgId") final Integer orgId);

  @Query(value = "select case when count(b) > 0 then 'true' else 'false' end "
      + "from WssBranch b where b.branchNr = :branchNr and b.orgId = :orgId")
  boolean checkExistingBranchByBranchNrAndOrgId(@Param("branchNr") final Integer branchNr,
      @Param("orgId") final Integer orgId);

  @Query(value = "select case when count(b) > 0 then 'true' else 'false' end "
      + "from WssBranch b where b.branchCode = :branchCode and b.orgId = :orgId")
  boolean checkExistingBranchByBranchCodeAndOrgId(@Param("branchCode") final String branchCode,
      @Param("orgId") final Integer orgId);

  @Query(value = "select b.id from WssBranch b where b.branchNr = :branchNr and b.orgId = :orgId")
  Optional<Integer> findIdByBranchNrAndOrgId(@Param("branchNr") final Integer branchNr, @Param("orgId") final Integer orgId);

  @Query(value = "select b from WssBranch b where b.orgId = :orgId")
  List<WssBranch> findAllByOrgId(@Param("orgId") final Integer orgId);

  @Query(value = "select b from WssBranch b where b.id = :id and b.orgId = :orgId")
  Optional<WssBranch> findByIdAndOrgId(@Param("id") final Integer id,
      @Param("orgId") final Integer orgId);
}
