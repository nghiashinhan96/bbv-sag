package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.VBasketHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * JPA repository for basket history view.
 */
public interface VBasketHistoryRepository extends JpaRepository<VBasketHistory, Long>,
  JpaSpecificationExecutor<VBasketHistory> {

  @Query(value = "select v from VBasketHistory v where v.basketHistoryId = :basketId")
  Optional<VBasketHistory> findByBasketId(@Param("basketId") Long basketId);


  @Query("select count(v) from VBasketHistory v where v.createdUserId = :createdUserId and v.active = 1")
  Long countByCreatedUserId(@Param("createdUserId") Long createdUserId);

  @Query("select count(v) from VBasketHistory v where v.orgCode = :orgCode and v.active = 1")
  Long countByOrganisationOrgCode(@Param("orgCode") String orgCode);

  @Query("select count(v) from VBasketHistory v where v.salesUserId = :salesUserId and v.active = 1")
  Long countBySalesUserId(@Param("salesUserId") Long salesUserId);

}
