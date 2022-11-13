package com.sagag.services.service.api.impl;

import com.google.common.collect.Lists;
import com.sagag.eshop.repo.api.WssMarginsBrandRepository;
import com.sagag.eshop.repo.entity.WssMarginsBrand;
import com.sagag.eshop.repo.specification.WssMarginBrandSpecifications;
import com.sagag.eshop.service.converter.WssMarginBrandConverters;
import com.sagag.eshop.service.dto.WssImportMarginBrandResponseDto;
import com.sagag.eshop.service.dto.WssMarginBrandDto;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.common.utils.SagValidationUtils;
import com.sagag.services.domain.eshop.criteria.WssMarginBrandSearchCriteria;
import com.sagag.services.domain.eshop.dto.WssCsvMarginBrand;
import com.sagag.services.elasticsearch.domain.SupplierTxt;
import com.sagag.services.hazelcast.api.SupplierCacheService;
import com.sagag.services.service.api.WssMarginBrandService;
import com.sagag.services.service.exception.WssBrandMaginException;
import com.sagag.services.service.exception.WssBrandMaginException.WssMarginBrandErrorCase;
import com.sagag.services.service.exporter.wss.WssMarginBrandCsvExporter;
import com.sagag.services.service.exporter.wss.WssMarginBrandExportItemDto;
import com.sagag.services.service.utils.WssMarginUtils;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;

@Service
@Transactional
public class WssMarginBrandServiceImpl implements WssMarginBrandService {

  @Autowired
  private SupplierCacheService supplierCacheService;

  @Autowired
  private WssMarginsBrandRepository wssMarginsBrandRepository;

  @Autowired
  private WssMarginBrandCsvExporter wssMarginBrandCsvExporter;

  @Override
  public WssMarginBrandDto create(WssMarginBrandDto wssMarginBrand, int orgId)
      throws WssBrandMaginException {
    validateInputMarginBrand(wssMarginBrand);

    if (!Objects.isNull(findExisingByName(orgId, wssMarginBrand.getName()))) {
      throw new WssBrandMaginException(WssMarginBrandErrorCase.WMBEC_001,
          "This brand with name " + wssMarginBrand.getName() + " is already existed");
    }

    WssMarginsBrand marginBrand = WssMarginBrandConverters.convertToEntity(wssMarginBrand);
    marginBrand.setOrgId(orgId);

    return WssMarginBrandConverters.convertToDto(wssMarginsBrandRepository.save(marginBrand));
  }

  @Override
  public WssMarginBrandDto update(WssMarginBrandDto wssMarginBrand, int orgId)
      throws WssBrandMaginException {
    validateInputMarginBrand(wssMarginBrand);

    Optional<WssMarginsBrand> existingOpt =
        wssMarginsBrandRepository.findOneById(wssMarginBrand.getId());
    if (!existingOpt.isPresent()) {
      throw new WssBrandMaginException(WssMarginBrandErrorCase.WMBEC_002,
          "This margin brand with name " + wssMarginBrand.getName() + " does not exist");
    }
    WssMarginsBrand targetWssTour = existingOpt.get();

    if (wssMarginBrand.getBrandId() != targetWssTour.getBrandId()
        && !wssMarginBrand.getName().equalsIgnoreCase(targetWssTour.getBrandName())) {
      throw new WssBrandMaginException(WssMarginBrandErrorCase.WMBEC_005,
          "Can not update brand for created margin brand");
    }
    WssMarginBrandConverters.updateToTargetProperties(wssMarginBrand, targetWssTour);

    return WssMarginBrandConverters.convertToDto(wssMarginsBrandRepository.save(targetWssTour));
  }

  @Override
  public void delete(Integer id) throws WssBrandMaginException {
    Assert.notNull(id, "Id must not be null");
    Optional<WssMarginsBrand> existingOpt = wssMarginsBrandRepository.findOneById(id);
    if (!existingOpt.isPresent()) {
      throw new WssBrandMaginException(WssMarginBrandErrorCase.WMBEC_002,
          "This margin brand with id " + id + " does not exist");
    }
    wssMarginsBrandRepository.delete(existingOpt.get());
  }

  @Override
  public Page<SupplierTxt> searchBrandByName(String brandNameWithWildCard, Pageable pageable) {
    return supplierCacheService.findLikeByName(brandNameWithWildCard, pageable);
  }

