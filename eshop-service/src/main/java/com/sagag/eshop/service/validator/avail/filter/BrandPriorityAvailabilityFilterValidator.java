package com.sagag.eshop.service.validator.avail.filter;

import com.sagag.services.common.validator.IDataValidator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class BrandPriorityAvailabilityFilterValidator implements IDataValidator<String> {

  private static final Pattern CUSTOMER_BRAND_PRIORITY_AVAIL_FILTER_PATTERN =
      Pattern.compile("^[01]{3}");

  @Override
  public boolean validate(String brandPriorityAvailabilityFilter) {
    if (StringUtils.isBlank(brandPriorityAvailabilityFilter)) {
      return false;
    }
    return CUSTOMER_BRAND_PRIORITY_AVAIL_FILTER_PATTERN.matcher(brandPriorityAvailabilityFilter)
        .matches();
  }
}
