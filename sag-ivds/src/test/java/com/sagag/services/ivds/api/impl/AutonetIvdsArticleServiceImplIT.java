package com.sagag.services.ivds.api.impl;

import com.google.common.collect.Sets;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.AutonetEshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.ivds.api.IvdsArticleService;
import com.sagag.services.ivds.app.IvdsApplication;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { IvdsApplication.class })
@AutonetEshopIntegrationTest
@Ignore("Will enable after go-live Autonet feature")
public class AutonetIvdsArticleServiceImplIT {

  protected static final SupportedAffiliate AFFLIATE = SupportedAffiliate.AUTONET_HUNGARY;

  @Autowired
  private IvdsArticleService ivdsArticleService;

  private UserInfo user;

  @Before
  public void init() {
    if (user != null) {
      return;
    }
    user = new UserInfo();
    user.setCompanyName(AFFLIATE.getCompanyName());
    user.setAffiliateShortName(AFFLIATE.getAffiliate());
    user.setRoles(Arrays.asList(EshopAuthority.USER_ADMIN.name()));
    LocaleContextHolder.setLocale(Locale.GERMAN);
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
  public void testSearchArticlesBySearchArticlesByArticleIds() {
    Page<ArticleDocDto> result =
        ivdsArticleService.searchArticlesByArticleIds(user, Sets.newHashSet("oc90"),
            Optional.empty());
    Assert.assertNotNull(result);
  }
}
