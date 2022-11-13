package com.sagag.services.service.dto.registration;

import com.sagag.services.common.contants.SagConstants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PotentialCustomerRegistrationBuider {

  private static final String DIV_START = "<div>";
  private static final String DIV_END = "</div>";

  //@formatter:off
  public static String buildHtml(PotentialCustomerRegistrationDto dto) {
    StringBuilder strBuilder = new StringBuilder();
    dto.getFields()
        .forEach(field ->
        strBuilder.append(DIV_START)
                        .append(field.getTitle()).append(SagConstants.COLON).append(SagConstants.SPACE).append(field.getValue())
                  .append(DIV_END));
    return strBuilder.toString();
  }
  //@formatter:on
}
