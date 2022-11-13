package com.sagag.eshop.service.finalcustomer;

import static com.sagag.eshop.repo.entity.SettingsKeys.FinalCustomer.Settings.ADDRESS_1;
import static com.sagag.eshop.repo.entity.SettingsKeys.FinalCustomer.Settings.ADDRESS_2;
import static com.sagag.eshop.repo.entity.SettingsKeys.FinalCustomer.Settings.CUSTOMER_NUMBER;
import static com.sagag.eshop.repo.entity.SettingsKeys.FinalCustomer.Settings.EMAIL;
import static com.sagag.eshop.repo.entity.SettingsKeys.FinalCustomer.Settings.FAX;
import static com.sagag.eshop.repo.entity.SettingsKeys.FinalCustomer.Settings.FIRSTNAME;
import static com.sagag.eshop.repo.entity.SettingsKeys.FinalCustomer.Settings.PHONE;
import static com.sagag.eshop.repo.entity.SettingsKeys.FinalCustomer.Settings.PLACE;
import static com.sagag.eshop.repo.entity.SettingsKeys.FinalCustomer.Settings.POSTCODE;
import static com.sagag.eshop.repo.entity.SettingsKeys.FinalCustomer.Settings.PO_BOX;
import static com.sagag.eshop.repo.entity.SettingsKeys.FinalCustomer.Settings.SALUTATION;
import static com.sagag.eshop.repo.entity.SettingsKeys.FinalCustomer.Settings.STATUS;
import static com.sagag.eshop.repo.entity.SettingsKeys.FinalCustomer.Settings.STREET;
import static com.sagag.eshop.repo.entity.SettingsKeys.FinalCustomer.Settings.SURNAME;
import static com.sagag.eshop.repo.entity.SettingsKeys.FinalCustomer.Settings.TYPE;

