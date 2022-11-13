package com.sagag.eshop.service.api.impl.offer;

import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.offer.ShopArticleRepository;
import com.sagag.eshop.repo.criteria.offer.ShopArticleSearchCriteria;
import com.sagag.eshop.repo.entity.offer.ShopArticle;
import com.sagag.eshop.service.api.impl.ShopArticleServiceImpl;
import com.sagag.eshop.service.dto.offer.ShopArticleDto;
import com.sagag.services.common.enums.offer.ShopArticleType;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


/**
 * UT for shop article service.
 */
@RunWith(MockitoJUnitRunner.class)
public class ShopArticleServiceImplTest {

  private static final long CREATED_USER_ID = 6L;

  private static final String ARTICLE_NR = "APEC";

  private static final int ORG_ID = 1;

  private static final ShopArticleType TYPE = ShopArticleType.ARTICLE;

  @InjectMocks
  private ShopArticleServiceImpl shopArticleService;

  @Mock
  private EshopUserRepository eshopUserRepo;

  @Mock
  private ShopArticleRepository shopArticleRepo;

  @Mock
  private ShopArticle shopArticle;

  @Mock
  private Page<ShopArticleDto> shopArticles;

  private PageRequest firstPageRequest = PageRequest.of(0, 10);

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    Mockito.when(shopArticleRepo.save(Mockito.any(ShopArticle.class))).thenReturn(shopArticle);
    Mockito.when(shopArticle.getArticleNumber()).thenReturn(ARTICLE_NR);
    Mockito.when(shopArticle.getType()).thenReturn(TYPE.name());
  }

  @Test
  public void testCreateNewShopArticle() {
    ShopArticleDto shopArticleDto = new ShopArticleDto();
    shopArticleDto.setArticleNumber(ARTICLE_NR);
    shopArticleDto.setCreatedUserId(CREATED_USER_ID);
    shopArticleDto.setOrganisationId(ORG_ID);
    shopArticleDto.setType(ShopArticleType.ARTICLE);

    final ShopArticleDto createdShopArticle =
        shopArticleService.createNewShopArticle(shopArticleDto);

    Assert.assertNotNull(createdShopArticle);
    Assert.assertThat(createdShopArticle.getArticleNumber(), Matchers.equalTo(ARTICLE_NR));

    // Verify times mockup
    Mockito.verify(shopArticleRepo, Mockito.times(1)).save(Mockito.any(ShopArticle.class));
    Mockito.verify(shopArticle, Mockito.times(1)).getArticleNumber();
    Mockito.verify(shopArticle, Mockito.times(1)).getType();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewShopArticle_With_CreatedUserId_IsNull() {
    ShopArticleDto shopArticleDto = new ShopArticleDto();
    shopArticleDto.setArticleNumber(ARTICLE_NR);
    shopArticleDto.setType(ShopArticleType.ARTICLE);
    shopArticleService.createNewShopArticle(shopArticleDto);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewShopArticle_With_ShopArticle_IsNull() {

    shopArticleService.createNewShopArticle(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewShopArticle_With_ArticleNr_IsNull() {
    ShopArticleDto shopArticleDto = new ShopArticleDto();
    shopArticle.setCreatedUserId(CREATED_USER_ID);
    shopArticleDto.setType(ShopArticleType.ARTICLE);
    shopArticleService.createNewShopArticle(shopArticleDto);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetShopArticlesByCriteria_With_NullCriteria() {

    shopArticleService.getShopArticlesByCriteria(null, firstPageRequest);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetShopArticlesByCriteria_With_NullOrgId() {
    ShopArticleSearchCriteria criteria = new ShopArticleSearchCriteria();
    criteria.setArticleNumber(StringUtils.EMPTY);
    criteria.setType(ShopArticleType.ARTICLE.name());

    shopArticleService.getShopArticlesByCriteria(criteria, firstPageRequest);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetShopArticlesByCriteria_With_NullType() {
    ShopArticleSearchCriteria criteria = new ShopArticleSearchCriteria();
    criteria.setArticleNumber(StringUtils.EMPTY);
    criteria.setOrganisationId(ORG_ID);

    shopArticleService.getShopArticlesByCriteria(criteria, firstPageRequest);

  }
}
