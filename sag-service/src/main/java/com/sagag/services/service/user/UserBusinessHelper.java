package com.sagag.services.service.user;

import com.sagag.eshop.repo.api.CustomerSettingsRepository;
import com.sagag.eshop.repo.api.DeliveryTypesRepository;
import com.sagag.eshop.repo.api.UserSettingsRepository;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.DeliveryType;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.hazelcast.api.CustomerCacheService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserBusinessHelper {

  @Autowired
  private CustomerSettingsRepository customerSettingsRepo;

  @Autowired
  private DeliveryTypesRepository deliveryTypesRepo;

  @Autowired
  private CustomerCacheService customerCacheService;

  @Autowired
  private UserSettingsRepository userSettingsRepo;

  /**
   * Sync Customer deliveryType From AX to Eshop.
   *
   * @param user the user info
   * @param customerSettings the customer setting
   * @return instance of <CustomerSettings>
   */
  public CustomerSettings syncDeliveryTypeFromAX(UserInfo user, CustomerSettings customerSettings) {
    final String customerNr = user.getCustNrStr();
    final String companyName = user.getCompanyName();
    Customer erpCustomer = customerCacheService.getCachedCustomer(customerNr, companyName)
        .orElseThrow(IllegalStateException::new);
    Optional<DeliveryType> deliveryTypeOpt =
        deliveryTypesRepo.findOneByDescCode(erpCustomer.getSendMethod());
    if (!deliveryTypeOpt.isPresent()) {
      throw new UnsupportedOperationException(
          String.format("Delivery Type %s is not supported", erpCustomer.getSendMethod()));
    }

    DeliveryType deliveryType = deliveryTypeOpt.get();

    UserSettings userSettings = userSettingsRepo.findSettingsByUserId(user.getId());
    // Avoid hitting database if settings are not changed
    if (deliveryType.getId() != userSettings.getDeliveryId()) {
      userSettings.setDeliveryId(deliveryType.getId());
      userSettingsRepo.save(userSettings);
    }
    // Avoid hitting database if settings are not changed
    if (deliveryType.getId() != customerSettings.getDeliveryId()) {
      customerSettings.setDeliveryId(deliveryType.getId());
      return customerSettingsRepo.save(customerSettings);
    }

    return customerSettings;
  }

}
