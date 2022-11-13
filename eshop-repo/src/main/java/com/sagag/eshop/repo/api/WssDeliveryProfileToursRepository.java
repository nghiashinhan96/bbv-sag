package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.WssDeliveryProfileTours;
import com.sagag.eshop.repo.utils.EshopRepoConstans;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WssDeliveryProfileToursRepository
    extends JpaRepository<WssDeliveryProfileTours, Integer>,
    JpaSpecificationExecutor<WssDeliveryProfileTours> {

  @Query(value = "select case when count(p) > 0 then 'true' else 'false' end "
      + "from WssDeliveryProfileTours p where p.wssTour.id = :wssTourId and p.wssDeliveryProfile.id = :wssDeliveryProfileId")
  boolean checkExistingDeliveryProfileTour(@Param("wssTourId") final Integer wssTourId,
      @Param("wssDeliveryProfileId") final Integer wssDeliveryProfileId);

  @Query(value = "select case when count(p) > 0 then 'true' else 'false' end "
      + "from WssDeliveryProfileTours p where p.wssDeliveryProfile.id = :wssDeliveryProfileId")
  boolean checkExistingDeliveryProfileTour(
      @Param("wssDeliveryProfileId") final Integer wssDeliveryProfileId);

  @Query(value = "select p "
      + "from WssDeliveryProfileTours p where p.wssDeliveryProfile.id = :wssDeliveryProfileId")
  List<WssDeliveryProfileTours> findAllByDeliveryProfileId(
      @Param("wssDeliveryProfileId") final Integer wssDeliveryProfileId);

  @Query(value = "select case when count(t.ID) > 0 then 'true' else 'false' end "
      + "from WSS_DELIVERY_PROFILE_TOURS t join WSS_DELIVERY_PROFILE p on t.WSS_DELIVERY_PROFILE_ID = p.ID "
      + "where p.ID = :profileId and p.ORG_ID = :orgId and t.SUPPLIER_TOUR_DAY = :supplierTourDay and CONVERT(VARCHAR(5), t.SUPPLIER_DEPARTURE_TIME, "
      + EshopRepoConstans.SQL_SERVER_TIME_FORMAT_CODE_HH_MM_SS + ") = :supplierTourTime",
      nativeQuery = true)
  public boolean checkExistDeliveryProfileTourByProfileIdOrgIdAndSupplierDayAndSupplierTour(
      @Param("profileId") int profileId, @Param("orgId") int orgId,
      @Param("supplierTourDay") String supplierTourDay,
      @Param("supplierTourTime") String supplierTourTime);

  @Query(value = "select case when count(t.ID) > 0 then 'true' else 'false' end "
      + "from WSS_DELIVERY_PROFILE_TOURS t join WSS_DELIVERY_PROFILE p on t.WSS_DELIVERY_PROFILE_ID = p.ID "
      + "where p.ID = :profileId and  t.ID != :id and p.ORG_ID = :orgId and t.SUPPLIER_TOUR_DAY = :supplierTourDay and CONVERT(VARCHAR(5), t.SUPPLIER_DEPARTURE_TIME, "
      + EshopRepoConstans.SQL_SERVER_TIME_FORMAT_CODE_HH_MM_SS + ") = :supplierTourTime",
      nativeQuery = true)
  public boolean checkExistDeliveryProfileTourByByProfileIdAndIdAndOrgIdAndSupplierDayAndSupplierTour(
      @Param("profileId") int profileId, @Param("id") int id, @Param("orgId") int orgId,
      @Param("supplierTourDay") String supplierTourDay,
      @Param("supplierTourTime") String supplierTourTime);
}
