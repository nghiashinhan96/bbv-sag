package com.sagag.eshop.service.api.impl.offer;

import com.sagag.eshop.repo.criteria.offer.ShopArticleSearchCriteria;
import com.sagag.eshop.service.api.ShopArticleService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.dto.offer.ShopArticleDto;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.offer.ShopArticleType;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Integration test class for shop article service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
public class ShopArticleServiceImplIT {

  private static final long CREATED_USER_ID = 1L;

  private static final int ORG_ID = 16;

  @Autowired
  private ShopArticleService shopArticleService;

  @Test
  @Transactional
  public void testCreateNewShopArticle() {

    String articleNr = "articleNumber";

    final ShopArticleDto shopArticle = new ShopArticleDto();
    shopArticle.setArticleNumber(articleNr);
    shopArticle.setName("name");
    shopArticle.setDescription("description");
    shopArticle.setAmount(1);
    shopArticle.setPrice(1);
    shopArticle.setCreatedUserId(CREATED_USER_ID);
    shopArticle.setOrganisationId(ORG_ID);
    shopArticle.setType(ShopArticleType.WORK);

    final ShopArticleDto createdShopArticle =
        shopArticleService.createNewShopArticle(shopArticle);

    Assert.assertNotNull(createdShopArticle);
    Assert.assertNotNull(createdShopArticle.getId());
    Assert.assertThat(createdShopArticle.getArticleNumber(), Matchers.equalTo(articleNr));
  }

  @Test
  public void testGetShopArticlesByCriteria_WithEmptyText() {
    PageRequest pageRequest = PageRequest.of(0, 10);
    ShopArticleSearchCriteria criteria = new ShopArticleSearchCriteria();
    criteria.setOrganisationId(ORG_ID);
    criteria.setType(ShopArticleType.ARTICLE.name());

    Page<ShopArticleDto> articles = shopArticleService.getShopArticlesByCriteria(criteria, pageRequest);

    Assert.assertThat(articles, Matchers.notNullValue());
  }

  @Test
  public void testGetShopArticlesByCriteria_WithHasText() {
    PageRequest pageRequest = PageRequest.of(0, 10);
    ShopArticleSearchCriteria criteria = new ShopArticleSearchCriteria();
    criteria.setArticleNumber("1");
    criteria.setOrganisationId(ORG_ID);
    criteria.setType(ShopArticleType.ARTICLE.name());

    Page<ShopArticleDto> articles = shopArticleService.getShopArticlesByCriteria(criteria, pageRequest);

    Assert.assertThat(articles, Matchers.notNullValue());
  }

  @Test
  public void testGetShopArticleDetails() {
    final long shopArticleId = 26L;
    Optional<ShopArticleDto> shopArticleOpt =
        shopArticleService.getShopArticleDetails(shopArticleId);
    Assert.assertThat(shopArticleOpt.isPresent(), Matchers.is(true));
    Assert.assertThat(shopArticleOpt.get().getId(), Matchers.equalTo(shopArticleId));
  }
}
