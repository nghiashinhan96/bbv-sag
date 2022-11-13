package com.sagag.services.rest.controller.finalcustomer;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.WssImportMarginArticleGroupResponseDto;
import com.sagag.eshop.service.dto.WssMarginArticleGroupDto;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.exception.CsvServiceException;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.utils.CsvUtils;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.article.WssArticleGroupDto;
import com.sagag.services.domain.eshop.criteria.WssMarginArticleGroupSearchCriteria;
import com.sagag.services.domain.eshop.dto.WssCsvMarginArticleGroup;
import com.sagag.services.ivds.api.IvdsWssArticleGroupService;
import com.sagag.services.ivds.request.WssArticleGroupSearchRequest;
import com.sagag.services.rest.authorization.annotation.HasWholesalerPreAuthorization;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.WssMarginArticleGroupService;
import com.sagag.services.service.exception.WssBrandMaginException;
import com.sagag.services.service.exception.WssMarginArticleGroupException;
import com.sagag.services.service.exception.WssMarginArticleGroupException.WssMarginArticleGroupErrorCase;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * The controller exposes API for WSS margin by article group.
 *
 */
@RestController
@RequestMapping("/wss/margin-by-article-group")
@Api(tags = "WSS Margin by article group")
public class WssMarginArticleGroupController {

  @Autowired
  private WssMarginArticleGroupService wssMarginArticleGroupService;

  @Autowired
  private IvdsWssArticleGroupService ivdsWssArticleGroupService;

