package com.sagag.services.ax.executor;

import java.util.Arrays;
import java.util.List;

import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.ax.AxApplication;
import com.sagag.services.ax.AxDataTestUtils;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;

import lombok.extern.slf4j.Slf4j;

/**
 * Class to verify {@link AxArticleSearchExecutor}.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { AxApplication.class })
@EshopIntegrationTest
@Slf4j
@Ignore
public class AxArticleSearchExecutorIT {

  @Autowired
  private AxArticleSearchExecutor axArticleSearchExecutor;

  @Test(expected = ValidationException.class)
  public void testExecuteWithGetArticlesInfo() {

    ArticleDocDto article = new ArticleDocDto();
    article.setIdSagsys("1001097638");
    article.setAmountNumber(1);
    ArticleSearchCriteria.ArticleSearchCriteriaBuilder builder = ArticleSearchCriteria.builder()
        .affiliate(SupportedAffiliate.DERENDINGER_AT)
        .custNr("1109480")
        .availabilityUrl("/webshop-service/articles/Derendinger-Austria/availabilities/")
        .partialDelivery(true)
        .deliveryType(ErpSendMethodEnum.PICKUP.name())
        .articles(Arrays.asList(article))
        .filterArticleBefore(false)
        .updatePrice(false).updateStock(false)
        .addressId(StringUtils.EMPTY)
        .updateAvailability(true).isCartMode(true)
        .nextWorkingDate(AxDataTestUtils.buildFixedNextWorkingDates())
        .pickupBranchId("1001");


    final List<ArticleDocDto> articles = axArticleSearchExecutor.execute(builder.build());
    log.info("Result = {}", SagJSONUtil.convertObjectToPrettyJson(articles));
    Assert.assertNotNull(articles);
  }

}
