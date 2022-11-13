package com.sagag.services.stakis.erp.client;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.stakis.erp.DataProvider;
import com.sagag.services.stakis.erp.StakisErpApplication;
import com.sagag.services.stakis.erp.domain.TmArticlePriceAndAvailabilityRequest;
import com.sagag.services.stakis.erp.domain.TmUserCredentials;
import com.sagag.services.stakis.erp.wsdl.tmconnect.GetErpInformationResponseBody;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { StakisErpApplication.class })
@EshopIntegrationTest
public class StakisErpTmConnectClientIT {

  @Autowired
  private StakisErpTmConnectClient client;

  @Test
  public void test() {
    final TmUserCredentials credentials = DataProvider.tmUserCredentials();
    credentials.setContextId(1);

    final ArticleDocDto article = new ArticleDocDto();
    article.setIdSagsys("10602512");
    article.setSupplier("");
    article.setArtnr("");
    article.setIdDlnr("");
    article.setAmountNumber(1);

    TmArticlePriceAndAvailabilityRequest request =
        new TmArticlePriceAndAvailabilityRequest(DataProvider.buildArticles());
    GetErpInformationResponseBody body = client.getERPInformation(credentials, request);
    Assert.assertThat(body, Matchers.notNullValue());
  }

}
