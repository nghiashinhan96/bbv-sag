package com.sagag.services.stakis.erp.converter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.JAXBException;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.stakis.erp.DataProvider;
import com.sagag.services.stakis.erp.StakisErpApplication;
import com.sagag.services.stakis.erp.converter.impl.article.TmGetPriceAndStockResponseConverterImpl;
import com.sagag.services.stakis.erp.wsdl.tmconnect.GetErpInformationResponseBody;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StakisErpApplication.class)
@EshopIntegrationTest
@CzProfile
@Slf4j
public class TmGetErpInformationResponseConverterIT {

  @Autowired
  private TmGetPriceAndStockResponseConverterImpl converter;

  @Test
  public void shouldConvertGetErpInformationResponseSucceed() throws IOException, JAXBException {
    final ArticleDocDto article = DataProvider.buildArticles("10403173").get(0);

    final GetErpInformationResponseBody responseBody =
        DataProvider.getGetErpArticlesResponseBody("/data/articles_10403173.xml").getValue();
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
    final List<ArticleDocDto> result = converter.apply(Lists.newArrayList(article), responseBody,
        DataProvider.DF_VAT_FOR_CZ, Locale.GERMAN.getLanguage());

    log.debug(SagJSONUtil.convertObjectToPrettyJson(result));

    Assert.assertThat(result.isEmpty(), Matchers.is(false));

    final ArticleDocDto foundArticle = result.get(0);
    Assert.assertThat(foundArticle.isAllowedAddToShoppingCart(), Matchers.is(true));
  }

  @Test
  public void shouldSetDefaultValueForQtyMultipleInCaseOfZero()
      throws IOException, JAXBException {
    final ArticleDocDto article = DataProvider.buildArticles("10756280").get(0);
    article.setQtyMultiple(null);

    final GetErpInformationResponseBody responseBody =
        DataProvider.getGetErpArticlesResponseBody("/data/articles_10756280.xml").getValue();

    //Simulate the case  by Overriding value of QuantityPackagingUnit from the erp repsonse to zero
	responseBody.getGetErpInformationResult().getValue().getErpArticleInformation().getValue()
	  .getErpInformation().forEach(erpInfo -> {
		  erpInfo.getWarehouses().getValue().getWarehouse().forEach(wh -> {
			wh.getQuantities().getValue().getQuantity().forEach(qty -> {
				qty.setQuantityPackingUnit(BigDecimal.ZERO);
			});
		});
	});

    final List<ArticleDocDto> result = converter.apply(Lists.newArrayList(article), responseBody,
        DataProvider.DF_VAT_FOR_CZ, Locale.GERMAN.getLanguage());

    log.debug(SagJSONUtil.convertObjectToPrettyJson(result));

    final ArticleDocDto foundArticle = result.get(0);
    Assert.assertThat(foundArticle.getQtyMultiple(), Matchers.is(SagConstants.DEFAULT_MULTIPLE_QUANTITY));
  }

  @Test
  public void shouldSetDefaultValueForQtyMultipleInCaseOfNull()
      throws IOException, JAXBException {
    final ArticleDocDto article = DataProvider.buildArticles("10756280").get(0);
    article.setQtyMultiple(null);


    final GetErpInformationResponseBody responseBody =
        DataProvider.getGetErpArticlesResponseBody("/data/articles_10756280.xml").getValue();

    //Simulate the case  by Overriding value of QuantityPackagingUnit from the erp repsonse to zero
    responseBody.getGetErpInformationResult().getValue().getErpArticleInformation().getValue()
	  .getErpInformation().forEach(erpInfo -> {
		 erpInfo.getWarehouses().getValue().getWarehouse().forEach(wh -> {
		   wh.getQuantities().getValue().getQuantity().forEach(qty -> {
			  qty.setQuantityPackingUnit(null);
			});
		});
	});

    final List<ArticleDocDto> result = converter.apply(Lists.newArrayList(article), responseBody,
        DataProvider.DF_VAT_FOR_CZ, Locale.GERMAN.getLanguage());

    log.debug(SagJSONUtil.convertObjectToPrettyJson(result));

    final ArticleDocDto foundArticle = result.get(0);
    Assert.assertThat(foundArticle.getQtyMultiple(), Matchers.is(SagConstants.DEFAULT_MULTIPLE_QUANTITY));
  }
}
