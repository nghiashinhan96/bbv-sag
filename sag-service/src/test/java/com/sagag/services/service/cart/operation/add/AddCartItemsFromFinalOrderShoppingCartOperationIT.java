package com.sagag.services.service.cart.operation.add;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.SagServiceApplication;
import com.sagag.services.service.user.cache.ISyncUserLoader;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { SagServiceApplication.class })
@EshopIntegrationTest
@Transactional
@Slf4j
public class AddCartItemsFromFinalOrderShoppingCartOperationIT {

  @Autowired
  private AddCartItemsFromFinalOrderShoppingCartOperation operation;

  @Autowired
  private ISyncUserLoader syncUserLoader;

  private UserInfo user;

  @Before
  public void init() {
    user = syncUserLoader.load(30857l, StringUtils.EMPTY, StringUtils.EMPTY, Optional.empty());
  }

  @Test
  @Ignore("The customer = 5132364 of user is not found in AX SWS")
  public void test() {
    ShoppingCart shopCart = operation.execute(user, 1l, ShopType.SUB_FINAL_SHOPPING_CART,
        ArrayUtils.EMPTY_OBJECT_ARRAY);
    Assert.assertThat(shopCart, Matchers.notNullValue());
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(shopCart));
  }

}
