package com.sagag.services.service.api;

import com.sagag.eshop.repo.entity.VExternalVendor;
import com.sagag.eshop.service.dto.CsvExternalVendorDto;
import com.sagag.eshop.service.exception.ExternalVendorValidationException;
import com.sagag.services.domain.eshop.criteria.ExternalVendorSearchCriteria;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;
import com.sagag.services.domain.eshop.dto.externalvendor.SupportExternalVendorDto;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Interface to define services for External Vendor.
 */
public interface ExternalVendorService {

  /**
   * Imports the CSV file for external vendor.
   * 
   * @param externalVendors the list CsvExternalVendorDto
   * @throws ExternalVendorValidationException
   */
  void importExternalVendor(List<CsvExternalVendorDto> externalVendors)
      throws ExternalVendorValidationException;

  /**
   * Returns the external vendor by criteria.
   * 
   * @param searchCriteria Search Criteria for External Vendor.
   * @return Page of {@link VExternalVendor}.
   */
  Page<VExternalVendor> searchExternalVendor(ExternalVendorSearchCriteria searchCriteria);

  /**
   * Returns initialize the data for external vendor search.
   * 
   * @return SupportExternalVendorDto
   */
  SupportExternalVendorDto getMasterDataExternalVendor();

  /**
   * Creates new external vendor.
   * 
   * @param request ExternalVendorDto
   * @param userId
   * @throws ExternalVendorValidationException
   */

  void createExternalVendor(ExternalVendorDto request, Long userId)
      throws ExternalVendorValidationException;

  /**
   * Updates external vendor.
   * 
   * @param request ExternalVendorDto
   * @param userId
   * @throws ExternalVendorValidationException
   */
  void updateExternalVendor(ExternalVendorDto request, Long userId)
      throws ExternalVendorValidationException;

  /**
   * Deletes external vendor.
   * 
   * @param id internal id in DB
   */
  void delete(Integer id);

}
