package com.sagag.services.ivds.api.impl;

import static org.junit.Assert.assertThat;

import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.contants.TyreConstants;
import com.sagag.services.common.contants.TyreConstants.Season;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.domain.article.ArticlePartDto;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.sag.external.CustomLink;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.elasticsearch.enums.ArticlePartType;
import com.sagag.services.ivds.DataProvider;
import com.sagag.services.ivds.api.IvdsArticleService;
import com.sagag.services.ivds.app.IvdsApplication;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.request.FreetextSearchRequest;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import com.sagag.services.ivds.request.filter.TyreArticleSearchRequest;
import com.sagag.services.ivds.response.CustomArticleResponseDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Integration test class for article search service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { IvdsApplication.class })
@EshopIntegrationTest
@Slf4j
public class IvdsArticleServiceImplIT extends BaseSearchServiceImplIT {

  @Autowired
  private IvdsArticleService ivdsArticleService;

  @Test
  public void testSearchTyreArticlesByCriteria() throws Exception {
    final TyreArticleSearchRequest request = TyreArticleSearchRequest.builder().build();
    final CustomArticleResponseDto response =
        ivdsArticleService.searchTyreArticlesByRequest(user, request, PageUtils.DEF_PAGE);
    assertThat(response.getAggregations(), Matchers.notNullValue());
    assertThat(response.getTotalElements(), Matchers.greaterThanOrEqualTo(Long.valueOf(0)));
  }

  @Test
  public void testSearchTyreArticlesByCriteriaHasArticlesWithWidthCvp() throws Exception {
    final String widthCvp = "10.5";
    final TyreArticleSearchRequest request =
        TyreArticleSearchRequest.builder().width(widthCvp).build();
    final CustomArticleResponseDto response =
        ivdsArticleService.searchTyreArticlesByRequest(user, request, PageUtils.DEF_PAGE);
    assertThat(response.getAggregations(), Matchers.notNullValue());
    assertThat(response.getAggregations().get(TyreConstants.WIDTH_MAP_NAME).size(),
        Matchers.greaterThanOrEqualTo(0));
    assertThat(response.getTotalElements(), Matchers.greaterThanOrEqualTo(Long.valueOf(0)));
  }

  @Test
  public void testSearchTyreArticlesByCriteriaHasArticlesHasWidthCvpAndSummerSeason()
      throws Exception {
    final String widthCvp = "10.5";
    final Season season = TyreConstants.Season.SUMMER;
    final TyreArticleSearchRequest request =
        TyreArticleSearchRequest.builder().season(season.name()).width(widthCvp).build();
    final CustomArticleResponseDto response =
        ivdsArticleService.searchTyreArticlesByRequest(user, request, PageUtils.DEF_PAGE);
    assertThat(response.getAggregations(), Matchers.notNullValue());
    assertThat(response.getAggregations().get(TyreConstants.WIDTH_MAP_NAME).size(),
        Matchers.greaterThanOrEqualTo(0));
    assertThat(response.getTotalElements(), Matchers.greaterThanOrEqualTo(Long.valueOf(0)));
  }

  @Test
  public void testSearchArticleNr_57264021_WithinATCountryForSales() {

    UserInfo user = new UserInfo();
    user.setId(1L);
    user.setUsername("sales");
    user.setSalesId(5L);
    Customer customer = new Customer();
    Map<String, CustomLink> map = new HashMap<>();
    Link link = new Link("/webshop-service/articles/Derendinger-Austria/prices");
    map.put(Customer.PRICE_KEY, SagBeanUtils.map(link, CustomLink.class));

    link = new Link("/webshop-service/articles/Derendinger-Austria/availabilities");
    map.put(Customer.AVAILABILITY_KEY, SagBeanUtils.map(link, CustomLink.class));
    customer.setNr(1100005L);
    customer.setLinks(map);
    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(customer);
    user.setSettings(ownSettings);
    user.setAffiliateShortName("derendinger-at");
    user.setCompanyName("Derendinger-Austria");
    user.setRoles(Arrays.asList(EshopAuthority.USER_ADMIN.name()));

    Page<ArticleDocDto> articles =
        ivdsArticleService.searchArticlesByNumber(user, "57264021", 1, PageUtils.DEF_PAGE, false);
    Assert.assertThat(articles.hasContent(), Matchers.is(true));
    log.debug("Result = {}", SagJSONUtil.convertObjectToJson(articles.getContent()));
  }

