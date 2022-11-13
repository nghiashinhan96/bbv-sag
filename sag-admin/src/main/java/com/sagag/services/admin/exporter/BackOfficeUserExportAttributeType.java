package com.sagag.services.admin.exporter;

import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.backoffice.dto.ExportingUserDto;

/**
 * The enumeration of user export header column name.
 */
public enum BackOfficeUserExportAttributeType {

  CUSTOMER_NUMBER("Kundennummer") {
    @Override
    public String getAttrValue(ExportingUserDto user) {
      return user.getCustomerNumber();
    }
  },
  DVSE_CUSTOMER_NAME("DVSE Kundenname") {
    @Override
    public String getAttrValue(ExportingUserDto user) {
      return user.getDvseCustomerName();
    }
  },
  DVSE_USERNAME("DVSE Benutzername") {
    @Override
    public String getAttrValue(ExportingUserDto user) {
      return user.getDvseUserName();
    }
  },
  AFFILIATE_SHORTNAME("Gesellschaft") {
    @Override
    public String getAttrValue(ExportingUserDto user) {
      return user.getAffiliateShortname();
    }
  },
  ROLE_NAME("Rolle") {
    @Override
    public String getAttrValue(ExportingUserDto user) {
      return user.getRoleName();
    }
  },
  SALUTAION("Anrede") {
    @Override
    public String getAttrValue(ExportingUserDto user) {
      return user.getSalutation();
    }
  },
  LANGUAGE("Sprache") {
    @Override
    public String getAttrValue(ExportingUserDto user) {
      return user.getLanguage();
    }
  },
  USERNAME("Benutzername") {
    @Override
    public String getAttrValue(ExportingUserDto user) {
      return user.getUserName();
    }
  },
  FIRST_NAME("Vorname") {
    @Override
    public String getAttrValue(ExportingUserDto user) {
      return user.getFirstName();
    }
  },
  LAST_NAME("Nachname") {
    @Override
    public String getAttrValue(ExportingUserDto user) {
      return user.getLastName();
    }
  },
  EMAIL("E-Mail") {
    @Override
    public String getAttrValue(ExportingUserDto user) {
      return user.getEmail();
    }
  },
  ZIP("Zip") {
    @Override
    public String getAttrValue(ExportingUserDto user) {
      return user.getZip();
    }
  },
  CH_ZIP("PLZ") {
    @Override
    public String getAttrValue(ExportingUserDto user) {
      return user.getZip();
    }
  },
  FIRST_LOGIN_DATE("First login date") {
    @Override
    public String getAttrValue(ExportingUserDto user) {
        return DateUtils.toStringDate(user.getFirstLoginDate(), DateUtils.SWISS_DATE_TIME_PATTERN);
    }
  },
  CH_FIRST_LOGIN_DATE("erstes Login") {
    @Override
    public String getAttrValue(ExportingUserDto user) {
      return DateUtils.toStringDate(user.getFirstLoginDate(), DateUtils.SWISS_DATE_TIME_PATTERN);
    }
  },
  LAST_LOGIN_DATE("Last login date") {
    @Override
    public String getAttrValue(ExportingUserDto user) {
      return DateUtils.toStringDate(user.getLastLoginDate(), DateUtils.SWISS_DATE_TIME_PATTERN);
    }
  },
  CH_LAST_LOGIN_DATE("letztes Login") {
      @Override
      public String getAttrValue(ExportingUserDto user) {
          return DateUtils.toStringDate(user.getLastLoginDate(), DateUtils.SWISS_DATE_TIME_PATTERN);
      }
  };

  private String value;

  private BackOfficeUserExportAttributeType(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  abstract String getAttrValue(ExportingUserDto user);

}

