package com.sagag.eshop.service.virtualuser.impl;

import com.sagag.eshop.service.DataProvider;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceApplication.class)
@EshopIntegrationTest
@Transactional
public class RemoveEshopFavoriteForVirtualUserHandlerImplIT {

  @Autowired
  private RemoveEshopFavoriteForVirtualUserHandlerImpl handler;

  @Test
  public void givenVVirtualUserListShouldRemoveEshopFavoriteItems() {
    handler.accept(DataProvider.buildVirtualUserList(26l));
  }

  @Test
  public void givenEmptyVVirtualUserListShouldNonRemoveEshopFavoriteItems() {
    handler.accept(Collections.emptyList());
  }
}
