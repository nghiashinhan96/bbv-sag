package com.sagag.eshop.repo.api.offer;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.offer.Currency;
import com.sagag.eshop.repo.entity.offer.ShopArticle;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.offer.ShopArticleType;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Integration test class for {@link ShopArticleRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class ShopArticleRepositoryIT {

  @Autowired
  private ShopArticleRepository repository;

  @Test
  public void testSaveSuccessful() {
    final EshopUser user = EshopUser.builder().id(1).build();
    final ShopArticle entity =
        ShopArticle.builder().articleNumber("1125_ART1").currency(Currency.builder().id(1).build())
            .name("Vehicle 1").organisationId(1).type(ShopArticleType.ARTICLE.name()).price(2.5)
            .tecstate("ACTIVE").createdUserId(user.getId())
            .createdDate(Calendar.getInstance().getTime()).build();
    repository.save(entity);
  }

  @Test
  public void testFindByIdsAndOrganisationIdOK() {
    final int orgId = 41;
    final long shopArticleId = 26L; // Article_Number = Thi_OC_90
    Set<Long> shopArticleIds = new HashSet<>(Arrays.asList(shopArticleId));
    List<ShopArticle> result = repository.findByIdsAndOrganisationId(shopArticleIds, orgId);
    Assert.assertTrue(CollectionUtils.isNotEmpty(result));
  }
}
