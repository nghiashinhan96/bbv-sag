package com.sagag.services.ax.client;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.sagag.services.article.api.request.AvailabilityRequest;
import com.sagag.services.article.api.request.PriceRequest;
import com.sagag.services.ax.domain.AxArticle;
import com.sagag.services.ax.domain.AxArticlesResourceSupport;
import com.sagag.services.ax.domain.AxAvailabilityResourceSupport;
import com.sagag.services.ax.domain.AxBulkArticleStockResourceSupport;
import com.sagag.services.ax.domain.AxNextWorkingDateResourceSupport;
import com.sagag.services.ax.domain.AxPriceResourceSupport;
import com.sagag.services.ax.domain.AxStock;
import com.sagag.services.ax.domain.vendor.AxVendorResouceSupport;
import com.sagag.services.ax.domain.vendor.AxVendorStock;
import com.sagag.services.ax.request.AxAvailabilityClientRequest;
import com.sagag.services.ax.request.AxNextWorkingDateRequest;
import com.sagag.services.ax.request.AxPriceClientRequest;
import com.sagag.services.ax.request.vendor.AxVendorStockRequest;
import com.sagag.services.common.profiles.AxProfile;

@Component
@AxProfile
public class AxArticleClient extends AxBaseClient {

  private static final String API_FIND_ARTICLE_BY_ARTICLE_IDS =
      "/webshop-service/articles/%s?articleIds=%s";

  private static final String API_FIND_ARTICLE_BY_ARTICLE_ID = "/webshop-service/articles/%s/%s";

  private static final String API_FIND_ARTICLE_STOCKS =
      "/webshop-service/articles/%s/stocks/articleIds?articleIds=%s";

  private static final String API_FIND_ARTICLE_STOCK_BY_ID =
      "/webshop-service/articles/%s/%s/stock";

  private static final String API_GET_ATICLE_PRICES = "/webshop-service/articles/%s/prices";

  private static final String API_GET_VENDORS =
      "/webshop-service/articles/%s/vendors?articleIds=%s";

  private static final String API_GET_VENDOR_STOCKS = "/webshop-service/articles/%s/%s/stocks";

  private static final String API_GET_NEXT_WORKING_DATE =
      "/webshop-service/branches/%s/next-working-date";

  private static final String ARTICLE_IDS_STR_BLANK_MESSAGE =
      "The given list article ids must not be empty";

  private static final String ARTICLE_ID_INT_NULL_MESSAGE =
      "The given article id must not be null";