import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.api.WssDeliveryProfileRepository;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.FinalCustomerProperty;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.service.dto.finalcustomer.SavingFinalCustomerDto;
import com.sagag.services.common.enums.FinalCustomerStatus;
import com.sagag.services.common.enums.FinalCustomerType;
import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;
import com.sagag.services.domain.eshop.common.EshopUserCreateAuthority;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class UpdatingFinalCustomerProcessor extends AbstractFinalCustomerProcessor {

  @Autowired
  private OrganisationRepository organisationRepo;

  @Autowired
  private FinalCustomerUserLoginStatusProcessor finalCustomerUserLoginStatusProcessor;

  @Autowired
  private WssDeliveryProfileRepository wssDeliveryProfileRepo;

  public void process(Integer finalCustomerId, SavingFinalCustomerDto finalCustomer) {
    String customerName = finalCustomer.getCustomerName();
    String customerNr = finalCustomer.getCustomerNr();
    updateOrganisation(finalCustomerId, customerName, customerNr);
    updateCustomerSetting(finalCustomerId, finalCustomer.getShowNetPrice(),
        finalCustomer.getWssDeliveryProfileDto(), finalCustomer.getDeliveryId(), finalCustomer.getWssMarginGroup());
    updateCustomerProperty(finalCustomerId, finalCustomer);
    List<EshopGroup> groups = updateEshopGroup(finalCustomerId, customerNr, customerName);
    updatePermission(groups, finalCustomer.getPerms());
  }

  private void updateOrganisation(Integer finalCustomerId, String customerName, String customerNr) {
    Organisation customer = organisationRepo.findOneById(finalCustomerId)
        .orElseThrow(() -> new IllegalArgumentException(
            "Not found final customer with id = " + finalCustomerId));

    String shortName = "customer - " + customerNr;
    customer.setName(customerName);
    customer.setDescription(customerName);
    customer.setShortname(shortName);
    organisationRepo.save(customer);
  }

  private void updateCustomerSetting(Integer finalCustomerId, boolean showNetPrice,
      WssDeliveryProfileDto wssDeliveryProfileDto, int deliveryId, Integer wssMarginGroup) {
    CustomerSettings settings = customerSettingsRepo.findSettingsByOrgId(finalCustomerId);
    settings.setDeliveryId(deliveryId);
    settings.setNetPriceView(showNetPrice);
    settings.setWssMarginGroup(wssMarginGroup);
    if (isValidDeliveryProfile(wssDeliveryProfileDto)) {
      settings.setWssDeliveryProfile(wssDeliveryProfileRepo.findById(wssDeliveryProfileDto.getId())
          .orElseThrow(() -> new IllegalArgumentException(
              "Not found wss delivery profile with id = " + wssDeliveryProfileDto.getId())));
    } else {
      settings.setWssDeliveryProfile(null);
    }
    customerSettingsRepo.save(settings);
  }

  private boolean isValidDeliveryProfile(WssDeliveryProfileDto wssDeliveryProfileDto) {
    return wssDeliveryProfileDto != null && wssDeliveryProfileDto.getId() != null;
  }

  private void updateCustomerProperty(Integer finalCustomerId,
      SavingFinalCustomerDto finalCustomer) {

    List<FinalCustomerProperty> properties =
        finalCustomerPropertyRepo.findByOrgId(finalCustomerId.longValue());

    List<FinalCustomerProperty> deletedProperties = new ArrayList<>();
    if (CollectionUtils.isEmpty(properties)) {
      return;
    }

    filterProperty(properties, TYPE)
        .ifPresent(p -> updateCustomerType(finalCustomer.getCustomerType(), p));
    filterProperty(properties, STATUS)
        .ifPresent(p -> updateCustomerStatus(finalCustomer.getIsActive(), p));

    updateUserLoginStatus(finalCustomerId, finalCustomer.getCustomerType(),
        finalCustomer.getIsActive());

    filterProperty(properties, CUSTOMER_NUMBER)
        .ifPresent(p -> p.setValue(finalCustomer.getCustomerNr()));
    filterProperty(properties, SALUTATION)
        .ifPresent(p -> p.setValue(finalCustomer.getSalutation()));
    filterProperty(properties, SURNAME).ifPresent(p -> p.setValue(finalCustomer.getSurName()));
    filterProperty(properties, FIRSTNAME).ifPresent(p -> p.setValue(finalCustomer.getFirstName()));
    filterProperty(properties, POSTCODE).ifPresent(p -> p.setValue(finalCustomer.getPostcode()));
    filterProperty(properties, PLACE).ifPresent(p -> p.setValue(finalCustomer.getPlace()));

    executeOptionalField(finalCustomerId, STREET, finalCustomer.getStreet(), properties,
        deletedProperties);
    executeOptionalField(finalCustomerId, ADDRESS_1, finalCustomer.getAddressOne(), properties,
        deletedProperties);
    executeOptionalField(finalCustomerId, ADDRESS_2, finalCustomer.getAddressTwo(), properties,
        deletedProperties);
    executeOptionalField(finalCustomerId, PO_BOX, finalCustomer.getPoBox(), properties,
        deletedProperties);
    executeOptionalField(finalCustomerId, PHONE, finalCustomer.getPhone(), properties,
        deletedProperties);
    executeOptionalField(finalCustomerId, FAX, finalCustomer.getFax(), properties,
        deletedProperties);
    executeOptionalField(finalCustomerId, EMAIL, finalCustomer.getEmail(), properties,
        deletedProperties);

    finalCustomerPropertyRepo.saveAll(properties);
    if (CollectionUtils.isNotEmpty(deletedProperties)) {
      finalCustomerPropertyRepo.deleteInBatch(deletedProperties);
    }
  }

  private void executeOptionalField(Integer finalCustomerId,
      SettingsKeys.FinalCustomer.Settings key, String value, List<FinalCustomerProperty> properties,
      List<FinalCustomerProperty> deletedProperties) {
    if (StringUtils.isNoneBlank(value)) {
      updateOptionalPropertyOrCreateIfNotExist(finalCustomerId, properties, key, value);
    } else {
      filterProperty(properties, key).ifPresent(deletedProperties::add);
    }
  }

  private void updateCustomerType(String updatedCustomerType, FinalCustomerProperty customerType) {
    customerType.setValue(FinalCustomerType.valueOf(updatedCustomerType).name());
  }

  private void updateCustomerStatus(boolean status, FinalCustomerProperty statusProperty) {
    statusProperty
        .setValue(status ? FinalCustomerStatus.ACTIVE.name() : FinalCustomerStatus.INACTIVE.name());
  }

  private void updateUserLoginStatus(Integer finalCustomerId, String customerType, boolean status) {
    boolean isOnlineCustomer = FinalCustomerType.valueOf(customerType).isOnline();
    this.finalCustomerUserLoginStatusProcessor.process(finalCustomerId, status && isOnlineCustomer);
  }

  private List<EshopGroup> updateEshopGroup(Integer finalCustomerId, String customerNr,
      String customerName) {
    EshopGroup adminGroup = eshopGroupRepo
        .findByOrgIdAndRoleName(finalCustomerId, EshopUserCreateAuthority.FINAL_USER_ADMIN.name())
        .orElseThrow(() -> new NoSuchElementException("Not found admin user group"));
    adminGroup.setName("FINAL_CUSTOMER_" + customerNr + "_USER_ADMIN");
    adminGroup.setDescription("User admin group of " + customerName);

    EshopGroup normalGroup = eshopGroupRepo
        .findByOrgIdAndRoleName(finalCustomerId, EshopUserCreateAuthority.FINAL_NORMAL_USER.name())
        .orElseThrow(() -> new NoSuchElementException("Not found normal user group"));
    normalGroup.setName("FINAL_CUSTOMER_" + customerNr + "_NORMAL_USER");
    normalGroup.setDescription("Normal user group of " + customerName);

    return eshopGroupRepo.saveAll(Arrays.asList(adminGroup, normalGroup));
  }

  private void updatePermission(List<EshopGroup> eshopGroups,
      List<PermissionConfigurationDto> perms) {
    if (CollectionUtils.isEmpty(perms) || CollectionUtils.isEmpty(eshopGroups)) {
      return;
    }
    perms.forEach(perm -> permissionService.updatePermission(eshopGroups, perm.getPermissionId(),
        perm.isEnable()));
  }

  private void updateOptionalPropertyOrCreateIfNotExist(Integer finalCustomerId,
      List<FinalCustomerProperty> properties, SettingsKeys.FinalCustomer.Settings key,
      String value) {
    Optional<FinalCustomerProperty> propertyOpt = filterProperty(properties, key);
    if (propertyOpt.isPresent()) {
      propertyOpt.get().setValue(value);
    } else {
      FinalCustomerProperty newProperty = FinalCustomerProperty.builder()
          .orgId(finalCustomerId.longValue()).settingKey(key.name()).value(value).build();
      properties.add(newProperty);
    }
  }
}
