package com.sagag.services.common.enums;

import java.util.Arrays;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

@Getter
@AllArgsConstructor
public enum OrderSource {

  RAPIDBOX_AT("rpdbox-at"),
  RAPIDBOX_CH("rpdbox-ch"),
  RAPIDBOX_CZ("rbdbox-cz"),
  DMS_AT("dms-at"),
  DMS_CH("dms-ch"),
  DMS_CZ("dms-cz"),
  DMS_RS("dms-rs");

  private String source;

  public static OrderSource fromText(final String text) {
    Assert.hasText(text, "The given OrderSource must not be empty");
    return Arrays.asList(values()).stream().filter(val -> StringUtils.equals(text, val.getSource()))
        .findFirst()
        .orElseThrow(() -> new NoSuchElementException("The order source is not supported!"));
  }

}