  @Test
  public void testSearchArticleNr_57264021_B2BUserDDAT() {

    UserInfo user = new UserInfo();
    user.setId(1L);
    Customer customer = new Customer();
    Map<String, CustomLink> map = new HashMap<>();
    Link link = new Link("/webshop-service/articles/Derendinger-Austria/prices");
    map.put(Customer.PRICE_KEY, SagBeanUtils.map(link, CustomLink.class));

    link = new Link("/webshop-service/articles/Derendinger-Austria/availabilities");
    map.put(Customer.AVAILABILITY_KEY, SagBeanUtils.map(link, CustomLink.class));
    customer.setNr(1100005L);
    customer.setLinks(map);
    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(customer);
    user.setSettings(ownSettings);
    user.setAffiliateShortName("derendinger-at");
    user.setCompanyName("Derendinger-Austria");
    user.setRoles(Arrays.asList(EshopAuthority.USER_ADMIN.name()));

    Page<ArticleDocDto> articles =
        ivdsArticleService.searchArticlesByNumber(user, "57264021", 1, PageUtils.DEF_PAGE, false);
    Assert.assertThat(articles.hasContent(), Matchers.is(true));
  }

  @Test
  public void testSearchArticleNr_57264021_B2BUserMAT() {

    UserInfo user = new UserInfo();
    user.setId(1L);
    Customer customer = new Customer();
    Map<String, CustomLink> map = new HashMap<>();
    Link link = new Link("/webshop-service/articles/Matik-Austria/prices");
    map.put(Customer.PRICE_KEY, SagBeanUtils.map(link, CustomLink.class));

    link = new Link("/webshop-service/articles/Matik-Austria/availabilities");
    map.put(Customer.AVAILABILITY_KEY, SagBeanUtils.map(link, CustomLink.class));
    customer.setNr(8000016L);
    customer.setLinks(map);
    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(customer);
    user.setSettings(ownSettings);
    user.setAffiliateShortName("matik-at");
    user.setCompanyName("Matik-Austria");
    user.setRoles(Arrays.asList(EshopAuthority.USER_ADMIN.name()));

    Page<ArticleDocDto> articles =
        ivdsArticleService.searchArticlesByNumber(user, "57264021", 1, PageUtils.DEF_PAGE, false);
    Assert.assertThat(articles.hasContent(), Matchers.is(true));
  }

  @Test()
  public void testSearchArticlesByScanCode_IdSagsys() {
    final UserInfo user = DataProvider.buildUserInfo();
    final String code = "1000055887";

    final Page<ArticleDocDto> articles = ivdsArticleService.searchArticlesByBarCode(user, code);
    Assert.assertThat(articles.hasContent(), Matchers.is(true));
    Assert.assertThat(articles.getNumberOfElements(), Matchers.is(1));
  }

