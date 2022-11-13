package com.sagag.services.rest.controller.user_history;

import com.sagag.eshop.repo.criteria.user_history.UserArticleHistorySearchCriteria;
import com.sagag.eshop.service.api.UserArticleHistoryService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.UserHistoryFromSource;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.eshop.dto.ArticleHistoryDto;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.request.AddArticleHistoryRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for historical data.
 */
@RestController
@RequestMapping(value = "/history/article", produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Article History API")
public class ArticleHistoryController {

  @Autowired
  private UserArticleHistoryService userArticleHistoryService;

  //@formatter:off
  @ApiOperation(
      value = ApiDesc.ArticleHistory.SEARCH_LATEST_ARTICLE_HISTORY_API_DESC,
      notes = ApiDesc.ArticleHistory.SEARCH_LATEST_ARTICLE_HISTORY_API_NOTE
  )
  @GetMapping(
      value = "/latest",
      produces = MediaType.APPLICATION_JSON_VALUE
  ) //@formatter:on
  public Page<ArticleHistoryDto> getArticleHistory(final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return userArticleHistoryService.getLastestArticleHistory(user.getOriginalUserId());
  }

  //@formatter:off
  @ApiOperation(
      value = ApiDesc.ArticleHistory.SEARCH_ARTICLE_HISTORY_API_DESC,
      notes = ApiDesc.ArticleHistory.SEARCH_ARTICLE_HISTORY_API_NOTE
  )
  //@formatter:on
  @PostMapping("/search")
  public Page<ArticleHistoryDto> searchVehicleHistories(final OAuth2Authentication authed,
      @RequestBody final UserArticleHistorySearchCriteria criteria,
      @PageableDefault final Pageable pageable) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    if (user.isFinalUserRole()) {
      criteria.setOrgId(user.getFinalCustomerOrgId());
    } else {
      criteria.setOrgId(user.getOrganisationId());
    }

    criteria.setUserId(user.getOriginalUserId());
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(
        userArticleHistoryService.searchArticleHistories(criteria, pageable));
  }

  //@formatter:off
  @ApiOperation(
      value = ApiDesc.ArticleHistory.ADD_ARTICLE_HISTORY_API_DESC,
      notes = ApiDesc.ArticleHistory.ADD_ARTICLE_HISTORY_API_NOTE
  )
  //@formatter:on
  @PostMapping("/add")
  public void addVehicleHistories(final OAuth2Authentication authed,
      @RequestBody final AddArticleHistoryRequest request,
      @PageableDefault final Pageable pageable) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final UserHistoryFromSource fromSource =
        UserHistoryFromSource.findFromSource(user.isSaleOnBehalf());

    userArticleHistoryService.addArticleHistory(user.getOriginalUserId(), request.getArticleId(),
        request.getArticleNumber(), request.getSearchTerm(), request.getSearchMode(), fromSource,
        request.getRawSearchTerm());
  }

  //@formatter:off
  @ApiOperation(
      value = ApiDesc.ArticleHistory.UPDATE_ARTICLE_HISTORY_API_DESC,
      notes = ApiDesc.ArticleHistory.UPDATE_ARTICLE_HISTORY_API_NOTE
  )
  //@formatter:on
  @PutMapping("/update")
  public void updateVehicleHistories(final OAuth2Authentication authed,
      @RequestParam final long userArticleHistoryId) {
    final UserInfo user = (UserInfo) authed.getPrincipal();

    userArticleHistoryService.updateArticleHistorySelectDate(user.getOriginalUserId(),
        userArticleHistoryId);
  }
}
