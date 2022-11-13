package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.WssDeliveryProfile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WssDeliveryProfileRepository extends JpaRepository<WssDeliveryProfile, Integer>,
    JpaSpecificationExecutor<WssDeliveryProfile> {

  public Page<WssDeliveryProfile> findByOrgId(int orgId, Pageable pageable);

  Optional<WssDeliveryProfile> findByIdAndOrgId(int id, int orgId);

  @Query(value = "select case when count(d) > 0 then 'true' else 'false' end "
      + "from WssDeliveryProfile d where d.name = :name and d.orgId = :orgId")
  public boolean checkExistDeliveryProfileNameByOrgId(@Param("name") String name,
      @Param("orgId") int orgId);

  @Query(value = "select case when count(d) > 0 then 'true' else 'false' end "
      + "from WssDeliveryProfile d where d.id != :id and d.name = :name and d.orgId = :orgId")
  public boolean checkExistDeliveryProfileNameByIdAndOrgId(@Param("id") int id,
      @Param("name") String name, @Param("orgId") int orgId);
}
