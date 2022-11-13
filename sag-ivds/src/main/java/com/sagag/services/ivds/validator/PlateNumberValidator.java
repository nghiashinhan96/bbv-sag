package com.sagag.services.ivds.validator;

import com.sagag.services.common.validator.IDataValidator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PlateNumberValidator implements IDataValidator<String> {

  private static final Pattern PLATE_NUM_PATTERN = Pattern.compile("^[a-z A-Z]{2}[0-9]{1,6}$");

  @Override
  public boolean validate(String plateNumber) {
    if (StringUtils.isBlank(plateNumber)) {
      return false;
    }
    return PLATE_NUM_PATTERN.matcher(plateNumber).matches();
  }
}
