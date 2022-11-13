package com.sagag.services.tools.batch.migration.customer_settings;

import com.sagag.services.tools.domain.target.CustomerSettings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum SourceOrganisationPropertyTypeEnum {

  HAS_INVOICE_PREVIEW("hasInvoicePreview") {
    @Override
    CustomerSettings updateSetting(CustomerSettings settings, String value) {
      settings.setViewBilling(new Boolean(value));
      return settings;
    }
  };

  private String type;

  abstract CustomerSettings updateSetting(CustomerSettings settings, String value);

  public static Optional<SourceOrganisationPropertyTypeEnum> fromType(String type) {
    return Arrays.asList(values()).stream().filter(item -> item.getType().equals(type)).findFirst();
  }
}
