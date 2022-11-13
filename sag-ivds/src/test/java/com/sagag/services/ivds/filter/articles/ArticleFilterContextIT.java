package com.sagag.services.ivds.filter.articles;

import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleAccessoryCriteriaDto;
import com.sagag.services.domain.article.ArticleAccessoryDto;
import com.sagag.services.domain.article.ArticleAccessoryItemDto;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.ivds.app.IvdsApplication;
import com.sagag.services.ivds.filter.impl.CompositeArticleFilterContext;
import com.sagag.services.ivds.request.AccessorySearchRequest;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import com.sagag.services.ivds.request.filter.TyreArticleSearchRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * IT for {@link com.sagag.services.ivds.filter.impl.CompositeArticleFilterContext}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { IvdsApplication.class })
@EshopIntegrationTest
@Slf4j
public class ArticleFilterContextIT {

  @Autowired
  private CompositeArticleFilterContext context;
  @Autowired
  private CustomerExternalService custExternalService;

  private UserInfo user;

  @Before
  public void setup() {
    if (user != null) {
      return;
    }
    user = new UserInfo();
    user.setAffiliateShortName(SupportedAffiliate.DERENDINGER_AT.getAffiliate());
    user.setCompanyName(SupportedAffiliate.DERENDINGER_AT.getCompanyName());
    user.setId(6L);
    user.setRoles(Arrays.asList(EshopAuthority.USER_ADMIN.name()));

    String customerNr = "1127186";
    final Optional<Customer> customer = custExternalService.findCustomerByNumber(
        user.getSupportedAffiliate().getCompanyName(), customerNr);
    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(customer.orElse(null));
    user.setSettings(ownSettings);
    LocaleContextHolder.setLocale(Locale.GERMAN);
  }

  @Test
  public void givenAccessoryRequestShouldReturnArticles() {

    ArticleFilterRequest request = new ArticleFilterRequest();
    request.setFilterMode(FilterMode.ACCESSORY_LIST.name());
    request
        .setAccessorySearchRequest(
            AccessorySearchRequest.builder()
                .accessoryList(Arrays
                    .asList(ArticleAccessoryDto.builder().seqNo(1).linkType(3).linkVal("26552")
                        .accesoryListItems(Arrays.asList(ArticleAccessoryItemDto
                            .builder().accessoryArticleIdArt("1000426311").sort(1).gaid("919")
                            .quantity(1).criteria(Arrays.asList(ArticleAccessoryCriteriaDto
                                .builder().cid("467").cvp("rund").cn("Form").build()))
                            .build()))
                        .build()))
                .build());

    ArticleFilteringResponseDto response = context.execute(user, request, PageUtils.DEF_PAGE,
        Optional.empty());

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(response));

    Assert.assertThat(response.getContextKey(), Matchers.nullValue());
    Assert.assertThat(response.hasContent(), Matchers.is(true));

