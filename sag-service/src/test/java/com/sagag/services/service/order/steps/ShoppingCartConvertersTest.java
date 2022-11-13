package com.sagag.services.service.order.steps;

import com.sagag.eshop.repo.entity.order.FinalCustomerOrderItem;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;

import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ShoppingCartConvertersTest {

  @Test
  public void test_converter() throws IOException {
    final String shoppingCartJson = IOUtils.toString(getClass()
        .getResourceAsStream("/json/cartItems.json"), StandardCharsets.UTF_8);
    final ShoppingCart shoppingCart =
        SagJSONUtil.convertJsonToObject(shoppingCartJson, ShoppingCart.class);

    final List<FinalCustomerOrderItem> items =  ShoppingCartConverters.converter(shoppingCart, true);

    Assert.assertThat(items.isEmpty(), Matchers.is(false));
    Assert.assertThat(items.get(0).getReference(), Matchers.equalTo("vw vehicle"));
    Assert.assertThat(items.get(1).getReference(), Matchers.equalTo("seat vehicle"));
    Assert.assertThat(items.get(2).getReference(), Matchers.equalTo("none vehicle"));
  }
}