  @Test()
  public void testSearchArticlesByScanCode_EANNum() {
    final UserInfo user = DataProvider.buildUserInfo();
    final String code = "4009026037935";

    final Page<ArticleDocDto> articles = ivdsArticleService.searchArticlesByBarCode(user, code);
    Assert.assertThat(articles.hasContent(), Matchers.is(true));
    Assert.assertThat(articles.getNumberOfElements(), Matchers.is(1));

    final ArticleDocDto articleDocDto = articles.getContent().get(0);
    final List<ArticlePartDto> partDto = articleDocDto.getParts().stream()
        .filter(part -> ArticlePartType.EAN.name().equals(part.getPtype()))
        .collect(Collectors.toList());
    Assert.assertThat(partDto.size(), Matchers.greaterThan(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSearchArticlesByScanCode_EmptyCode() {
    final UserInfo user = DataProvider.buildUserInfo();
    final String code = StringUtils.EMPTY;
    Page<ArticleDocDto> articles = ivdsArticleService.searchArticlesByBarCode(user, code);
    Assert.assertThat(articles.hasContent(), Matchers.is(false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSearchArticlesByScanCode_NullCode() {
    final UserInfo user = DataProvider.buildUserInfo();
    final String code = null;
    ivdsArticleService.searchArticlesByBarCode(user, code);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSearchArticlesByScanCode_BlankCode() {
    final UserInfo user = DataProvider.buildUserInfo();
    final String code = " ";
    ivdsArticleService.searchArticlesByBarCode(user, code);
  }

  @Test(expected = NullPointerException.class)
  public void testSearchArticlesByFilteringWithEANCode_Null() {
    final String eanCode = null;
    final ArticleFilterRequest request = ArticleFilterRequest.builder().keyword(eanCode)
        .filterMode(FilterMode.EAN_CODE.name()).build();
    ivdsArticleService.searchArticlesByFilteringRequest(user, request, PageUtils.DEF_PAGE);
  }

  @Test
  public void testSearchArticlesByFilteringWithEANCode_Empty() {
    final String EANCode = StringUtils.EMPTY;
    final ArticleFilterRequest request = ArticleFilterRequest.builder().keyword(EANCode)
        .filterMode(FilterMode.EAN_CODE.name()).build();
    ArticleFilteringResponseDto response =
        ivdsArticleService.searchArticlesByFilteringRequest(user, request, PageUtils.DEF_PAGE);
    Assert.assertThat(response.hasContent(), Matchers.is(false));
  }

  @Test
  public void testSearchArticlesByFilteringWithEANCode_Space() {
    final String EANCode = StringUtils.SPACE;
    final ArticleFilterRequest request = ArticleFilterRequest.builder().keyword(EANCode)
        .filterMode(FilterMode.EAN_CODE.name()).build();
    ArticleFilteringResponseDto response =
        ivdsArticleService.searchArticlesByFilteringRequest(user, request, PageUtils.DEF_PAGE);
    Assert.assertThat(response.hasContent(), Matchers.is(false));
  }

  @Test
  public void testSearchArticlesByFilteringWithEANCode() {
    final String EANCode = "3165143377147";
    final ArticleFilterRequest request = ArticleFilterRequest.builder().keyword(EANCode)
        .filterMode(FilterMode.EAN_CODE.name()).build();
    ArticleFilteringResponseDto result =
        ivdsArticleService.searchArticlesByFilteringRequest(user, request, PageUtils.DEF_PAGE);
    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getArticles());
    Assert.assertThat(result.getArticles().getTotalElements(), Matchers.greaterThan(0L));
  }

  @Test
  public void testSearchArticlesByFilteringWithEANCodeAndCriteria() {
    final String EANCode = "3165143377147";
    final ArticleFilterRequest request =
        ArticleFilterRequest.builder().keyword(EANCode).filterMode(FilterMode.EAN_CODE.name())
            .suppliers(Collections.singletonList("BOSCH")).build();
    ArticleFilteringResponseDto result =
        ivdsArticleService.searchArticlesByFilteringRequest(user, request, PageUtils.DEF_PAGE);
    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getArticles());
    Assert.assertThat(result.getArticles().getTotalElements(), Matchers.equalTo(3L));
  }

  @Test
  public void testSearchArticlesByFilteringByFreetextWithEmptyFilter() {
    final ArticleFilterRequest request = ArticleFilterRequest.builder().keyword("oc90")
        .filterMode(FilterMode.FREE_TEXT.name()).suppliers(Collections.emptyList()).build();
    ArticleFilteringResponseDto result =
        ivdsArticleService.searchArticlesByFilteringRequest(user, request, PageUtils.DEF_PAGE);
    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getArticles());
    Assert.assertThat(result.getArticles().getTotalElements(), Matchers.greaterThanOrEqualTo(4L));
  }

  @Test
  public void testSearchArticlesByFilteringByFreetextWithSingleSupplier() {
    final ArticleFilterRequest request =
        ArticleFilterRequest.builder().keyword("oc90").filterMode(FilterMode.FREE_TEXT.name())
            .suppliers(Collections.singletonList("BOSCH")).build();
    ArticleFilteringResponseDto result =
        ivdsArticleService.searchArticlesByFilteringRequest(user, request, PageUtils.DEF_PAGE);
    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getArticles());
    Assert.assertThat(result.getArticles().getTotalElements(), Matchers.greaterThanOrEqualTo(0L));
  }

  @Test
  public void testSearchArticlesByFilteringByFreetextWithMultiSuppliers() {
    final ArticleFilterRequest request =
        ArticleFilterRequest.builder().keyword("oc90").filterMode(FilterMode.FREE_TEXT.name())
            .suppliers(Stream.of("BOSCH", "HENGST FILTER").collect(Collectors.toList())).build();
    ArticleFilteringResponseDto result =
        ivdsArticleService.searchArticlesByFilteringRequest(user, request, PageUtils.DEF_PAGE);
    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getArticles());
    Assert.assertThat(result.getArticles().getTotalElements(), Matchers.greaterThanOrEqualTo(1L));
  }

  @Test
  public void testSearchArticlesByFreeText() throws Exception {
    final FreetextSearchRequest request = FreetextSearchRequest.builder().text("oc90").user(user)
        .pageRequest(PageUtils.DEF_PAGE).build();
    final Page<ArticleDocDto> response = ivdsArticleService.searchFreetext(request).getArticles();
    Assert.assertNotNull(response);
    Assert.assertThat(response.getTotalElements(), Matchers.greaterThanOrEqualTo(4L));
  }

  @Test
  public void testSearchArticlesByFilteringByArticleNrWithEmptyFilter() {
    final ArticleFilterRequest request = ArticleFilterRequest.builder().keyword("oc90")
        .filterMode(FilterMode.ARTICLE_NUMBER.name()).suppliers(Collections.emptyList()).build();
    ArticleFilteringResponseDto result =
        ivdsArticleService.searchArticlesByFilteringRequest(user, request, PageUtils.DEF_PAGE);
    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getArticles());
    Assert.assertThat(result.getArticles().getTotalElements(), Matchers.greaterThanOrEqualTo(4L));
  }

  @Test
  public void testSearchArticlesByFilteringByArticleNrWithSingleSupplier() {
    final ArticleFilterRequest request =
        ArticleFilterRequest.builder().keyword("oc90").filterMode(FilterMode.ARTICLE_NUMBER.name())
            .suppliers(Collections.singletonList("BOSCH")).build();
    ArticleFilteringResponseDto result =
        ivdsArticleService.searchArticlesByFilteringRequest(user, request, PageUtils.DEF_PAGE);
    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getArticles());
    Assert.assertThat(result.getArticles().getTotalElements(), Matchers.greaterThanOrEqualTo(0L));
  }

