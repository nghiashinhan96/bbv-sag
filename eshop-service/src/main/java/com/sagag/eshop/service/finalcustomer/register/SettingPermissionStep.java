package com.sagag.eshop.service.finalcustomer.register;

import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.service.api.PermissionService;
import com.sagag.eshop.service.dto.finalcustomer.NewFinalCustomerDto;
import com.sagag.eshop.service.finalcustomer.register.result.NewFinalCustomerStepResult;
import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SettingPermissionStep extends AbstractNewFinalCustomerStep {

  @Autowired
  private PermissionService permissionService;


  @Override
  public String getStepName() {
    return "Set permissions step";
  }

  @Override
  public NewFinalCustomerStepResult processItem(NewFinalCustomerDto finalCustomer,
      NewFinalCustomerStepResult stepResult) {
    List<PermissionConfigurationDto> perms = finalCustomer.getPerms();
    List<EshopGroup> eshopGroups = stepResult.getAddingEshopGroupStepResult().getEshopGroups();
    perms.forEach(perm -> permissionService.updatePermission(eshopGroups, perm.getPermissionId(),
        perm.isEnable()));
    return stepResult;
  }
}
