package com.sagag.services.article.api;

import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.domain.vendor.VendorStockDto;
import com.sagag.services.article.api.request.AvailabilityRequest;
import com.sagag.services.article.api.request.BasketPosition;
import com.sagag.services.article.api.request.PriceRequest;
import com.sagag.services.domain.sag.erp.ArticleStock;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.BulkArticleResult;
import com.sagag.services.domain.sag.erp.PriceWithArticle;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface to provide the services for business update articles, customer from ERP.
 *
 */
public interface ArticleExternalService {

  /**
   * Returns a list of company articles from ERP system by a list of <code>articleIds</code>.
   *
   * <p>
   * <b>ERP API: </b> GET /{company}/articles?articleIds=%s
   * </p>
   *
   * @param compName the company name
   * @param articleIds the list of article ids
   * @return the map of {@link BulkArticleResult} response.
   */
  Map<String, BulkArticleResult> searchByArticleIds(String compName, List<String> articleIds);

  /**
   * Returns a list of company articles from ERP system by a list of <code>umarIds</code>.
   *
   * <p>
   * <b>ERP API: </b> GET /{company}/articles?umarIds=%s
   * </p>
   *
   * @param compName the company name
   * @param umarIds the list of umarIds
   * @return the map of {@link BulkArticleResult} response.
   */
  Map<String, BulkArticleResult> searchByUmarIds(String compName, String umarIds);

  /**
   * Returns the article prices from the price request.
   *
   * @param companyName the company name
   * @param request the price request
   * @param isFinalCustomerUser the flag detect user is final customer user role
   * @return the map of {@link PriceWithArticle} response.
   */
  Map<String, PriceWithArticle> searchPrices(String companyName, PriceRequest request,
      boolean isFinalCustomerUser);

  /**
   * Returns the company stocks from the article ids.
   *
   * @param compName the company name
   * @param articleIds the list of article ids
   * @param branchId the branch id of customer
   * @return the map of {@link ArticleStock} response.
   */
  Map<String, List<ArticleStock>> searchStocks(String compName, List<String> articleIds,
      String branchId);

  /**
   * Returns the article availabilities from the availability request.
   *
   * @param req the availability request
   * @return the map of list of {@link Availability} response.
   * @see {@link AvailabilityRequest}
   */
  Map<String, List<Availability>> searchAvailabilities(AvailabilityRequest req);

  /**
   * <p>
   * Retrieves next working day of a specific warehouse. this method should be invoked only after
   * user login and pickup branch changed in order condition section
   * </p>
   *
   * @param companyName the name of affiliate
   * @param branchId the request branch id
   * @param requestDate the request date
   * @return the optional of <code>Date</code> instance.
   *
   */
  Optional<Date> getNextWorkingDate(String companyName, String branchId, Date requestDate);

  /**
   * Returns the list of vendors by article ids.
   *
   * @param companyName the name of affiliate
   * @param articleIds the list of article ids
   * @return the list of vendors.
   */
  List<VendorDto> searchVendors(String companyName, List<String> articleIds);

  /**
   * Returns the optional of vendor stock by positions, branch id and vendor id.
   *
   * @param companyName the name of affiliate
   * @param vendorId the requested vendor id
   * @param branchId branchId the request branch id
   * @param positions the requested list of positions
   * @return the optional of vendor stock.
   */
  Optional<VendorStockDto> searchVendorStock(String companyName, String vendorId, String branchId,
      List<BasketPosition> positions);

}
