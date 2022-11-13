package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.WssTour;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WssTourRepository
    extends JpaRepository<WssTour, Integer>, JpaSpecificationExecutor<WssTour> {

  @Query(value = "select t from WssTour t "
      + "where t.id = :id and t.orgId = :orgId")
  Optional<WssTour> findByIdAndOrgId(@Param("id") final int id, @Param("orgId") final int orgId);

  @Query(value = "select case when count(t) > 0 then 'true' else 'false' end "
      + "from WssTour t where t.name = :name and t.orgId = :orgId ")
  boolean checkExistingTourByNameAndOrgId(@Param("name") final String name,
      @Param("orgId") final Integer orgId);

  @Query(value = "select case when count(t) > 0 then 'true' else 'false' end "
      + "from WssTour t where t.id != :id and t.name = :name and t.orgId = :orgId ")
  boolean checkExistingTourByIdAndNameAndOrgId(@Param("id") final Integer id,
      @Param("name") final String name, @Param("orgId") final Integer orgId);
}
