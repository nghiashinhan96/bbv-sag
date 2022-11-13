package com.sagag.services.haynespro.builder;

import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.common.utils.XmlUtils;
import com.sagag.services.haynespro.dto.HaynesProShoppingCart;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;

@RequiredArgsConstructor
@Slf4j
public class HaynesProShopCartResponseBuilder {

  @NonNull
  private BufferedReader reader;

  public HaynesProShoppingCart build() {
    final StringBuilder xmlBuilder = new StringBuilder();
    reader.lines().forEach(line -> xmlBuilder.append(line).append(System.lineSeparator()));
    final String xml = xmlBuilder.toString();
    log.debug("XML response = {}", xml);

    final HaynesProShoppingCart cart = XmlUtils.unmarshal(HaynesProShoppingCart.class, xml);
    log.info("The shopping cart from HaynesPro = {}", SagJSONUtil.convertObjectToPrettyJson(cart));
    return cart;
  }

}
