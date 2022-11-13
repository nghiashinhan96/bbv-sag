package com.sagag.eshop.service.api;

import com.sagag.eshop.service.dto.HaynesProLicenseSettingDto;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.LicenseExportException;
import com.sagag.eshop.service.exception.LicenseNotFoundException;
import com.sagag.services.common.enums.LicenseType;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.domain.eshop.backoffice.dto.BackOfficeLicenseDto;
import com.sagag.services.domain.eshop.criteria.LicenseSearchCriteria;
import com.sagag.services.domain.eshop.dto.CustomerLicenseDto;
import com.sagag.services.domain.eshop.dto.LicenseDto;
import com.sagag.services.domain.eshop.dto.LicenseSettingsDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interface defines the License services.
 */
public interface LicenseService {

  /**
   * Returns the license settings by package id.
   *
   * @param packId the package id
   * @return the {@link LicenseSettingsDto}, nullable
   */
  Optional<LicenseSettingsDto> getLicenseSettingsByPackId(Long packId);

  /**
   * Returns the license settings pack article id.
   *
   * @param packArticleId the article id
   * @return {@link LicenseSettingsDto}, nullable
   */
  Optional<LicenseSettingsDto> getLicenseSettingsByArticleId(Long packArticleId);

  /**
   * Returns total number of VIN calls left for specific customer.
   *
   * @param customerNr the customer number
   * @return total VIN calls left
   */
  int getVinCallsLeft(final Long customerNr);

  /**
   * Creates the VIN license.
   *
   * @param licenseDto the license data
   */
  void createVinLicense(LicenseDto licenseDto);

  /**
   * Returns the list of license packages.
   *
   *@return the list of {@link LicenseSettingsDto}
   */
  List<LicenseSettingsDto> getAllLicensePackage();

  /**
   * Increases the quantity used for license.
   *
   * @param userId the user who use the license
   * @param custNr the customer number that the user belongs
   * @param licenseType the license type
   */
  void increaseQuantityUsed(Long userId, String custNr, LicenseType licenseType);

  /**
   * Redeems the VIN license.
   *
   * @param custNr the customer number
   * @param searchCount the search count
   */
  void redeemVinLicense(String custNr, int searchCount);

  /**
   * Returns the HaynesPro license.
   */
  Optional<HaynesProLicenseSettingDto> getHaynesProLicense(String custNr);

  /**
   * Returns all available VIN licenses.
   *
   * @return a list of VIN license settings
   */
  List<LicenseSettingsDto> getAllVinLicenses();

  /**
   * Returns all license of customer.
   *
   * @param customerNr the customer number
   * @param pageable the pagination
   * @return Page of {@link BackOfficeLicenseDto}
   */
  Page<BackOfficeLicenseDto> getAllLicensesOfCustomer(String customerNr, Pageable pageable);

  /**
   * delete specific license for the customer
   *
   * @param id
   */
  void deleteLicenseForCustomer(Long id);

  /**
   * This service to update a licence for the customer
   *
   * @param customerLicenseDto
   * @param user
   */
  void updateLicenseForCustomer(Long id, CustomerLicenseDto customerLicenseDto,
      UserInfo user) throws LicenseNotFoundException;

  /**
   * This service assign license to Customer.
   * @param customerLicenseDto
   * @param user
   * @throws LicenseNotFoundException
   */
  void assignLicenseToCustomer(CustomerLicenseDto customerLicenseDto, UserInfo user)
      throws LicenseNotFoundException;

  /**
   * Search licenses by criteria.
   * @param searchCriteria the search criteria
   * @return Page of back-office licenses
   */
  Page<BackOfficeLicenseDto> search(LicenseSearchCriteria searchCriteria);

  /**
   * Export licenses to CSV file by criteria
   * @param criteria
   * @return
   * @throws ServiceException
   */
  ExportStreamedResult exportToCsvByCriteria(LicenseSearchCriteria criteria) throws ServiceException;

  /**
   * Export licenses to Excel file by criteria
   * @param criteria
   * @return
   * @throws LicenseExportException
   */
  ExportStreamedResult exportToExcelByCriteria(LicenseSearchCriteria criteria) throws LicenseExportException;
}
