package com.sagag.services.ax.client;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sagag.services.article.api.request.BasketPosition;
import com.sagag.services.ax.AxApplication;
import com.sagag.services.ax.AxBaseIT;
import com.sagag.services.ax.AxDataTestUtils;
import com.sagag.services.ax.domain.AxArticle;
import com.sagag.services.ax.domain.AxArticlesResourceSupport;
import com.sagag.services.ax.domain.AxAvailabilityResourceSupport;
import com.sagag.services.ax.domain.AxBulkArticleStockResourceSupport;
import com.sagag.services.ax.domain.AxNextWorkingDateResourceSupport;
import com.sagag.services.ax.domain.AxPriceResourceSupport;
import com.sagag.services.ax.domain.AxStock;
import com.sagag.services.ax.request.AxPriceRequest;
import com.sagag.services.common.annotation.EshopIntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { AxApplication.class })
@EshopIntegrationTest
@Ignore("Save time for verify build, because the AX service often has problems for failed build")
public class AxArticleClientIT extends AxBaseIT {

  @Autowired
  private AxArticleClient axClient;

  private static String cachedToken;

  @Before
  public void init() {
    cachedToken = getAxToken(cachedToken);
  }

  @Test
  public void testGetArticles() {
    final String articleIds = "1001219628,1001037670";
    ResponseEntity<AxArticlesResourceSupport> jsonRes = axClient.getArticles(cachedToken,
        AxDataTestUtils.companyNameOfDDAT(), articleIds);
    assertOkHttpStatus(jsonRes);
  }

  @Test
  public void testGetArticleById() {
    final Integer articleId = 1001219628;
    ResponseEntity<AxArticle> jsonRes = axClient.getArticleById(cachedToken,
        AxDataTestUtils.companyNameOfDDAT(), articleId);
    assertOkHttpStatus(jsonRes);
  }

  @Test
  public void testGetArticleStocks() {
    final String articleIds = "1001015956,";
    ResponseEntity<AxBulkArticleStockResourceSupport> jsonRes = axClient.getArticleStocks(cachedToken,
        AxDataTestUtils.companyNameOfDDAT(), articleIds, StringUtils.EMPTY);
    assertOkHttpStatus(jsonRes);
  }

  @Test
  public void testGetArticleStockById() {
    Integer articleId = 1001015956;
    ResponseEntity<AxStock> jsonRes =
        axClient.getArticleStockById(cachedToken, AxDataTestUtils.companyNameOfDDAT(), articleId);
    assertOkHttpStatus(jsonRes);
  }

  @Test
  public void testGetArticlePrices() {
    ResponseEntity<AxPriceResourceSupport> jsonRes =
        axClient.getArticlePrices(cachedToken, AxDataTestUtils.companyNameOfDDAT(), AxDataTestUtils.priceRequest());
    assertOkHttpStatus(jsonRes);
  }

  @Test
  public void givenArticleId_shouldGetArticlePricesWithAddtionalPrices() {
    AxPriceRequest request = (AxPriceRequest) AxDataTestUtils.priceRequest();
    request.setCustomerNr("1100005");
    BasketPosition basketPosition = AxDataTestUtils.basketPostion();
    basketPosition.setArticleId("1000426311");
    request.setBasketPositions(Arrays.asList(basketPosition));
    ResponseEntity<AxPriceResourceSupport> jsonRes = axClient.getArticlePrices(cachedToken, AxDataTestUtils.companyNameOfDDAT(), request);
    assertOkHttpStatus(jsonRes);
  }

  @Test
  public void testGetNextWorkingDate() {
    ResponseEntity<AxNextWorkingDateResourceSupport> nextWrkDateRes =
        axClient.getNextWorkingDate(cachedToken, AxDataTestUtils.companyNameOfDDAT(),
            AxDataTestUtils.nextWorkingDateRequestWithNow());

    assertOkHttpStatus(nextWrkDateRes);
  }

  @Test
  public void testGetAvailabilities() {
    ResponseEntity<AxAvailabilityResourceSupport> jsonRes = axClient.getAvailabilities(cachedToken,
        AxDataTestUtils.availabilityRequest());
    assertOkHttpStatus(jsonRes);
  }
}
