package com.sagag.services.admin.controller;

import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.exception.OrganisationCollectionException;
import com.sagag.services.admin.swagger.docs.ApiDesc;
import com.sagag.services.admin.swagger.docs.ApiDesc.OrganisationCollection;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.eshop.backoffice.dto.CollectionSearchResultDto;
import com.sagag.services.domain.eshop.criteria.CollectionSearchCriteria;
import com.sagag.services.domain.eshop.dto.collection.OrganisationCollectionDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/collections", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "admin Collection APIs")
public class AdminCollectionController {

  @Autowired
  private OrganisationCollectionService organisationCollectionService;

  @ApiOperation(value = OrganisationCollection.GET_COLLECTION_TEMPLATE_DESC,
      notes = OrganisationCollection.GET_COLLECTION_TEMPLATE_NOTE)
  @GetMapping(value = "/template/{affiliateShortName}")
  public OrganisationCollectionDto getTemplate(
      @PathVariable("affiliateShortName") final String affiliateShortName) {
    return organisationCollectionService.getCollectionTemplate(affiliateShortName);
  }

  @ApiOperation(value = OrganisationCollection.GET_COLLECTION_DESC,
      notes = OrganisationCollection.GET_COLLECTION_NOTE)
  @GetMapping(value = "/{collectionShortName}")
  public OrganisationCollectionDto getCollectionInfo(
      @PathVariable("collectionShortName") final String collectionShortName)
          throws OrganisationCollectionException {
    return organisationCollectionService.getCollectionInfo(collectionShortName);
  }

  @ApiOperation(value = OrganisationCollection.CREATE_COLLECTION_DESC,
      notes = OrganisationCollection.CREATE_COLLECTION_NOTE)
  @PostMapping(value = "/create")
  public void create(@RequestBody final OrganisationCollectionDto request)
      throws OrganisationCollectionException {
    organisationCollectionService.create(request);
  }

  @ApiOperation(value = OrganisationCollection.UPDATE_COLLECTION_DESC,
      notes = OrganisationCollection.UPDATE_COLLECTION_NOTE)
  @PostMapping(value = "/update")
  public void update(@RequestBody final OrganisationCollectionDto request)
      throws OrganisationCollectionException {
    organisationCollectionService.update(request);
  }

  @ApiOperation(value = ApiDesc.OrganisationCollection.B0_SEARCH_COLLECTION,
      notes = ApiDesc.OrganisationCollection.B0_SEARCH_COLLECTION_NOTES)
  @PostMapping(value = "/search")
  public Page<CollectionSearchResultDto> searchCollection(
      @RequestBody final CollectionSearchCriteria searchCriteria) throws ResultNotFoundException {
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(
        organisationCollectionService.searchCollection(searchCriteria));
  }

  @ApiOperation(value = ApiDesc.OrganisationCollection.B0_GET_CUSTOMER,
      notes = ApiDesc.OrganisationCollection.B0_GET_CUSTOMER_NOTES)
  @GetMapping(value = "/{collectionShortName}/customers")
  public Page<String> getAllCustomerNr(@PathVariable String collectionShortName,
      final @PageableDefault Pageable pageable) throws ResultNotFoundException {
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(
        organisationCollectionService.getAllCustomerNr(collectionShortName, pageable));
  }
}
