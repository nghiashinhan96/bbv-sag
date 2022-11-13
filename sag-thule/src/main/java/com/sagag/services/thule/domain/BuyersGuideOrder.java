package com.sagag.services.thule.domain;

import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.Assert;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BuyersGuideOrder extends BuyersGuideUnit {

  private static final long serialVersionUID = 2783396887485715922L;

  private static final char ORDER_SEPARATOR = '_';

  private static final int REQUIRED_OF_LENGTH = 2;

  private long quantity;

  public BuyersGuideOrder(String component) {
    Assert.hasText(component, "The given component item must not be empty");
    final String[] components = StringUtils.split(component, ORDER_SEPARATOR);
    assertRequiredComponents(components);
    Optional.ofNullable(components[0]).map(StringUtils::trim).ifPresent(this::setMovexId);
    Optional.ofNullable(components[1]).map(StringUtils::trim).map(NumberUtils::createLong)
    .ifPresent(this::setQuantity);
  }

  private void assertRequiredComponents(String[] components) {
    if (ArrayUtils.isEmpty(components) || ArrayUtils.getLength(components) < REQUIRED_OF_LENGTH) {
      throw new IllegalArgumentException(String.format(
          "The components array size must be greater or equals than %d.", REQUIRED_OF_LENGTH));
    }
  }
}
