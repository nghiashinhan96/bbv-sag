package com.sagag.services.rest.controller.finalcustomer;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.WssImportMarginBrandResponseDto;
import com.sagag.eshop.service.dto.WssMarginBrandDto;
import com.sagag.eshop.service.exception.WssBranchValidationException;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.exception.CsvServiceException;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.utils.CsvUtils;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.eshop.criteria.WssMarginBrandSearchCriteria;
import com.sagag.services.domain.eshop.dto.WssCsvMarginBrand;
import com.sagag.services.elasticsearch.domain.SupplierTxt;
import com.sagag.services.rest.authorization.annotation.HasWholesalerPreAuthorization;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.WssMarginBrandService;
import com.sagag.services.service.exception.WssBrandMaginException;
import com.sagag.services.service.exception.WssBrandMaginException.WssMarginBrandErrorCase;
import com.sagag.services.service.exception.WssMarginArticleGroupException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

/**
 * The controller exposes API for WSS margin setting by brand.
 *
 */
@RestController
@RequestMapping("/wss/margin-by-brand")
@Api(tags = "WSS Margin by brand")
public class WssMarginBrandController {

  @Autowired
  private WssMarginBrandService wssMarginBrandService;

  @ApiOperation(value = ApiDesc.MarginByBrand.CREATE_MARGIN_BY_BRAND_DESC,
      notes = ApiDesc.MarginByBrand.CREATE_MARGIN_BY_BRAND_NOTE)
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public WssMarginBrandDto create(OAuth2Authentication authed,
      @RequestBody final WssMarginBrandDto request) throws WssBrandMaginException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return wssMarginBrandService.create(request, user.getOrganisationId());
  }

  @ApiOperation(value = ApiDesc.MarginByBrand.UPDATE_MARGIN_BY_BRAND_DESC,
      notes = ApiDesc.MarginByBrand.UPDATE_MARGIN_BY_BRAND_NOTE)
  @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public WssMarginBrandDto update(OAuth2Authentication authed,
      @RequestBody final WssMarginBrandDto request) throws WssBrandMaginException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return wssMarginBrandService.update(request, user.getOrganisationId());
  }

  @ApiOperation(value = ApiDesc.MarginByBrand.DELETE_MARGIN_BY_BRAND_DESC,
      notes = ApiDesc.MarginByBrand.DELETTE_MARGIN_BY_BRAND_NOTE)
  @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  @HasWholesalerPreAuthorization
  public void delete(OAuth2Authentication authed, @PathVariable("id") final Integer id)
      throws WssBranchValidationException, WssBrandMaginException {
    wssMarginBrandService.delete(id);
  }

  @ApiOperation(value = ApiDesc.MarginByBrand.SEARCH_BRAND_BY_NAME,
      notes = ApiDesc.MarginByBrand.SEARCH_BRAND_BY_NAME_NOTE)
  @GetMapping(value = "/brand/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public Page<SupplierTxt> searchBrand(OAuth2Authentication authed,
      @RequestParam(name = "brandName", required = false) final String brandName,
      @PageableDefault final Pageable pageable) {
    return wssMarginBrandService.searchBrandByName(brandName, pageable);
  }

  @ApiOperation(value = ApiDesc.MarginByBrand.SEARCH_MARGIN_BY_BRAND_DESC,
      notes = ApiDesc.MarginByBrand.SEARCH_MARGIN_BY_BRAND_NOTE)
  @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public Page<WssMarginBrandDto> search(OAuth2Authentication authed,
      @RequestBody final WssMarginBrandSearchCriteria criteria,
      @PageableDefault final Pageable pageable) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    criteria.setOrgId(user.getOrganisationId());
    return RestExceptionUtils
        .doSafelyReturnNotEmptyRecords(wssMarginBrandService.searchByCriteria(criteria, pageable));
  }

  @ApiOperation(value = ApiDesc.MarginByBrand.GET_DEFAULT_MARGIN_BY_BRAND_DESC,
      notes = ApiDesc.MarginByBrand.GET_DEFAULT_MARGIN_BY_BRAND_NOTE)
  @GetMapping(value = "/default", produces = MediaType.APPLICATION_JSON_VALUE)
  @HasWholesalerPreAuthorization
  public WssMarginBrandDto getWholeSalerDefaultMarginBrand(OAuth2Authentication authed)
      throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return RestExceptionUtils.doSafelyReturnOptionalRecord(
        wssMarginBrandService.getWholeSalerDefaultMarginBrand(user.getOrganisationId()));
  }

  /**
   * Imports all record from csv file into DB
   *
   * @param file
   * @throws CsvServiceException
   * @throws WssMarginArticleGroupException
   * @throws WssBrandMaginException
   */
  @ApiOperation(value = ApiDesc.MarginByBrand.IMPORT_WSS_MARGIN_BY_BRAND_DESC,
      notes = ApiDesc.MarginByBrand.IMPORT_WSS_MARGIN_BY_BRAND_NOTE)
  @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @HasWholesalerPreAuthorization
  public WssImportMarginBrandResponseDto importFromCsv(OAuth2Authentication authed,
      @RequestParam("file") MultipartFile file)
      throws CsvServiceException, WssBrandMaginException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    boolean useDefaultCharset = false;
    final List<WssCsvMarginBrand> wssMarginBrands =
        CsvUtils.read(file, WssCsvMarginBrand.class, SagConstants.CSV_SEMICOLON_SEPARATOR, useDefaultCharset);
    if (CollectionUtils.isEmpty(wssMarginBrands)) {
      throw new WssBrandMaginException(WssMarginBrandErrorCase.WMBEC_007,
          "The imported WSS margin by brand data is empty");
    }
    return wssMarginBrandService.importWssMarginBrand(wssMarginBrands, user.getOrganisationId());
  }

  @PostMapping(value = "/export-csv")
  @ResponseBody
  @HasWholesalerPreAuthorization
  public ResponseEntity<byte[]> exportCsv(OAuth2Authentication authed,
      @RequestBody final WssMarginBrandSearchCriteria criteria) throws ServiceException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    criteria.setOrgId(user.getOrganisationId());
    return wssMarginBrandService.exportToCsvByCriteria(criteria).buildResponseEntity();
  }

  @GetMapping(value = "/csv-template")
  @ResponseBody
  @HasWholesalerPreAuthorization
  public ResponseEntity<byte[]> getCsvTemplate() throws ServiceException {
    return wssMarginBrandService.getCsvTemplate().buildResponseEntity();
  }

}
