package com.sagag.services.ivds.filter;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.RelevanceGroupType;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.category.BrandDto;
import com.sagag.services.domain.eshop.category.SortDto;
import com.sagag.services.domain.sag.erp.ArticleStock;
import com.sagag.services.hazelcast.api.BrandPriorityCacheService;
import com.sagag.services.hazelcast.domain.categories.CachedBrandPriorityDto;
import com.sagag.services.ivds.filter.impl.CachedPerfectMatchArticleFilterContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RunWith(SpringRunner.class)
public class ArticleFilterContextTest {

  @InjectMocks
  private CachedPerfectMatchArticleFilterContext filterContext;

  @Mock
  private BrandPriorityCacheService brandPriorityCacheService;

  private UserInfo user;
  private Map<String, CachedBrandPriorityDto> brandPriorities;

  public ArticleFilterContextTest() {
  }

  @Before
  public void setup() {
    if (Objects.isNull(brandPriorities)) {
      brandPriorities = new HashMap<>();

      Map<Integer, Integer> sortPrio = new HashMap<>();
      sortPrio.put(1, 1);
      BrandDto brand = new BrandDto();
      brand.setBrand("10");
      brand.setCollection("ddch");
      brand.setAffiliate("dch");
      brand.setSorts(sortPrio);
      List<BrandDto> brands = new ArrayList<>();
      brands.add(brand);

      SortDto sort = new SortDto();
      sort.setBrands(brands);
      List<SortDto> sorts = new ArrayList<>();
      sorts.add(sort);

      CachedBrandPriorityDto brandPriority = new CachedBrandPriorityDto();
      brandPriority.setSorts(sorts);
      brandPriorities.put("123", brandPriority);
    }
    if (Objects.isNull(user)) {
      user = new UserInfo();
      user.setCompanyName("Derendinger-Switzerland");
      user.setCollectionName("ddch");
    }
  }

  @Test
  public void shouldSortArticleStockDesc() {
    // Given
    List<ArticleDocDto> source = new ArrayList<>();
    ArticleDocDto article1 = new ArticleDocDto();
    article1.setRelevanceGroupType(RelevanceGroupType.DIRECT_MATCH);
    article1.setStock(ArticleStock.builder().stock(10.0).build());
    ArticleDocDto article2 = new ArticleDocDto();
    article2.setRelevanceGroupType(RelevanceGroupType.REFERENCE_MATCH);
    article2.setStock(ArticleStock.builder().stock(0.0).build());
    ArticleDocDto article3 = new ArticleDocDto();
    article3.setRelevanceGroupType(RelevanceGroupType.REFERENCE_MATCH);
    article3.setStock(ArticleStock.builder().stock(20.0).build());
    ArticleDocDto article4 = new ArticleDocDto();
    article4.setRelevanceGroupType(RelevanceGroupType.REFERENCE_MATCH);
    article4.setStock(ArticleStock.builder().build());
    source.add(article1);
    source.add(article2);
    source.add(article3);
    source.add(article4);
    // When
    List<ArticleDocDto> result = filterContext.sortArticleStockDesc(source);
    // Then
    Assert.assertEquals(4, result.size());
    Assert.assertEquals(RelevanceGroupType.DIRECT_MATCH, result.get(0).getRelevanceGroupType());
    Assert.assertEquals(20.0, result.get(2).getStock().getStockAmount(), 0.1);
  }

  @Test
  public void shouldSortArticlesBrandPriority() {
    // Given
    List<ArticleDocDto> source = new ArrayList<>();
    ArticleDocDto article1 = new ArticleDocDto();
    article1.setGaId("123");
    article1.setIdProductBrand("11");
    article1.setRelevanceGroupType(RelevanceGroupType.REFERENCE_MATCH);
    article1.setStock(ArticleStock.builder().stock(10.0).build());
    ArticleDocDto article2 = new ArticleDocDto();
    article2.setGaId("123");
    article2.setIdProductBrand("10");
    article2.setRelevanceGroupType(RelevanceGroupType.REFERENCE_MATCH);
    article2.setStock(ArticleStock.builder().stock(0.0).build());
    ArticleDocDto article3 = new ArticleDocDto();
    article3.setGaId("123");
    article3.setIdProductBrand("10");
    article3.setRelevanceGroupType(RelevanceGroupType.REFERENCE_MATCH);
    article3.setStock(ArticleStock.builder().stock(20.0).build());
    ArticleDocDto article4 = new ArticleDocDto();
    article4.setGaId("123");
    article4.setIdProductBrand("10");
    article4.setRelevanceGroupType(RelevanceGroupType.REFERENCE_MATCH);
    article4.setStock(ArticleStock.builder().build());
    source.add(article1);
    source.add(article2);
    source.add(article3);
    source.add(article4);

    Mockito.when(brandPriorityCacheService.findCachedBrandPriority(Arrays.asList("123", "123", "123", "123"))).thenReturn(brandPriorities);

    // When
    List<ArticleDocDto> result = filterContext.sortArticlesBrandPriority(user, source);
    // Then
    Assert.assertEquals(4, result.size());
    Assert.assertEquals(20.0, result.get(0).getStock().getStockAmount(), 0.1);
    Assert.assertEquals(10.0, result.get(3).getStock().getStockAmount(), 0.1);
  }
}