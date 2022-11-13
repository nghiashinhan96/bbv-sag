package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.service.api.EshopFavoriteService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.EshopFavoriteException;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.domain.eshop.uniparts_favorite.dto.EshopFavoriteDto;
import com.sagag.services.domain.eshop.uniparts_favorite.dto.EshopFavoriteRequestDto;

import org.apache.commons.codec.binary.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import javax.transaction.Transactional;

/**
 * Integration tests for Eshop_Favorite service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class EshopFavoriteServiceImplIT {

  @Autowired
  private EshopFavoriteService eshopFavoriteService;

  private UserInfo userInfo;

  @Before
  public void setUp() {
    userInfo = new UserInfo();
    userInfo.setId(41434l);
  }

  public EshopFavoriteDto buildReqBody() {
    return EshopFavoriteDto.builder().type("ARTICLE").articleId("123").title("NEW TITLE")
        .comment("Comment").build();
  }

  @Test
  public void getFavoriteItemList() {
    final List<EshopFavoriteDto> resultDto = eshopFavoriteService.getFavoriteItemList(userInfo);
    Assert.assertThat(resultDto.size(), Matchers.greaterThanOrEqualTo(2));
  }

  @Test
  public void addNewFavoriteItemSuccessfully() throws ValidationException {
    EshopFavoriteDto reqBody = buildReqBody();
    reqBody.setArticleId("189");
    
    eshopFavoriteService.processFavoriteItem(userInfo, reqBody, true);
    
    final List<EshopFavoriteDto> resultDto = eshopFavoriteService.getFavoriteItemList(userInfo);
    Assert.assertThat(resultDto.stream()
        .filter(favo -> StringUtils.equals(favo.getArticleId(), "189")).findAny().isPresent(),
        Matchers.is(true));
  }
  
  @Test(expected = EshopFavoriteException.class)
  public void addNewFavoriteItemThrowException() throws ValidationException {
    EshopFavoriteDto reqBody = buildReqBody();
    eshopFavoriteService.processFavoriteItem(userInfo, reqBody, true);
  }
  
  @Test
  public void editFavoriteItemSuccessfully() throws ValidationException {
    EshopFavoriteDto reqBody = buildReqBody();
    EshopFavoriteDto result = eshopFavoriteService.updateFavoriteItem(userInfo, reqBody);
    Assert.assertNotNull(result);
    Assert.assertThat(result.getTitle(), Matchers.equalTo("NEW TITLE"));
  }
  
  @Test
  public void deleteFavoriteItemSuccessfully() throws ValidationException {
    EshopFavoriteDto reqBody = buildReqBody();
    eshopFavoriteService.processFavoriteItem(userInfo, reqBody, false);
  }
  
  
  @Test
  public void searchFavoriteItem() {
    EshopFavoriteRequestDto dto = new EshopFavoriteRequestDto();
    dto.setKeySearch("VEHICLE");
    Page<EshopFavoriteDto> result = eshopFavoriteService.searchFavoriteItem(userInfo, dto);
    Assert.assertThat(result.getContent().size(), Matchers.greaterThanOrEqualTo(1));
  }
  
  @Test
  public void searchComments() {
    List<String> result = eshopFavoriteService.searchComments("comment",userInfo);
    Assert.assertThat(result.size(), Matchers.greaterThanOrEqualTo(2));
  }

}
