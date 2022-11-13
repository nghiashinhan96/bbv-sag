package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.WssMarginsBrand;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WssMarginsBrandRepository
    extends JpaRepository<WssMarginsBrand, Integer>, JpaSpecificationExecutor<WssMarginsBrand> {


  /**
   * Return WssMarginsBrand from id
   *
   * @param id
   * @return WssMarginsBrand
   */
  Optional<WssMarginsBrand> findOneById(int id);

  Optional<WssMarginsBrand> findOneByOrgIdAndBrandName(int orgId, String brandName);

  @Query("select w from WssMarginsBrand w where w.orgId = :orgId")
  List<WssMarginsBrand> findAllByOrgId(@Param("orgId") int orgId);

  @Query("select w from WssMarginsBrand w where w.orgId = :orgId and w.isDefault = true")
  Optional<WssMarginsBrand> findDefaultSettingByOrgId(@Param("orgId") int orgId);

  @Query("select w from WssMarginsBrand w "
      + "where w.orgId = :orgId "
      + "and (w.brandId = :brandId or w.isDefault is true) "
      + "order by w.brandId desc")
  List<WssMarginsBrand> findFirstOrDefaultByOrgIdAndBrandId(@Param("orgId") int orgId,
      @Param("brandId") int brandId, Pageable pageable);

  @Query(value = "select case when count(w) > 0 then 'true' else 'false' end "
      + "from WssMarginsBrand w where w.orgId = :orgId and w.isDefault = true and w.margin1 is not null ")
  boolean checkDefaultMarginBrandExist(@Param("orgId") int orgId);

  Optional<WssMarginsBrand> findByBrandIdAndOrgId(@Param("brandId") int brandId, @Param("orgId") int orgId);

}
