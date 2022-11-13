package com.sagag.eshop.service.finalcustomer.register;

import com.sagag.eshop.repo.api.CustomerSettingsRepository;
import com.sagag.eshop.repo.api.PriceDisplayTypeRepository;
import com.sagag.eshop.repo.api.WssDeliveryProfileRepository;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.PriceDisplayType;
import com.sagag.eshop.repo.entity.WssDeliveryProfile;
import com.sagag.eshop.service.api.PriceDisplayTypeService;
import com.sagag.eshop.service.dto.finalcustomer.NewFinalCustomerDto;
import com.sagag.eshop.service.exception.FinalCustomerValidationException;
import com.sagag.eshop.service.exception.FinalCustomerValidationException.FinalCustomerErrorCase;
import com.sagag.eshop.service.finalcustomer.register.result.AddingCustomerSettingStepResult;
import com.sagag.eshop.service.finalcustomer.register.result.NewFinalCustomerStepResult;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AddingCustomerSettingStep extends AbstractNewFinalCustomerStep {

  @Autowired
  private CustomerSettingsRepository customerSettingsRepo;

  @Autowired
  private PriceDisplayTypeRepository priceDisplayTypeRepo;

  @Autowired
  private WssDeliveryProfileRepository wssDeliveryProfileRepo;

  @Autowired
  private PriceDisplayTypeService priceDisplayTypeService;

  @Override
  public String getStepName() {
    return "Adding customer settings step";
  }

  @Override
  public NewFinalCustomerStepResult processItem(NewFinalCustomerDto newFinalCustomer,
      NewFinalCustomerStepResult stepResult) throws ServiceException {

    final Integer customerOrgId = newFinalCustomer.getCustomerOrgId();
    CustomerSettings wsSettings = customerSettingsRepo.findSettingsByOrgId(customerOrgId);
    final WssDeliveryProfile wssDeliveryProfile =
        getWssDeliveryProfile(newFinalCustomer.getWssDeliveryProfileDto(), customerOrgId)
            .orElse(null);
    final PriceDisplayTypeEnum defaultPriceDisplayType = priceDisplayTypeService.getDefaultPriceDisplayType();
    PriceDisplayType priceDisplayType = priceDisplayTypeRepo.findByType(defaultPriceDisplayType.name())
        .orElseThrow(() -> new FinalCustomerValidationException(FinalCustomerErrorCase.FC_MP_001,
            "Price type display is missing default value "));

    CustomerSettings newSettings = CustomerSettings.builder()
        .allocationId(wsSettings.getAllocationId()).deliveryId(newFinalCustomer.getDeliveryId())
        .collectiveDelivery(wsSettings.getCollectiveDelivery())
        .netPriceView(newFinalCustomer.getShowNetPrice())
        .netPriceConfirm(wsSettings.isNetPriceConfirm())
        .showDiscount(wsSettings.isShowDiscount())
        .allowNetPriceChanged(wsSettings.isAllowNetPriceChanged())
        .viewBilling(wsSettings.isViewBilling()).addressId(wsSettings.isAddressId())
        .billingAddressId(wsSettings.getBillingAddressId())
        .deliveryAddressId(wsSettings.getDeliveryAddressId())
        .useDefaultSetting(wsSettings.isUseDefaultSetting())
        .emailNotificationOrder(wsSettings.isEmailNotificationOrder())
        .sessionTimeoutSeconds(wsSettings.getSessionTimeoutSeconds())
        .invoiceType(wsSettings.getInvoiceType()).paymentMethod(wsSettings.getPaymentMethod())
        .homeBranch(wsSettings.getHomeBranch()).showOciVat(wsSettings.isShowOciVat())
        .priceDisplayId(priceDisplayType.getId())
        .wssMarginGroup(newFinalCustomer.getWssMarginGroup()).wssDeliveryProfile(wssDeliveryProfile)
        .build();

    CustomerSettings createdCustomerSettings = customerSettingsRepo.save(newSettings);
    stepResult.setAddingCustomerSettingStepResult(
        new AddingCustomerSettingStepResult(createdCustomerSettings));
    return stepResult;
  }

  private Optional<WssDeliveryProfile> getWssDeliveryProfile(
      WssDeliveryProfileDto wssDeliveryProfileDto, int wssOrgId) {
    if (wssDeliveryProfileDto == null || wssDeliveryProfileDto.getId() == null) {
      return Optional.empty();
    }
    return wssDeliveryProfileRepo.findByIdAndOrgId(wssDeliveryProfileDto.getId(), wssOrgId);
  }
}
