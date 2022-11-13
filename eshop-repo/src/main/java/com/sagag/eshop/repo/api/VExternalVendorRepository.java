package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.VExternalVendor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VExternalVendorRepository
    extends JpaRepository<VExternalVendor, Integer>, JpaSpecificationExecutor<VExternalVendor> {

  /**
   * Find distinct availability type for external vendor.
   * 
   * @return the list of availability type.
   */
  @Query("SELECT DISTINCT ex.availabilityTypeId FROM VExternalVendor ex")
  List<String> findDistinctAvailabilityType();

}