    Assert.assertThat(response.getArticles().getTotalElements(), Matchers.equalTo(1L));
    Assert.assertThat(response.getArticles().getContent().get(0).getAccessoryLinkText(),
        Matchers.is("SEAT LEON (1P1) 1.4 16V"));
    Assert.assertThat(response.getArticles().getContent().get(0).getSeqNo(),
        Matchers.is(1));
  }


  @Test
  public void givenPimIdShouldReturnOneArticle() {
    ArticleFilterRequest request = new ArticleFilterRequest();
    request.setFilterMode(FilterMode.ID_SAGSYS.name());
    request.setKeyword("1000426311");
    ArticleFilteringResponseDto response = context.execute(user, request, PageUtils.DEF_PAGE,
        Optional.empty());

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(response));

    Assert.assertThat(response.getContextKey(), Matchers.nullValue());
    Assert.assertThat(response.hasContent(), Matchers.is(true));
    Assert.assertThat(response.getArticles().getTotalElements(), Matchers.equalTo(1L));
  }

  @Test
  public void givenArticleNrShouldReturnArticles() {
    ArticleFilterRequest request = new ArticleFilterRequest();
    request.setFilterMode(FilterMode.ARTICLE_NUMBER.name());
    request.setKeyword("OC90");
    ArticleFilteringResponseDto response = context.execute(user, request, PageUtils.DEF_PAGE,
        Optional.empty());

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(response));

    Assert.assertThat(response.getContextKey(), Matchers.notNullValue());
    Assert.assertThat(response.hasContent(), Matchers.is(true));
    Assert.assertThat(response.getArticles().getTotalElements(), Matchers.greaterThanOrEqualTo(1L));
  }

  @Test
  public void givenFreetextShouldReturnArticles() {
    ArticleFilterRequest request = new ArticleFilterRequest();
    request.setFilterMode(FilterMode.FREE_TEXT.name());
    request.setKeyword("audi");
    ArticleFilteringResponseDto response = context.execute(user, request, PageUtils.DEF_PAGE,
        Optional.empty());

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(response));

    Assert.assertThat(response.getContextKey(), Matchers.notNullValue());
    Assert.assertThat(response.hasContent(), Matchers.is(true));
    Assert.assertThat(response.getArticles().getTotalElements(), Matchers.greaterThanOrEqualTo(1L));
  }

  @Ignore
  @Test
  public void givenTyresRequestShouldReturnArticles() {
    ArticleFilterRequest request = new ArticleFilterRequest();
    request.setFilterMode(FilterMode.TYRES_SEARCH.name());
    TyreArticleSearchRequest tRequest = new TyreArticleSearchRequest();
    tRequest.setWidth("255");
    tRequest.setHeight("55");
    request.setTyreSearchRequest(tRequest);
    request.setTotalElementsOfSearching(20);

    // Verify first page
    ArticleFilteringResponseDto response = context.execute(user, request, PageUtils.DEF_PAGE,
        Optional.empty());
    log.debug("-------- Page 1 --------");
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(response));

    Assert.assertThat(response.getContextKey(), Matchers.notNullValue());
    Assert.assertThat(response.hasContent(), Matchers.is(true));
    Assert.assertThat(response.getArticles().getTotalElements(), Matchers.greaterThanOrEqualTo(1L));

    // Verify second page
    request.setContextKey(response.getContextKey());
    response = context.execute(user, request, PageUtils.DEF_PAGE, null);

    log.debug("-------- Page 2 --------");
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(response));

    Assert.assertThat(response.getContextKey(), Matchers.notNullValue());
    Assert.assertThat(response.hasContent(), Matchers.is(true));
    Assert.assertThat(response.getArticles().getTotalElements(), Matchers.greaterThanOrEqualTo(1L));
  }

  @Test
  public void testFilterWithEANCode() {
    final ArticleFilterRequest request = ArticleFilterRequest.builder()
        .filterMode(FilterMode.EAN_CODE.name()).keyword("3165143377147").build();
    final ArticleFilteringResponseDto response =
        context.execute(user, request, PageUtils.DEF_PAGE, Optional.empty());

    Assert.assertThat(response.getContextKey(), Matchers.nullValue());
    Assert.assertThat(response.hasContent(), Matchers.is(true));
    Assert.assertThat(response.getArticles().getTotalElements(), Matchers.greaterThanOrEqualTo(1L));
  }

  @Test
  public void testFilterWithEANCodeAndSupplier() {
    final ArticleFilterRequest request =
        ArticleFilterRequest.builder().filterMode(FilterMode.EAN_CODE.name())
            .keyword("3165143377147").suppliers(Collections.singletonList("BOSCH")).build();

    ArticleFilteringResponseDto response = context.execute(user, request, PageUtils.DEF_PAGE,
        Optional.empty());

    Assert.assertThat(response.getContextKey(), Matchers.nullValue());
    Assert.assertThat(response.hasContent(), Matchers.is(true));
    Assert.assertThat(response.getArticles().getTotalElements(), Matchers.greaterThanOrEqualTo(1L));
  }
}
