package com.sagag.eshop.service.finalcustomer.register;

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

import com.sagag.eshop.repo.api.FinalCustomerPropertyRepository;
import com.sagag.eshop.repo.entity.FinalCustomerProperty;
import com.sagag.eshop.repo.entity.SettingsKeys.FinalCustomer;
import com.sagag.eshop.service.dto.finalcustomer.NewFinalCustomerDto;
import com.sagag.eshop.service.finalcustomer.register.result.AddingFinalCustomerPropertyStepResult;
import com.sagag.eshop.service.finalcustomer.register.result.NewFinalCustomerStepResult;
import com.sagag.services.common.enums.FinalCustomerStatus;
import com.sagag.services.common.enums.FinalCustomerType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AddingFinalCustomerPropertyStep extends AbstractNewFinalCustomerStep {

  @Autowired
  private FinalCustomerPropertyRepository finalCustomerPropertyRepo;

  @Override
  public String getStepName() {
    return "Adding final customer property step";
  }

  @Override
  public NewFinalCustomerStepResult processItem(NewFinalCustomerDto finalCustomer,
      NewFinalCustomerStepResult stepResult) {
    long organisationId = stepResult.getAddingOrganisationStepResult().getOrganisation().getId();

    List<FinalCustomerProperty> properties = new ArrayList<>();

    String customerType = FinalCustomerType.valueOf(finalCustomer.getCustomerType()).name();
    properties.add(buildProperty(organisationId, TYPE, customerType));

    String status = finalCustomer.getIsActive() ? FinalCustomerStatus.ACTIVE.name()
        : FinalCustomerStatus.INACTIVE.name();
    properties.add(buildProperty(organisationId, STATUS, status));

    properties.add(buildProperty(organisationId, CUSTOMER_NUMBER, finalCustomer.getCustomerNr()));
    properties.add(buildProperty(organisationId, SALUTATION, finalCustomer.getSalutation()));
    properties.add(buildProperty(organisationId, SURNAME, finalCustomer.getSurName()));
    properties.add(buildProperty(organisationId, FIRSTNAME, finalCustomer.getFirstName()));

    String street = finalCustomer.getStreet();
    if (StringUtils.isNotBlank(street)) {
      properties.add(buildProperty(organisationId, STREET, street));
    }

    String addressOne = finalCustomer.getAddressOne();
    if (StringUtils.isNotBlank(addressOne)) {
      properties.add(buildProperty(organisationId, ADDRESS_1, addressOne));
    }

    String addressTwo = finalCustomer.getAddressTwo();
    if (StringUtils.isNotBlank(addressTwo)) {
      properties.add(buildProperty(organisationId, ADDRESS_2, addressTwo));
    }

    String poBox = finalCustomer.getPoBox();
    if (StringUtils.isNotBlank(poBox)) {
      properties.add(buildProperty(organisationId, PO_BOX, poBox));
    }

    properties.add(buildProperty(organisationId, POSTCODE, finalCustomer.getPostcode()));
    properties.add(buildProperty(organisationId, PLACE, finalCustomer.getPlace()));

    String phone = finalCustomer.getPhone();
    if (StringUtils.isNotBlank(phone)) {
      properties.add(buildProperty(organisationId, PHONE, phone));
    }

    String fax = finalCustomer.getFax();
    if (StringUtils.isNotBlank(fax)) {
      properties.add(buildProperty(organisationId, FAX, fax));
    }

    String email = finalCustomer.getEmail();
    if (StringUtils.isNotBlank(email)) {
      properties.add(buildProperty(organisationId, EMAIL, email));
    }

    List<FinalCustomerProperty> savedProperties = finalCustomerPropertyRepo.saveAll(properties);
    stepResult.setAddingFinalCustomerPropertyStepResult(
        new AddingFinalCustomerPropertyStepResult(savedProperties));
    return stepResult;
  }

  private FinalCustomerProperty buildProperty(Long organisationId, FinalCustomer.Settings key,
      String value) {
    return FinalCustomerProperty.builder().orgId(organisationId).settingKey(key.name()).value(value)
        .build();
  }
}
