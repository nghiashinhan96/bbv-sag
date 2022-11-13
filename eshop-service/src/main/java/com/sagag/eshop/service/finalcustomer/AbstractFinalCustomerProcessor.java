package com.sagag.eshop.service.finalcustomer;

import com.sagag.eshop.repo.api.CustomerSettingsRepository;
import com.sagag.eshop.repo.api.EshopGroupRepository;
import com.sagag.eshop.repo.api.FinalCustomerPropertyRepository;
import com.sagag.eshop.repo.entity.FinalCustomerProperty;
import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.service.api.PermissionService;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class AbstractFinalCustomerProcessor {

  @Autowired
  protected CustomerSettingsRepository customerSettingsRepo;

  @Autowired
  protected EshopGroupRepository eshopGroupRepo;

  @Autowired
  protected FinalCustomerPropertyRepository finalCustomerPropertyRepo;

  @Autowired
  protected PermissionService permissionService;

  protected Optional<FinalCustomerProperty> filterProperty(List<FinalCustomerProperty> properties,
      SettingsKeys.FinalCustomer.Settings settingKey) {
    return properties.stream().filter(p -> settingKey.name().equals(p.getSettingKey())).findAny();
  }
}
