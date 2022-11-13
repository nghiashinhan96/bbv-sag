package com.sagag.services.stakis.erp.converter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.JAXBException;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.stakis.erp.DataProvider;
import com.sagag.services.stakis.erp.converter.impl.article.TmArticleConverter;
import com.sagag.services.stakis.erp.converter.impl.article.TmGetPriceAndStockResponseConverterImpl;
import com.sagag.services.stakis.erp.wsdl.tmconnect.GetErpInformationResponseBody;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@Slf4j
public class TmGetErpInformationResponseConverterTest {

  @InjectMocks
  private TmGetPriceAndStockResponseConverterImpl converter;

  @Mock
  private TmArticleConverter articleConverter;

  @Test
  public void shouldConvertGetErpInformationResponseSucceed() throws IOException, JAXBException {
    final ArticleDocDto article = DataProvider.buildArticles("10403173").get(0);

    final GetErpInformationResponseBody responseBody =
        DataProvider.getGetErpArticlesResponseBody("/data/articles_10403173.xml").getValue();

    Mockito.when(articleConverter.convert(Mockito.any(), Mockito.any(), Mockito.any(),
        Mockito.any(), Mockito.any(), Mockito.anyDouble()))
    .thenReturn(DataProvider.convertJsonToArticle("/data/articles_10403173_result.json"));

    final List<ArticleDocDto> result = converter.apply(Lists.newArrayList(article), responseBody,
        DataProvider.DF_VAT_FOR_CZ, Locale.GERMAN.getLanguage());
    log.debug(SagJSONUtil.convertObjectToPrettyJson(result));

    Assert.assertThat(result.isEmpty(), Matchers.is(false));

    final ArticleDocDto foundArticle = result.get(0);
    final String depositArtId = "11288784";

    Assert.assertThat(foundArticle.getDepositArticle(), Matchers.notNullValue());
    Assert.assertThat(foundArticle.getArticle().getDepotArticleId(),
        Matchers.equalTo(depositArtId));
    Assert.assertThat(foundArticle.getDepositArticle().getIdSagsys(),
        Matchers.equalTo(depositArtId));
  }

  @Test
  public void shouldConvertGetErpInformationResponseSucceed_10756280()
      throws IOException, JAXBException {
    final ArticleDocDto article = DataProvider.buildArticles("10756280").get(0);

    final GetErpInformationResponseBody responseBody =
        DataProvider.getGetErpArticlesResponseBody("/data/articles_10756280.xml").getValue();

      Mockito.when(articleConverter.convert(Mockito.any(), Mockito.any(), Mockito.any(),
          Mockito.any(), Mockito.any(), Mockito.anyDouble()))
      .thenReturn(DataProvider.convertJsonToArticle("/data/articles_10756280_result.json"));

    final List<ArticleDocDto> result = converter.apply(Lists.newArrayList(article), responseBody,
        DataProvider.DF_VAT_FOR_CZ, Locale.GERMAN.getLanguage());
    log.debug(SagJSONUtil.convertObjectToPrettyJson(result));

    Assert.assertThat(result.isEmpty(), Matchers.is(false));
  }
}
