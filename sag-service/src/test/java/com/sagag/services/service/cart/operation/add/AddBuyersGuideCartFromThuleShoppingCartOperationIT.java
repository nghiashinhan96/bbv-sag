package com.sagag.services.service.cart.operation.add;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.SagServiceApplication;
import com.sagag.services.service.user.cache.ISyncUserLoader;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { SagServiceApplication.class })
@EshopIntegrationTest
@Transactional
@Slf4j
public class AddBuyersGuideCartFromThuleShoppingCartOperationIT {

  @Autowired
  private AddBuyersGuideCartFromThuleShoppingCartOperation operation;

  @Autowired
  private ISyncUserLoader syncUserLoader;

  private UserInfo user;

  @Before
  public void init() {
    user = syncUserLoader.load(26l, StringUtils.EMPTY, StringUtils.EMPTY, Optional.empty());
  }

  @Test
  public void testAddBuyersGuideCartFromThuleShoppingCartOperation() {
    final Map<String, String> buyersGuideFormData = new HashMap<>();
    buyersGuideFormData.put("dealer", "test-ddat-0C9094D8-09DB-40C5-83A8-48D22750A7DB");
    buyersGuideFormData.put("order_list", "20201301_1|20201401_2|721400_1|721500_2");
    ShoppingCart result = operation.execute(user, buyersGuideFormData,
        ShopType.DEFAULT_SHOPPING_CART);

    log.debug("Added Shopping Cart Items:\n{}",
        SagJSONUtil.convertObjectToPrettyJson(result.getItems()));

    log.debug("Not Found Part:\n{}",
        SagJSONUtil.convertObjectToPrettyJson(result.getNotFoundPartNumbers()));
  }
}
