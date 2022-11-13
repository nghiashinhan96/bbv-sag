package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.License;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * License JPA repository APIs.
 */
public interface LicenseRepository extends JpaRepository<License, Long>, JpaSpecificationExecutor<License> {

  List<License> findAllByCustomerNrAndTypeOfLicense(long customerNr, String typeOfLicense);

  @Query("SELECT l FROM License l " + "WHERE l.customerNr = :customerNr "
      + "AND l.typeOfLicense = :licenseType " + "AND l.quantity != l.quantityUsed ")
  Page<License> findFirstAvailableLicense(@Param("customerNr") long customerNr,
      @Param("licenseType") String licenseType, final Pageable pageable);

  /**
   * Returns the available HaynesPro licenses for customer number.
   *
   * @param customerNr selected customer number
   *
   * @return the page of available HP license
   */
  @Query("select l from License l where l.customerNr = :customerNr and l.typeOfLicense = 'HAYNESPRO' "
      + "and l.beginDate < getDate() and l.endDate > getDate()")
  Page<License> findAvailableHaynesProLicense(@Param("customerNr") long customerNr, Pageable page);

  /**
   * Returns available license calls for the customer. The calls can be the HaynesPro or VIN
   * calls...
   *
   * @param customerNr the customer number
   * @param type the license type
   * @return the total available license calls.
   */
  @Query("SELECT sum(l.quantity - l.quantityUsed) FROM License l WHERE l.customerNr = :customerNr AND l.typeOfLicense = :type")
  Integer findAvailableLicenseCalls(@Param("customerNr") long customerNr, @Param("type") String type);

  Page<License> findAllByCustomerNr(@Param("customerNr") long customerNr, final Pageable page);

  /**
   * get licence by id
   * @param id
   * @return
   */
  @Override
  Optional<License> findById(Long id);

}