  /**
   * <p>
   * Retrieves representation of a list of articles, which are given by their ids.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param articleIds the input list of article ids as the joining string
   * @return the response of {@link AxArticlesResourceSupport}
   */
  public ResponseEntity<AxArticlesResourceSupport> getArticles(
      String accessToken, String companyName, String articleIds) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.hasText(articleIds, ARTICLE_IDS_STR_BLANK_MESSAGE);
    return exchange(
        toUrl(API_FIND_ARTICLE_BY_ARTICLE_IDS, companyName, articleIds),
        HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxArticlesResourceSupport.class);
  }

  /**
   * <p>
   * Retrieves representation of an article which is given by its id..
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param articleId the input article id
   * @return the response of {@link AxArticle}
   */
  public ResponseEntity<AxArticle> getArticleById(
      String accessToken, String companyName, Integer articleId) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.notNull(articleId, ARTICLE_ID_INT_NULL_MESSAGE);
    return exchange(
        toUrl(API_FIND_ARTICLE_BY_ARTICLE_ID, companyName, articleId),
        HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxArticle.class);
  }

  /**
   * <p>
   * Retrieves the quantity on stock of a certain article which is given by its id.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param articleId the input list of article ids as the joining string
   * @return the stock amount of {@link AxStock}
   */
  public ResponseEntity<AxStock> getArticleStockById(
      String accessToken, String companyName, Integer articleId) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.notNull(articleId, ARTICLE_ID_INT_NULL_MESSAGE);
    return exchange(
        toUrl(API_FIND_ARTICLE_STOCK_BY_ID, companyName, articleId),
        HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxStock.class);
  }

  /**
   * <p>
   * Requests prices of a list of articles from ERP system.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param priceRequest the input price request
   * @return the response of {@link AxPriceResourceSupport}
   */
  public ResponseEntity<AxPriceResourceSupport> getArticlePrices(
      String accessToken, String companyName, PriceRequest priceRequest) {
    Assert.notNull(priceRequest, "The request article prices must not be null");
    Assert.hasText(companyName, "The companyName must not be empty");
    Assert.notEmpty(priceRequest.getBasketPositions(), BASKET_POSITIONS_EMPTY_MESSAGE);
    return exchange(toUrl(API_GET_ATICLE_PRICES, companyName),
        HttpMethod.POST, toHttpEntity(accessToken, AxPriceClientRequest.copy(priceRequest)),
        AxPriceResourceSupport.class);
  }

  /**
   * <p>
   * Retrieves the quantity on stock of a certain article which is given by its id.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param articleIds the input list of article ids as the joining string
   * @return the response of {@link AxBulkArticleStockResourceSupport}
   */
  public ResponseEntity<AxBulkArticleStockResourceSupport> getArticleStocks(
      String accessToken, String companyName, String articleIds, String branchId) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.hasText(articleIds, ARTICLE_IDS_STR_BLANK_MESSAGE);

    final StringBuilder urlBuilder = new StringBuilder(toUrl(API_FIND_ARTICLE_STOCKS, companyName,
        articleIds));
    if (StringUtils.isNotBlank(branchId)) {
      urlBuilder.append("&branchId=").append(branchId);
    }
    return exchange(urlBuilder.toString(), HttpMethod.GET, toHttpEntityNoBody(accessToken),
        AxBulkArticleStockResourceSupport.class);
  }

  /**
   * <p>
   * Requests vendor information for a list of articles
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param articleIds Query parameter containing a list of identifiers of comma-seperated articles
   * @return the response of {@link AxVendorResouceSupport}
   */
  public ResponseEntity<AxVendorResouceSupport> getVendors(String accessToken, String companyName,
      String articleIds) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.hasText(articleIds, ARTICLE_IDS_STR_BLANK_MESSAGE);
    return exchange(toUrl(API_GET_VENDORS, companyName, articleIds), HttpMethod.GET,
        toHttpEntityNoBody(accessToken), AxVendorResouceSupport.class);
  }

  /**
   * <p>
   * Requests stock information for a list of articles from SAG vendors
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param vendorId the vendorID to which the request will be sent
   * @param vendorStockRequest the ax vendor stock request
   * @return the response of {@link AxVendorStock}
   */
  public ResponseEntity<AxVendorStock> getVendorStocks(String accessToken, String companyName,
      String vendorId, AxVendorStockRequest vendorStockRequest) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.hasText(vendorId, "The vendorId must not be empty");
    Assert.notNull(vendorStockRequest, "The vendor stock request must not be null");
    final String url = toUrl(API_GET_VENDOR_STOCKS, companyName, vendorId);
    return exchange(url, HttpMethod.POST, toHttpEntity(accessToken, vendorStockRequest),
        AxVendorStock.class);
  }

  /**
   * <p>
   * Requests availabilities of a list of articles from ERP system.
   * </p>
   *
   * @param accessToken the ax access token
   * @param availabilityRequest the input availabilities request
   * @return the response of {@link AxAvailabilityResourceSupport}
   */
  public ResponseEntity<AxAvailabilityResourceSupport> getAvailabilities(
      String accessToken, AvailabilityRequest availabilityRequest) {
    Assert.notNull(availabilityRequest, "The request article availabilities must not be null");
    Assert.notNull(availabilityRequest.getAvailabilityUrl(),
          "The request availabilities api must not be empty");
    Assert.notEmpty(availabilityRequest.getBasketPositions(), BASKET_POSITIONS_EMPTY_MESSAGE);

    return exchange(toUrl(availabilityRequest.getAvailabilityUrl()),
        HttpMethod.POST, toHttpEntity(accessToken,
                    AxAvailabilityClientRequest.copy(availabilityRequest)),
        AxAvailabilityResourceSupport.class);
  }

  /**
   * <p>
   * Retrieves next working day of a specific warehouse.
   * </p>
   *
   * @param accessToken the ax access token
   * @param companyName the company name
   * @param axRequest the object of {@link AxNextWorkingDateRequest}
   * @return the response of {@link AxNextWorkingDateResourceSupport}
   */
  public ResponseEntity<AxNextWorkingDateResourceSupport> getNextWorkingDate(String accessToken,
      String companyName, AxNextWorkingDateRequest axRequest) {
    Assert.hasText(companyName, COMPANY_NAME_BLANK_MESSAGE);
    Assert.notNull(axRequest, "The ax next working date must not be null");
    return exchange(toUrl(API_GET_NEXT_WORKING_DATE, companyName),
        HttpMethod.POST, toHttpEntity(accessToken, axRequest),
        AxNextWorkingDateResourceSupport.class);
  }
}