  @Override
  public Page<WssMarginBrandDto> searchByCriteria(WssMarginBrandSearchCriteria criteria,
      Pageable pageable) {
    Assert.notNull(criteria, "The given search criteria must not be null");

    final Specification<WssMarginsBrand> marginBrandSpec =
        WssMarginBrandSpecifications.searchByCriteria(criteria);

    final Page<WssMarginsBrand> marginBrand =
        wssMarginsBrandRepository.findAll(marginBrandSpec, pageable);

    return marginBrand.map(WssMarginBrandConverters::convertToDto);
  }

  @Override
  public Optional<WssMarginBrandDto> getWholeSalerDefaultMarginBrand(int orgId) {
    Optional<WssMarginBrandDto> defaultMarginBrandOpt = wssMarginsBrandRepository
        .findDefaultSettingByOrgId(orgId).map(WssMarginBrandConverters::convertToDto);
    if (!defaultMarginBrandOpt.isPresent()) {
      WssMarginsBrand defaultWssMarginBrand = initDefaultWssMarginBrand(orgId);
      defaultMarginBrandOpt = Optional.of(WssMarginBrandConverters
          .convertToDto(wssMarginsBrandRepository.save(defaultWssMarginBrand)));
    }
    return defaultMarginBrandOpt;
  }

  @Override
  public WssImportMarginBrandResponseDto importWssMarginBrand(
      List<WssCsvMarginBrand> wssMarginBrands, int orgId) throws WssBrandMaginException {
    int successImported = 0;
    final int totalItemToImport = wssMarginBrands.size();
    wssMarginBrands =
        wssMarginBrands.parallelStream().filter(validCsvMarginBrand()).collect(Collectors.toList());
    for (WssCsvMarginBrand wssCsvMarginBrand : wssMarginBrands) {
      Optional<WssMarginsBrand> marginBrandOpt =
          wssMarginsBrandRepository.findByBrandIdAndOrgId(wssCsvMarginBrand.getBrandId(), orgId);
      if (marginBrandOpt.isPresent()) {
        successImported =
            updateMarginsBrandByImport(wssCsvMarginBrand, marginBrandOpt.get(), successImported);
      } else {
        successImported = createNewMarginsBrandByImport(orgId, wssCsvMarginBrand, successImported);
      }
    }
    return WssImportMarginBrandResponseDto.builder().totalImported(successImported)
        .totalItemToImport(totalItemToImport).build();
  }

  private Predicate<? super WssCsvMarginBrand> validCsvMarginBrand() {
    return marginBrand -> marginBrand.getBrandId() != null
        && WssMarginUtils.validMarginValues(marginBrand);
  }

  @Override
  public ExportStreamedResult exportToCsvByCriteria(WssMarginBrandSearchCriteria criteria)
      throws ServiceException {
    Assert.notNull(criteria, "The given search criteria must not be null");

    final Specification<WssMarginsBrand> marginBrandSpec =
        WssMarginBrandSpecifications.searchByCriteria(criteria);

    final Page<WssMarginsBrand> marginBrands =
        wssMarginsBrandRepository.findAll(marginBrandSpec, Pageable.unpaged());
    List<WssMarginBrandExportItemDto> content = marginBrands.getContent().parallelStream()
        .map(marginBrand -> new WssMarginBrandExportItemDto(marginBrand))
        .collect(Collectors.toList());
    return wssMarginBrandCsvExporter.exportCsv(content);
  }

  @Override
  public ExportStreamedResult getCsvTemplate() throws ServiceException {
    return wssMarginBrandCsvExporter.exportCsv(Lists.newArrayList());
  }