  @Test
  public void testSearchArticlesByFilteringByArticleNrWithMultiSuppliers() {
    final ArticleFilterRequest request =
        ArticleFilterRequest.builder().keyword("oc90").filterMode(FilterMode.FREE_TEXT.name())
            .suppliers(Stream.of("BOSCH", "HENGST FILTER").collect(Collectors.toList())).build();
    ArticleFilteringResponseDto result =
        ivdsArticleService.searchArticlesByFilteringRequest(user, request, PageUtils.DEF_PAGE);
    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getArticles());
    Assert.assertThat(result.getArticles().getTotalElements(), Matchers.greaterThanOrEqualTo(1L));
  }

  @Test
  public void testSearchArticlesByFilteringByIdSagsysWithEmptyFilter() {
    final ArticleFilterRequest request = ArticleFilterRequest.builder().keyword("1000408772")
        .filterMode(FilterMode.ID_SAGSYS.name()).suppliers(Collections.emptyList()).build();
    ArticleFilteringResponseDto result =
        ivdsArticleService.searchArticlesByFilteringRequest(user, request, PageUtils.DEF_PAGE);
    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getArticles());
    Assert.assertThat(result.getArticles().getTotalElements(), Matchers.equalTo(1L));
  }

  @Test
  public void testSearchArticlesByFilteringByIdSagsysWithSingleSupplier() {
    final ArticleFilterRequest request =
        ArticleFilterRequest.builder().keyword("1000408772").filterMode(FilterMode.ID_SAGSYS.name())
            .suppliers(Collections.singletonList("MAHLE")).build();
    ArticleFilteringResponseDto result =
        ivdsArticleService.searchArticlesByFilteringRequest(user, request, PageUtils.DEF_PAGE);
    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getArticles());
    Assert.assertThat(result.getArticles().getTotalElements(), Matchers.equalTo(1L));
  }

}
