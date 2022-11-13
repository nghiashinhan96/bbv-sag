package com.sagag.services.autonet.erp.api.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.autonet.erp.AutonetErpApplication;
import com.sagag.services.common.annotation.AutonetEshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { AutonetErpApplication.class })
@AutonetEshopIntegrationTest
@Slf4j
public class AutonetErpArticleExternalServiceImplIT {

  private static final double DF_VAT = 8.0;

  @Autowired
  private AutonetErpArticleExternalServiceImpl autonetErpArticleExternalServiceImpl;

  @Test
  public void searchArticlePricesAndAvailabilities_shouldGetPricesForArticles() throws Exception {

    ArticleDocDto article1 = new ArticleDocDto();
    article1.setIdSagsys("LP1706");
    article1.setAmountNumber(100);
    article1.setIdDlnr("3");
    article1.setArtnrDisplay("13.0460-7143.2");

    ArticleDocDto article2 = new ArticleDocDto();
    article2.setIdSagsys("100000");
    article2.setAmountNumber(1);
    article2.setIdDlnr("3");
    article2.setArtnrDisplay("13.0460-7143.2");
    List<ArticleDocDto> articles = Arrays.asList(article1, article2);

    List<ArticleDocDto> result =
        autonetErpArticleExternalServiceImpl.searchArticlePricesAndAvailabilities("realsergiu_test",
            "17665", "94C88E9D-330E-4157-A823-A9CE94ED739F", "DE", articles, DF_VAT,
            new AdditionalSearchCriteria());
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(result));
    assertThat(result.size(), Matchers.is(2));
    assertNotNull((result.get(0)).getAutonetInfos());
  }
}