  private int createNewMarginsBrandByImport(int orgId, WssCsvMarginBrand wssCsvMarginBrand,
      int successImported) throws WssBrandMaginException {
    String supplierId = String.valueOf(wssCsvMarginBrand.getBrandId());
    Map<String, String> supplierDocMap =
        supplierCacheService.searchSupplierNameByIds(Lists.newArrayList(supplierId));
    if (supplierDocMap.isEmpty() || !supplierDocMap.containsKey(supplierId)) {
      return successImported;
    }
    WssMarginBrandDto wssMarginBrandDto = WssMarginBrandDto.builder()
        .brandId(wssCsvMarginBrand.getBrandId()).name(supplierDocMap.get(supplierId)).orgId(orgId)
        .margin1(wssCsvMarginBrand.getMargin1()).margin2(wssCsvMarginBrand.getMargin2())
        .margin3(wssCsvMarginBrand.getMargin3()).margin4(wssCsvMarginBrand.getMargin4())
        .margin5(wssCsvMarginBrand.getMargin5()).margin6(wssCsvMarginBrand.getMargin6())
        .margin7(wssCsvMarginBrand.getMargin7()).build();
    create(wssMarginBrandDto, orgId);
    return successImported + 1;
  }

  private int updateMarginsBrandByImport(WssCsvMarginBrand wssCsvMarginBrand,
      WssMarginsBrand wssMarginsBrand, int successImport) {
    wssMarginsBrand.setMargin1(wssCsvMarginBrand.getMargin1());
    wssMarginsBrand.setMargin2(wssCsvMarginBrand.getMargin2());
    wssMarginsBrand.setMargin3(wssCsvMarginBrand.getMargin3());
    wssMarginsBrand.setMargin4(wssCsvMarginBrand.getMargin4());
    wssMarginsBrand.setMargin5(wssCsvMarginBrand.getMargin5());
    wssMarginsBrand.setMargin6(wssCsvMarginBrand.getMargin6());
    wssMarginsBrand.setMargin7(wssCsvMarginBrand.getMargin7());
    wssMarginsBrandRepository.save(wssMarginsBrand);
    return successImport + 1;
  }

  private WssMarginsBrand initDefaultWssMarginBrand(int orgId) {
    WssMarginsBrand defaultWssMarginBrand =
        WssMarginsBrand.builder().brandName("Default").isDefault(true).orgId(orgId).build();
    return defaultWssMarginBrand;
  }

  private void validateRequestBody(WssMarginBrandDto wssMarginBrand) throws WssBrandMaginException {
    Assert.notNull(wssMarginBrand, "Wss margin brand must not be null");
    final Optional<ConstraintViolation<WssMarginBrandDto>> violationOpt =
        SagValidationUtils.validateObjectAndReturnFirstError(wssMarginBrand);
    if (violationOpt.isPresent() || !WssMarginUtils.validMarginValues(wssMarginBrand)) {
      throw new WssBrandMaginException(WssMarginBrandErrorCase.WMBEC_003,
          "Invalid request body");
    }
    if (nonDefaultMarginBrandWithoutBrandId(wssMarginBrand)) {
      throw new WssBrandMaginException(WssMarginBrandErrorCase.WMBEC_003,
          "Non-default brand must have brand ID");
    }
  }

  private boolean nonDefaultMarginBrandWithoutBrandId(WssMarginBrandDto wssMarginBrand) {
    return !wssMarginBrand.isDefault() && wssMarginBrand.getBrandId() == null;
  }

  private void validateInputMarginBrand(WssMarginBrandDto wssMarginBrand)
      throws WssBrandMaginException {
    validateRequestBody(wssMarginBrand);

    if (!wssMarginBrand.isDefault()) {
      validateIndexBrand(wssMarginBrand);
    }
  }

  private void validateIndexBrand(WssMarginBrandDto wssMarginBrand) throws WssBrandMaginException {
    String brandIdStr = wssMarginBrand.getBrandId().toString();
    Map<String, SupplierTxt> supplierTxt =
        supplierCacheService.searchSupplierByIds(Arrays.asList(brandIdStr));
    if (MapUtils.isEmpty(supplierTxt) || !StringUtils
        .equalsIgnoreCase(supplierTxt.get(brandIdStr).getSuppname(), wssMarginBrand.getName())) {
      throw new WssBrandMaginException(WssMarginBrandErrorCase.WMBEC_004,
          "This brand (id,name) (" + wssMarginBrand.getId() + "," + wssMarginBrand.getName() + ") "
              + "does not index in ES");
    }
  }

  private WssMarginsBrand findExisingByName(int orgId, String brandName) {
    Optional<WssMarginsBrand> existingOpt =
        wssMarginsBrandRepository.findOneByOrgIdAndBrandName(orgId, brandName);
    return existingOpt.orElse(null);
  }

}
