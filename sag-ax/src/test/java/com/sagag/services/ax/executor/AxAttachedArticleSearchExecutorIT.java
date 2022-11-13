package com.sagag.services.ax.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sagag.services.article.api.executor.AttachedArticleSearchCriteria;
import com.sagag.services.article.api.request.AttachedArticleRequest;
import com.sagag.services.ax.AxApplication;
import com.sagag.services.ax.AxDataTestUtils;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;

import lombok.extern.slf4j.Slf4j;

/**
 * Class to verify {@link AxAttachedArticleShoppingCartSearchExecutor}.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { AxApplication.class })
@EshopIntegrationTest
@Slf4j
public class AxAttachedArticleSearchExecutorIT {

  @Autowired
  private AxAttachedArticleShoppingCartSearchExecutor executor;

  @Test
  public void shouldExecuteWithDupplicateDepotArticleButDifferentQuantity() {

    List<AttachedArticleRequest> attachedArticleRequestList = new ArrayList<>();
    attachedArticleRequestList.add(new AttachedArticleRequest("1001420930", 2, 1, "cartKey_1", 0d));
    attachedArticleRequestList.add(new AttachedArticleRequest("1001420930", 5, 1, "cartKey_2", 0d));

    final AttachedArticleSearchCriteria criteria = new AttachedArticleSearchCriteria();
    criteria.setAffiliate(SupportedAffiliate.DERENDINGER_AT);
    criteria.setCustNr("1100005");
    criteria.setGrossPrice(true);
    criteria.setSpecialNetPriceArticleGroup(true);
    criteria.setAttachedArticleRequestList(attachedArticleRequestList);
    criteria.setUpdatePrice(true);
    criteria.setPriceTypeDisplayEnum(PriceDisplayTypeEnum.UVPE_OEP_GROSS);
    criteria.setCompanyName(AxDataTestUtils.companyNameOfDDAT());
    criteria.setFinalCustomerUser(false);

    Map<String, ArticleDocDto> result = executor.execute(criteria);
    log.debug("result = {}", SagJSONUtil.convertObjectToPrettyJson(result));

    Assert.assertThat(result.size(), Matchers.is(attachedArticleRequestList.size()));
  }

}
