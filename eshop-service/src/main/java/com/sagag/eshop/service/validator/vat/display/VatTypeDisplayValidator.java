package com.sagag.eshop.service.validator.vat.display;

import com.sagag.services.common.validator.IDataValidator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class VatTypeDisplayValidator implements IDataValidator<String> {

  private static final Pattern VAT_TYPE_DISPLAY_PATTERN = Pattern.compile("^[01]{2}");

  @Override
  public boolean validate(String vatTypeDisplay) {
    if (StringUtils.isBlank(vatTypeDisplay)) {
      return false;
    }
    return VAT_TYPE_DISPLAY_PATTERN.matcher(vatTypeDisplay).matches();
  }
}
