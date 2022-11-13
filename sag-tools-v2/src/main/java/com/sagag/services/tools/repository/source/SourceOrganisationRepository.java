package com.sagag.services.tools.repository.source;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceOrganisation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
@OracleProfile
public interface SourceOrganisationRepository extends JpaRepository<SourceOrganisation, Long> {

  @Query("select so from SourceOrganisation so where type='FINALCUSTOMER'")
  Page<SourceOrganisation> findFinalCustomers(Pageable pageable);

  @Query("select count(so) from SourceOrganisation so where type='FINALCUSTOMER'")
  long countFinalCustomers();

  @Query(value = "select o.NAME " +
    "from SHOP.ORGANISATION o " +
    "inner join SHOP.ORGANISATIONLINK ol on ol.CLIENT_ID = o.ID " +
    "inner join SHOP.LOGIN_ROLEASSIGNMENT ra on ra.ORGANISATION_ID = ol.CLIENT_ID " +
    "inner join SHOP.PERSON p on p.ID = ra.PERSON_ID " +
    "where o.TYPE='FINALCUSTOMER' and p.ID = :personId", nativeQuery = true)
  Optional<String> findFinalCustomerNameByPersonId(@Param("personId") Long personId);

  @Query(value = "select o.ID " +
    "from SHOP.ORGANISATION o " +
    "inner join SHOP.ORGANISATIONLINK ol on ol.VENDOR_ID = o.ID " +
    "inner join SHOP.LOGIN_ROLEASSIGNMENT ra on ra.ORGANISATION_ID = ol.CLIENT_ID " +
    "inner join SHOP.PERSON p on p.ID = ra.PERSON_ID " +
    "where p.ID = :personId", nativeQuery = true)
  Optional<BigDecimal> findVendorIdByPersonId(@Param("personId") Long personId);

  Optional<SourceOrganisation> findOneByErpNumber(@Param("erpNr") Long erpNr);

  @Query("select o.id from SourceOrganisation o")
  List<Integer> findOrganisationIdList();
}
