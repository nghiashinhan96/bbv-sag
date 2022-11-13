package com.sagag.eshop.service.finalcustomer;

import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.services.common.enums.FinalCustomerStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeletingFinalCustomerProcessor extends AbstractFinalCustomerProcessor {

  @Autowired
  private FinalCustomerUserLoginStatusProcessor finalCustomerUserLoginStatusProcessor;

  public void process(Integer finalCustomerOrgId) {
    changeCustomerStatus(finalCustomerOrgId);
    finalCustomerUserLoginStatusProcessor.process(finalCustomerOrgId, false);
    updateFinalCustomerSettings(finalCustomerOrgId);
  }

  private void changeCustomerStatus(Integer finalCustomerOrgId) {
    finalCustomerPropertyRepo
        .findByOrgIdAndSettingKey(finalCustomerOrgId.longValue(),
            SettingsKeys.FinalCustomer.Settings.STATUS.name())
        .ifPresent(p -> p.setValue(FinalCustomerStatus.DELETED.name()));
  }

  private void updateFinalCustomerSettings(int finalCustomerOrgId) {
    CustomerSettings finalCustomerSettings =
        customerSettingsRepo.findSettingsByOrgId(finalCustomerOrgId);
    finalCustomerSettings.setWssDeliveryProfile(null);
    finalCustomerSettings.setWssMarginGroup(null);
    customerSettingsRepo.save(finalCustomerSettings);
  }
}
