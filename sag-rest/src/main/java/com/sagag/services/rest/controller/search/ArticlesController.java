package com.sagag.services.rest.controller.search;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceRequestItem;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceResponseItem;
import com.sagag.services.hazelcast.request.UpdateAmountRequestBody;
import com.sagag.services.ivds.executor.IvdsArticleAmountTaskExecutor;
import com.sagag.services.ivds.executor.IvdsArticleAvailabilityTaskExecutor;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;
import com.sagag.services.ivds.executor.impl.IvdsArticleErpInformationTaskExecutorImpl;
import com.sagag.services.ivds.request.GetArticleSyncInformation;
import com.sagag.services.ivds.request.availability.GetArticleInformation;
import com.sagag.services.ivds.response.GetArticleInformationResponse;
import com.sagag.services.ivds.response.availability.GetArticleAvailabilityResponse;
import com.sagag.services.rest.swagger.docs.ApiDesc;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Controller class to define APIs for article search.
 */
@RestController
@RequestMapping(value = "/articles", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Articles APIs")
public class ArticlesController {

  private static final String ERROR_MSG_IDSAGSYS_EMPTY =
      "the article idSagSys should not be null or blank";

  @Autowired
  private IvdsArticleAvailabilityTaskExecutor ivdsArticleAvailabilityTaskExecutor;

  @Autowired
  private IvdsArticleTaskExecutors ivdsArticleTaskExecutors;

  @Autowired
  private IvdsArticleErpInformationTaskExecutorImpl ivdsArticleErpInformationTaskExecutor;

  @Autowired
  private IvdsArticleAmountTaskExecutor ivdsArticleAmountTaskExecutor;

  /**
   * Returns one article by availability updated. The response is one article ref.
   *
   * @param authed the authenticated user
   * @param body the request body
   * @return a {@link Page<ArticleDocDto>}
   */
  @ApiOperation(value = ApiDesc.ArticleSearch.SEARCH_ARTICLE_BY_AMOUNT_API_DESC,
      notes = ApiDesc.ArticleSearch.SEARCH_ARTICLE_BY_AMOUNT_API_NOTE)
  @PostMapping(value = "/availability")
  public Page<ArticleDocDto> getUpdatedAvailability(final OAuth2Authentication authed,
      @RequestBody final UpdateAmountRequestBody body) throws ResultNotFoundException {
    final String idSagSys = body.getPimId();
    Assert.hasText(idSagSys, ERROR_MSG_IDSAGSYS_EMPTY);

    final UserInfo user = (UserInfo) authed.getPrincipal();
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(
        new PageImpl<>(ivdsArticleAvailabilityTaskExecutor.executeAvailability(user, idSagSys,
            body.getAmount(), body.getStock(), body.getTotalAxStock())));
  }

  /**
   * Returns the list of updated availabilities .
   *
   * @param authed the authenticated user
   * @param request the request body
   * @return a {@link GetArticleInformationResponse}
   */
  @ApiOperation(value = "Search article availabilities",
      notes = "The service will search the list of article availabilities")
  @PostMapping(value = "/availabilities")
  public GetArticleAvailabilityResponse getUpdatedAvailabilities(final OAuth2Authentication authed,
      @RequestBody final GetArticleInformation request) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return ivdsArticleAvailabilityTaskExecutor.executeAvailabilities(user, request);
  }

  /**
   * Returns the articles by order amount updated.
   *
   * @param authed the authenticated user
   * @param body the request body
   * @return a {@link Page<ArticleDocDto>}
   */
  @ApiOperation(value = ApiDesc.ArticleSearch.SEARCH_ARTICLE_BY_AMOUNT_API_DESC,
      notes = ApiDesc.ArticleSearch.SEARCH_ARTICLE_BY_AMOUNT_API_NOTE)
  @PostMapping(value = "/price")
  public Page<ArticleDocDto> getUpdatedPrice(final OAuth2Authentication authed,
      @RequestBody final UpdateAmountRequestBody body) throws ResultNotFoundException {
    final String idSagSys = body.getPimId();
    Assert.hasText(idSagSys, ERROR_MSG_IDSAGSYS_EMPTY);
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(ivdsArticleAmountTaskExecutor
        .execute(user, idSagSys, body.getAmount(), Optional.ofNullable(body.getFinalCustomerId())));
  }

  @ApiOperation(value = ApiDesc.ArticleSearch.SEARCH_ARTICLE_DISPLAY_PRICES_DESC,
      notes = ApiDesc.ArticleSearch.SEARCH_ARTICLE_DISPLAY_PRICES_NOTE)
  @PostMapping(value = "/display-prices")
  public List<DisplayedPriceResponseItem> getArticleDisplayPrices(OAuth2Authentication authed,
      @RequestBody List<DisplayedPriceRequestItem> requestItems) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return ivdsArticleTaskExecutors.executeTaskWithArticleDisplayPrices(requestItems, user);
  }

  /**
   * Returns the list of updated erp information .
   *
   * @param authed the authenticated user
   * @param request the request body
   * @return a {@link GetArticleAvailabilityResponse}
   */
  @ApiOperation(value = "Search article erp information",
      notes = "The service will search the list of article with specific request information")
  @PostMapping(value = "/erp-sync")
  public GetArticleInformationResponse getUpdatedErpInformation(final OAuth2Authentication authed,
      @RequestBody final GetArticleSyncInformation request) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return ivdsArticleErpInformationTaskExecutor.executeErpInformation(user, request);
  }
}
