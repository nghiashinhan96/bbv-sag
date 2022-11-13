package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.criteria.finalcustomer.FinalCustomerSearchCriteria;
import com.sagag.eshop.service.dto.finalcustomer.FinalCustomerDto;
import com.sagag.eshop.service.dto.finalcustomer.FinalCustomerSettingDto;
import com.sagag.eshop.service.dto.finalcustomer.NewFinalCustomerDto;
import com.sagag.eshop.service.dto.finalcustomer.SavingFinalCustomerDto;
import com.sagag.eshop.service.dto.finalcustomer.TemplateNewFinalCustomerProfileDto;
import com.sagag.eshop.service.dto.finalcustomer.UpdatingFinalCustomerDto;
import com.sagag.services.common.exception.ServiceException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FinalCustomerService {

  /**
   * Returns the final customer setting DTO.
   *
   * @param orgId the final customer id
   * @return instance of {FinalCustomerSettingDto}
   */
  FinalCustomerSettingDto getFinalCustomerSettings(Long orgId);

  /**
   * Returns the page of final customer info belong to existing customer by criteria.
   *
   * @param customerOrgId the existing customer id
   * @param criteria the search criteria
   * @param pageable the pagination object
   * @return the page of <code>FinalCustomerDto</code>
   */
  Page<FinalCustomerDto> searchFinalCustomersBelongToCustomer(Integer customerOrgId,
      FinalCustomerSearchCriteria criteria, Pageable pageable);

  /**
   * Returns the selected final customer info by id.
   *
   * @param customerOrgId the existing customer id
   * @param finalCustomerOrgId the selected final customer id
   * @param fullMode if true, return the full final customer info, otherwise
   * @return the optional of final customer info
   */
  Optional<FinalCustomerDto> getFinalCustomerInfo(Integer finalCustomerOrgId, boolean fullMode);

  /**
   * Returns the final customer template profile.
   *
   * @param wholesalerOrgId id of wholesaler
   * @param userId id of user
   * @param showNetPriceEnabled
   * @return the object of {@link TemplateNewFinalCustomerProfileDto}
   */
  TemplateNewFinalCustomerProfileDto getTemplateNewFinalCustomerProfile(Integer wholesalerOrgId,
      Long userId, boolean showNetPriceEnabled);

  /**
   * Creates the final customer by model.
   *
   * @param finalCustomerDto the final customer model.
   * @throws ServiceException
   */
  void createFinalCustomer(NewFinalCustomerDto finalCustomerDto) throws ServiceException;

  /**
   * Returns selected final customer.
   *
   * @param finalCustomerOrgId org id of final customer
   * @param wholesalerUserId id of wholesaler whom final customer belong to
   * @param showNetPriceEnabled
   * @return
   */
  UpdatingFinalCustomerDto getSelectedFinalCustomer(Integer finalCustomerOrgId,
      Long wholesalerUserId, boolean showNetPriceEnabled);

  /**
   * Updates existing final customer.
   *
   * @param finalCustomerId id of final customer
   * @param finalCustomerDto editing data
   * @throws ServiceException
   */
  void updateFinalCustomer(Integer finalCustomerId, SavingFinalCustomerDto finalCustomerDto)
      throws ServiceException;

  /**
   * Deletes final customer.
   *
   * @param finalCustomerId id of final customer
   * @throws ServiceException
   */
  void deleteFinalCustomer(Integer finalCustomerId) throws ServiceException;
}