  @ApiOperation(value = ApiDesc.MarginByArticleGroup.CREATE_MARGIN_BY_ARTICLE_GROUP_DESC,
      notes = ApiDesc.MarginByArticleGroup.CREATE_MARGIN_BY_ARTICLE_GROUP_NOTE)
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public WssMarginArticleGroupDto create(OAuth2Authentication authed,
      @RequestBody WssMarginArticleGroupDto request) throws WssMarginArticleGroupException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return wssMarginArticleGroupService.create(request, user.getOrganisationId());
  }

  @ApiOperation(value = ApiDesc.MarginByArticleGroup.UPDATE_MARGIN_BY_ARTICLE_GROUP_DESC,
      notes = ApiDesc.MarginByArticleGroup.UPDATE_MARGIN_BY_ARTICLE_GROUP_NOTE)
  @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public WssMarginArticleGroupDto update(OAuth2Authentication authed,
      @RequestBody final WssMarginArticleGroupDto request) throws WssMarginArticleGroupException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return wssMarginArticleGroupService.update(request, user.getOrganisationId());
  }

  @ApiOperation(value = ApiDesc.MarginByArticleGroup.DELETE_MARGIN_BY_ARTICLE_GROUP_DESC,
      notes = ApiDesc.MarginByArticleGroup.DELETTE_MARGIN_BY_ARTICLE_GROUP_NOTE)
  @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  @HasWholesalerPreAuthorization
  public void delete(OAuth2Authentication authed, @PathVariable("id") final Integer id)
      throws WssMarginArticleGroupException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    wssMarginArticleGroupService.delete(user.getOrganisationId(), id);
  }

  @ApiOperation(value = ApiDesc.MarginByArticleGroup.SEARCH_MARGIN_BY_ARTICLE_GROUP_DESC,
      notes = ApiDesc.MarginByArticleGroup.SEARCH_MARGIN_BY_ARTICLE_GROUP_NOTE)
  @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public Page<WssMarginArticleGroupDto> search(OAuth2Authentication authed,
      @RequestBody final WssMarginArticleGroupSearchCriteria criteria,
      @PageableDefault final Pageable pageable) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    criteria.setOrgId(user.getOrganisationId());
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(
        wssMarginArticleGroupService.searchByCriteria(criteria, pageable));
  }

  @ApiOperation(value = ApiDesc.MarginByArticleGroup.SEARCH_MARGIN_BY_ARTICLE_GROUP_INDEX_DESC,
      notes = ApiDesc.MarginByArticleGroup.SEARCH_MARGIN_BY_ARTICLE_GROUP_INDEX_NOTE)
  @PostMapping(value = "/search-index", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public Page<WssArticleGroupDto> searchArticlesByFilter(final OAuth2Authentication authed,
      @RequestBody final WssArticleGroupSearchRequest request,
      @PageableDefault final Pageable pageable) {
    request.setPageRequest(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
    return ivdsWssArticleGroupService.searchWssArticleGroup(request);
  }

  @ApiOperation(value = ApiDesc.MarginByArticleGroup.FIND_DEFAULT_MARGIN_BY_ARTICLE_GROUP_DESC,
      notes = ApiDesc.MarginByArticleGroup.FIND_DEFAULT_MARGIN_BY_ARTICLE_GROUP_NOTE)
  @GetMapping(value = "/get-default", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public Optional<WssMarginArticleGroupDto> getDefaultWssMarginArticleGroup(
      final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return wssMarginArticleGroupService.findDefaultMarginArticleGroup(user.getOrganisationId());
  }

  @ApiOperation(value = ApiDesc.MarginByArticleGroup.SEARCH_ROOT_MARGIN_BY_ARTICLE_GROUP_DESC,
      notes = ApiDesc.MarginByArticleGroup.SEARCH_ROOT_MARGIN_BY_ARTICLE_GROUP_NOTE)
  @GetMapping(value = "/search-root", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public Page<WssMarginArticleGroupDto> searchRootWssMarginArticleGroup(OAuth2Authentication authed,

      @PageableDefault final Pageable pageable) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(wssMarginArticleGroupService
        .searchAllRootMarginArticleGroup(user.getOrganisationId(), pageable));
  }

  @ApiOperation(value = ApiDesc.MarginByArticleGroup.SEARCH_CHILD_MARGIN_BY_ARTICLE_GROUP_DESC,
      notes = ApiDesc.MarginByArticleGroup.SEARCH_CHILD_MARGIN_BY_ARTICLE_GROUP_NOTE)
  @GetMapping(value = "/search-child/{parentId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public List<WssMarginArticleGroupDto> searchChildWssMarginArticleGroup(OAuth2Authentication authed,
      @PathVariable("parentId") int parentId) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(wssMarginArticleGroupService
        .searchAllChildMarginArticleGroup(user.getOrganisationId(), parentId));
  }

  @ApiOperation(value = ApiDesc.MarginByArticleGroup.UPDATE_WSS_SHOW_NET_PRICE_SETTING_DESC,
      notes = ApiDesc.MarginByArticleGroup.UPDATE_WSS_SHOW_NET_PRICE_SETTING_NOTE)
  @PostMapping(value = "/update-show-net-price", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public Boolean updateShowNetPrice(final OAuth2Authentication authed,
      @RequestParam(required = true) final boolean wssShowNetPrice)
      throws WssMarginArticleGroupException, WssBrandMaginException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return wssMarginArticleGroupService.updateWssShowNetPriceSetting(wssShowNetPrice,
        user.getOrganisationId());
  }

  /**
   * Imports all record from csv file into DB
   *
   * @param file
   * @throws CsvServiceException
   * @throws WssMarginArticleGroupException
   */
  @ApiOperation(value = ApiDesc.MarginByArticleGroup.IMPORT_WSS_MARGIN_BY_ARTICLE_GROUP_DESC,
      notes = ApiDesc.MarginByArticleGroup.IMPORT_WSS_MARGIN_BY_ARTICLE_GROUP_NOTE)
  @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @HasWholesalerPreAuthorization
  public WssImportMarginArticleGroupResponseDto importFromCsv(OAuth2Authentication authed,
      @RequestParam("file") MultipartFile file)
      throws CsvServiceException, WssMarginArticleGroupException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final boolean useDefaultCharset = true;
    final List<WssCsvMarginArticleGroup> wssMarginArticleGroups =
        CsvUtils.read(file, WssCsvMarginArticleGroup.class, SagConstants.CSV_SEMICOLON_SEPARATOR, useDefaultCharset);
    if (CollectionUtils.isEmpty(wssMarginArticleGroups)) {
      throw new WssMarginArticleGroupException(WssMarginArticleGroupErrorCase.WMAGE_008,
          "The imported WSS margin by article group data is empty");
    }
    return wssMarginArticleGroupService.importWssMarginArticleGroup(wssMarginArticleGroups,
        user.getOrganisationId());
  }

  @GetMapping(value = "/export-mapped-csv")
  @ResponseBody
  @HasWholesalerPreAuthorization
  public ResponseEntity<byte[]> exportMappedCsv(OAuth2Authentication authed,
      @RequestParam(required = false) String languageCode) throws ServiceException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    if (StringUtils.isBlank(languageCode)) {
      languageCode = user.getLanguage();
    }
    return wssMarginArticleGroupService
        .exportMappedMarginArticleGroup(user.getOrganisationId(), languageCode)
        .buildResponseEntity();
  }

  @GetMapping(value = "/export-all-csv")
  @ResponseBody
  @HasWholesalerPreAuthorization
  public ResponseEntity<byte[]> exportAllCsv(OAuth2Authentication authed,
      @RequestParam(required = false) String languageCode) throws ServiceException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    if (StringUtils.isBlank(languageCode)) {
      languageCode = user.getLanguage();
    }
    return wssMarginArticleGroupService
        .exportAllMarginArticleGroup(user.getOrganisationId(), languageCode).buildResponseEntity();
  }

  @GetMapping(value = "/csv-template")
  @ResponseBody
  @HasWholesalerPreAuthorization
  public ResponseEntity<byte[]> getCsvTemplate(OAuth2Authentication authed,
      @RequestParam(required = false) String languageCode) throws ServiceException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    if (StringUtils.isBlank(languageCode)) {
      languageCode = user.getLanguage();
    }
    return wssMarginArticleGroupService.getCsvTemplate(languageCode).buildResponseEntity();
  }

  @ApiOperation(value = ApiDesc.MarginByArticleGroup.UNMAP_MARGIN_BY_ARTICLE_GROUP_DESC,
      notes = ApiDesc.MarginByArticleGroup.UNMAP_MARGIN_BY_ARTICLE_GROUP_NOTE)
  @PutMapping(value = "/unmap", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public Optional<WssMarginArticleGroupDto> unmapMarginArticleGroup(
      final OAuth2Authentication authed,
      @RequestParam(required = true) final Integer marginArticleGroupId)
      throws WssMarginArticleGroupException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return wssMarginArticleGroupService.unmapWssMarginArticleGroup(marginArticleGroupId,
        user.getOrganisationId());
  }
}
