package com.sagag.eshop.service.dto.finalcustomer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.eshop.repo.entity.SettingsKeys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinalCustomerSettingDto implements Serializable {

  private static final long serialVersionUID = -3867875503683704264L;

  @JsonIgnore
  private Map<String, String> properties;

  public boolean getStatus() {
    return toBooleanValue(properties.get(SettingsKeys.FinalCustomer.Settings.STATUS.name()));
  }

  public String getTdWorkshop() {
    return properties.get(SettingsKeys.FinalCustomer.Settings.TD_WORKSHOP.name());
  }

  public String getCustomerNumber() {
    return properties.get(SettingsKeys.FinalCustomer.Settings.CUSTOMER_NUMBER.name());
  }

  public String getSalutaion() {
    return properties.get(SettingsKeys.FinalCustomer.Settings.SALUTATION.name());
  }

  public String getSurname() {
    return properties.get(SettingsKeys.FinalCustomer.Settings.SURNAME.name());
  }

  public String getFirstname() {
    return properties.get(SettingsKeys.FinalCustomer.Settings.FIRSTNAME.name());
  }

  public String getStreet() {
    return properties.get(SettingsKeys.FinalCustomer.Settings.STREET.name());
  }

  public String getAddress1() {
    return properties.get(SettingsKeys.FinalCustomer.Settings.ADDRESS_1.name());
  }

  public String getAddress2() {
    return properties.get(SettingsKeys.FinalCustomer.Settings.ADDRESS_2.name());
  }

  public String getPoBox() {
    return properties.get(SettingsKeys.FinalCustomer.Settings.PO_BOX.name());
  }

  public String getPostCode() {
    return properties.get(SettingsKeys.FinalCustomer.Settings.POSTCODE.name());
  }

  public String getPlace() {
    return properties.get(SettingsKeys.FinalCustomer.Settings.PLACE.name());
  }

  public String getPhone() {
    return properties.get(SettingsKeys.FinalCustomer.Settings.PHONE.name());
  }

  public String getFax() {
    return properties.get(SettingsKeys.FinalCustomer.Settings.FAX.name());
  }

  public String getEmail() {
    return properties.get(SettingsKeys.FinalCustomer.Settings.EMAIL.name());
  }

  @JsonIgnore
  private static boolean toBooleanValue(String value) {
    if (StringUtils.isEmpty(value)) {
      return false;
    }
    return Boolean.valueOf(value);
  }

}
