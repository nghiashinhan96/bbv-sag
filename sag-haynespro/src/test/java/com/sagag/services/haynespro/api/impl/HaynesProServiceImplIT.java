package com.sagag.services.haynespro.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.common.utils.XmlUtils;
import com.sagag.services.haynespro.HaynesProDataProvider;
import com.sagag.services.haynespro.api.HaynesProService;
import com.sagag.services.haynespro.app.HaynesProApplication;
import com.sagag.services.haynespro.dto.HaynesProPart;
import com.sagag.services.haynespro.dto.HaynesProShoppingCart;
import com.sagag.services.haynespro.request.HaynesProAccessUrlRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { HaynesProApplication.class })
@EshopIntegrationTest
@Slf4j
public class HaynesProServiceImplIT {

  @Autowired
  private HaynesProService service;

  @Test
  public void testGetHaynesProAccessUrlDE() {
    final HaynesProAccessUrlRequest request = HaynesProDataProvider.getHaynesProAccessRequest(
        Locale.GERMAN.getLanguage());
    service.getHaynesProAccessUrl(request);
  }

  @Test
  public void testGetHaynesProShoppingCart() {
    final String xml = XmlUtils.marshal(HaynesProDataProvider.getHaynesProShoppingCart());
    final BufferedReader reader = new BufferedReader(new StringReader(xml));
    Optional<HaynesProShoppingCart> result = service.getHaynesProShoppingCart(
        UUID.randomUUID().toString(), reader);

    Assert.assertThat(result.isPresent(), Matchers.is(true));
    log.debug("HaynesPro Shopping Cart = {}", SagJSONUtil.convertObjectToPrettyJson(
        result.orElse(new HaynesProShoppingCart())));
  }

  @Test
  public void testGetHaynesProShoppingCartNullReader() {
    Optional<HaynesProShoppingCart> result = service.getHaynesProShoppingCart(
        UUID.randomUUID().toString(), null);
    Assert.assertThat(result.isPresent(), Matchers.is(false));
  }

  @Test
  public void testGetHaynesProShoppingCartBlankUUID() {
    final String xml = XmlUtils.marshal(HaynesProDataProvider.getHaynesProShoppingCart());
    final BufferedReader reader = new BufferedReader(new StringReader(xml));
    Optional<HaynesProShoppingCart> result = service.getHaynesProShoppingCart(
        StringUtils.EMPTY, reader);
    Assert.assertThat(result.isPresent(), Matchers.is(false));
  }

  @Test
  public void testGetHaynesProShoppingCartEmptyParts() {
    HaynesProShoppingCart cart = HaynesProDataProvider.getHaynesProShoppingCart();
    cart.getParts().setPart(new HaynesProPart[] {});

    final BufferedReader reader = new BufferedReader(new StringReader(XmlUtils.marshal(cart)));
    Optional<HaynesProShoppingCart> result = service.getHaynesProShoppingCart(
        UUID.randomUUID().toString(), reader);

    Assert.assertThat(result.isPresent(), Matchers.is(true));
  }

  @Test
  public void testGetHaynesProShoppingCartNullParts() {
    HaynesProShoppingCart cart = HaynesProDataProvider.getHaynesProShoppingCart();
    cart.setParts(null);

    final BufferedReader reader = new BufferedReader(new StringReader(XmlUtils.marshal(cart)));
    Optional<HaynesProShoppingCart> result = service.getHaynesProShoppingCart(
        UUID.randomUUID().toString(), reader);

    Assert.assertThat(result.isPresent(), Matchers.is(true));
  }
}
