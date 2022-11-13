package com.sagag.services.service.api.impl;

import com.google.common.collect.Lists;
import com.sagag.eshop.repo.api.CustomerSettingsRepository;
import com.sagag.eshop.repo.api.WssMarginsArticleGroupRepository;
import com.sagag.eshop.repo.api.WssMarginsBrandRepository;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.WssMarginsArticleGroup;
import com.sagag.eshop.repo.specification.WssMarginArticleGroupSpecifications;
import com.sagag.eshop.service.converter.WssMarginArticleGroupConverters;
import com.sagag.eshop.service.dto.WssImportMarginArticleGroupResponseDto;
import com.sagag.eshop.service.dto.WssMarginArticleGroupDto;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.common.utils.CsvUtils;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.common.utils.SagValidationUtils;
import com.sagag.services.domain.article.WssArtGrpTreeDto;
import com.sagag.services.domain.article.WssArticleGroupDocDto;
import com.sagag.services.domain.article.WssDesignationsDto;
import com.sagag.services.domain.eshop.criteria.WssMarginArticleGroupSearchCriteria;
import com.sagag.services.domain.eshop.dto.WssCsvMarginArticleGroup;
import com.sagag.services.elasticsearch.api.WssArticleGroupSearchService;
import com.sagag.services.ivds.api.IvdsWssArticleGroupService;
import com.sagag.services.service.api.WssMarginArticleGroupService;
import com.sagag.services.service.exception.WssBrandMaginException;
import com.sagag.services.service.exception.WssBrandMaginException.WssMarginBrandErrorCase;
import com.sagag.services.service.exception.WssMarginArticleGroupException;
import com.sagag.services.service.exception.WssMarginArticleGroupException.WssMarginArticleGroupErrorCase;
import com.sagag.services.service.exporter.wss.WssMarginArticleGroupCsvExporter;
import com.sagag.services.service.exporter.wss.WssMarginArticleGroupExportItemDto;
import com.sagag.services.service.utils.WssMarginUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;

@Service
@Slf4j
public class WssMarginArticleGroupServiceImpl implements WssMarginArticleGroupService {

  private static final String DEFAULT_MARGIN_ARTICLE_GROUP_DESC = "Default";

  private static final String DEFAULT_MARGIN_ARTICLE_GROUP = "Default";

  private static final int DEFAULT_WSS_MARGIN_ARTICLE_GROUP_LEVEL = 1;

  private static final int DEFAULT_MARGIN_GROUP_LEVEL = 0;

  private static final String[] DEFAULT_MARGIN_GROUP_DESC_LANG = {"de", "fr", "it", "en"};

  @Autowired
  private WssMarginsArticleGroupRepository wssMarginsArticleGroupRepo;

  @Autowired
  private IvdsWssArticleGroupService ivdsWssArticleGroupService;

  @Autowired
  private CustomerSettingsRepository customerSettingsRepo;

  @Autowired
  private WssMarginsBrandRepository wssMarginBrandRepo;

  @Autowired
  private WssMarginArticleGroupCsvExporter wssMarginArticleGroupCsvExporter;

  @Autowired
  private WssArticleGroupSearchService wssArtGroupSearchService;

  @Override
  @Transactional
  public WssMarginArticleGroupDto create(WssMarginArticleGroupDto wssMarginArticleGroup, int orgId)
      throws WssMarginArticleGroupException {
    validateRequestBody(wssMarginArticleGroup);
    if (wssMarginsArticleGroupRepo.checkMarginArticleGroupExist(orgId,
        wssMarginArticleGroup.getSagArticleGroup())) {
      throw new WssMarginArticleGroupException(WssMarginArticleGroupErrorCase.WMAGE_001,
          "This margin article group with sag article group id "
              + wssMarginArticleGroup.getSagArticleGroup() + " existed");
    }
    WssMarginsArticleGroup marginArticleGroup =
        WssMarginArticleGroupConverters.convertToEntity(wssMarginArticleGroup);
    marginArticleGroup.setOrgId(orgId);
    marginArticleGroup.setMapped(true);

    setParentWssMarginArticleGroupInfo(wssMarginArticleGroup, orgId, marginArticleGroup);

    return WssMarginArticleGroupConverters
        .convertToDto(wssMarginsArticleGroupRepo.save(marginArticleGroup));
  }

  private void setParentWssMarginArticleGroupInfo(WssMarginArticleGroupDto wssMarginArticleGroup,
      int orgId, WssMarginsArticleGroup marginArticleGroup) throws WssMarginArticleGroupException {
    Optional<WssMarginsArticleGroup> parentMarginArticleGroupOpt =
        findParentWssMarginArticleGroup(wssMarginArticleGroup.getParentLeafId(), orgId);
    if (parentMarginArticleGroupOpt.isPresent()) {
      WssMarginsArticleGroup parentMarginsArticleGroup = parentMarginArticleGroupOpt.get();
      marginArticleGroup.setParentId(parentMarginsArticleGroup.getId());
      marginArticleGroup.setGroupLevel(parentMarginsArticleGroup.getGroupLevel() + 1);
    } else {
      marginArticleGroup.setGroupLevel(DEFAULT_WSS_MARGIN_ARTICLE_GROUP_LEVEL);
    }
  }

  private Optional<WssMarginsArticleGroup> findParentWssMarginArticleGroup(String parentLeafId,
      int orgId) throws WssMarginArticleGroupException {
    if (StringUtils.isBlank(parentLeafId)) {
      return Optional.empty();
    }
    Optional<WssArticleGroupDocDto> indexWssArticleGroupOpt =
        ivdsWssArticleGroupService.findExactByArticleGroupLeafId(parentLeafId);
    if (!indexWssArticleGroupOpt.isPresent()) {
      return Optional.empty();
    }
    WssArticleGroupDocDto wssArticleGroupDocDto = indexWssArticleGroupOpt.get();
    WssArtGrpTreeDto wssArtGrpTreeDto = wssArticleGroupDocDto.getWssArtGrpTree().get(0);
    if (StringUtils.isBlank(wssArtGrpTreeDto.getParentid())) {
      // [Alle] node, should not exist
      return Optional.empty();
    }
    Optional<WssMarginsArticleGroup> parentWssMarginArticleGroupOpt = wssMarginsArticleGroupRepo
        .findWssMarginArticleGroupByArtGroup(orgId, wssArtGrpTreeDto.getArtgrp());
    if (parentWssMarginArticleGroupOpt.isPresent()) {
      return parentWssMarginArticleGroupOpt;
    } else {
      WssMarginsArticleGroup parentWssMarginArticleGroup =
          WssMarginsArticleGroup.builder().sagArtGroup(wssArtGrpTreeDto.getArtgrp())
              .sagArticleGroupDesc(
                  SagJSONUtil.convertObjectToJson(wssArticleGroupDocDto.getWssDesignations()))
              .leafId(wssArtGrpTreeDto.getLeafid()).parentLeafId(wssArtGrpTreeDto.getParentid())
              .orgId(orgId).build();
      Optional<WssMarginsArticleGroup> upperNode =
          findParentWssMarginArticleGroup(wssArtGrpTreeDto.getParentid(), orgId);
      if (upperNode.isPresent()) {
        WssMarginsArticleGroup upperMarginsArticleGroup = upperNode.get();
        parentWssMarginArticleGroup.setParentId(upperMarginsArticleGroup.getId());
        parentWssMarginArticleGroup.setGroupLevel(upperMarginsArticleGroup.getGroupLevel() + 1);
      } else {
        parentWssMarginArticleGroup.setGroupLevel(DEFAULT_WSS_MARGIN_ARTICLE_GROUP_LEVEL);
      }
      return Optional.of(wssMarginsArticleGroupRepo.save(parentWssMarginArticleGroup));
    }
  }

  @Override
  @Transactional
  public WssMarginArticleGroupDto update(WssMarginArticleGroupDto wssMarginArticleGroup, int orgId)
      throws WssMarginArticleGroupException {

    validateRequestBody(wssMarginArticleGroup);
    if (wssMarginArticleGroup.isDefault()) {
      return setDefaultMarginArticleGroup(wssMarginArticleGroup, orgId);
    }
    Optional<WssMarginsArticleGroup> targetWssMarginArticleGroupOpt =
        wssMarginsArticleGroupRepo.findMarginArticleGroupByOrgIdAndSagArtGroupId(orgId,
            wssMarginArticleGroup.getSagArticleGroup());
    if (!targetWssMarginArticleGroupOpt.isPresent()) {
      throw new WssMarginArticleGroupException(WssMarginArticleGroupErrorCase.WMAGE_002,
          "This margin article group with sag article group id "
              + wssMarginArticleGroup.getSagArticleGroup() + " does not exist");
    }
    WssMarginsArticleGroup targetWssMarginArticleGroup = targetWssMarginArticleGroupOpt.get();
    WssMarginArticleGroupConverters.updateToTargetProperties(wssMarginArticleGroup,
        targetWssMarginArticleGroup);

    return WssMarginArticleGroupConverters
        .convertToDto(wssMarginsArticleGroupRepo.save(targetWssMarginArticleGroup));
  }

  private WssMarginArticleGroupDto setDefaultMarginArticleGroup(
      WssMarginArticleGroupDto wssMarginArticleGroup, int orgId) {
    Optional<WssMarginsArticleGroup> defautlMarginArticleGroupOpt =
        wssMarginsArticleGroupRepo.findDefaultWssMarginArticleGroup(orgId);
    WssMarginsArticleGroup defaultMarginArticleGroup =
        WssMarginArticleGroupConverters.convertToEntity(wssMarginArticleGroup);
    if (defautlMarginArticleGroupOpt.isPresent()) {
      defaultMarginArticleGroup = defautlMarginArticleGroupOpt.get();
      WssMarginArticleGroupConverters.updateToTargetProperties(wssMarginArticleGroup,
          defaultMarginArticleGroup);
      return WssMarginArticleGroupConverters
          .convertToDto(wssMarginsArticleGroupRepo.save(defaultMarginArticleGroup));
    }

    defaultMarginArticleGroup.setOrgId(orgId);
    defaultMarginArticleGroup.setMapped(true);
    defaultMarginArticleGroup.setGroupLevel(DEFAULT_MARGIN_GROUP_LEVEL);
    return WssMarginArticleGroupConverters
        .convertToDto(wssMarginsArticleGroupRepo.save(defaultMarginArticleGroup));
  }

  @Override
  @Transactional
  public void delete(int orgId, Integer id) throws WssMarginArticleGroupException {
    Assert.notNull(id, "Id must not be null");
    Optional<WssMarginsArticleGroup> existingOpt = wssMarginsArticleGroupRepo.findOneById(id);
    if (!existingOpt.isPresent()) {
      throw new WssMarginArticleGroupException(WssMarginArticleGroupErrorCase.WMAGE_002,
          "This margin article group with id " + id + " does not exist");
    }
    WssMarginsArticleGroup wssMarginsArticleGroup = existingOpt.get();
    checkMarginArticleGroupBelongToCustomer(orgId, wssMarginsArticleGroup);
    if (wssMarginsArticleGroupRepo.checkMarginArticleGroupHasChild(
        wssMarginsArticleGroup.getOrgId(), wssMarginsArticleGroup.getId())) {
      clearMarginArticleGroup(wssMarginsArticleGroup);
      wssMarginsArticleGroupRepo.save(wssMarginsArticleGroup);
    } else {
      wssMarginsArticleGroupRepo.delete(wssMarginsArticleGroup);
    }
  }

  @Override
  public Page<WssMarginArticleGroupDto> searchByCriteria(
      WssMarginArticleGroupSearchCriteria criteria, Pageable pageable) {
    Assert.notNull(criteria, "The given search criteria must not be null");

    final Specification<WssMarginsArticleGroup> marginArticleGroupSpec =
        WssMarginArticleGroupSpecifications.searchByCriteria(criteria);

    final Page<WssMarginsArticleGroup> marginArticleGroups =
        wssMarginsArticleGroupRepo.findAll(marginArticleGroupSpec, pageable);

    return marginArticleGroups.map(WssMarginArticleGroupConverters::convertToDto);
  }

  @Override
  public Optional<WssMarginArticleGroupDto> findDefaultMarginArticleGroup(int orgId) {
    Optional<WssMarginArticleGroupDto> defaultMarginArticleGroupOpt =
        wssMarginsArticleGroupRepo.findDefaultWssMarginArticleGroup(orgId)
            .map(WssMarginArticleGroupConverters::convertToDto);
    if (!defaultMarginArticleGroupOpt.isPresent()) {
      defaultMarginArticleGroupOpt = Optional.of(WssMarginArticleGroupConverters.convertToDto(
          wssMarginsArticleGroupRepo.save(initDefaultWssMarginArticleGroup(orgId))));
    }
    return defaultMarginArticleGroupOpt;
  }

  private WssMarginsArticleGroup initDefaultWssMarginArticleGroup(int orgId) {
    return WssMarginsArticleGroup.builder().orgId(orgId).isDefault(true).isMapped(false)
        .sagArtGroup(DEFAULT_MARGIN_ARTICLE_GROUP)
        .sagArticleGroupDesc(initDefaultMarginArticleGroupDesc())
        .groupLevel(DEFAULT_MARGIN_GROUP_LEVEL).build();
  }

  private String initDefaultMarginArticleGroupDesc() {
    List<WssDesignationsDto> defaultMarginArticleGroupDesc = new ArrayList<>();
    Arrays.stream(DEFAULT_MARGIN_GROUP_DESC_LANG).forEach(lang -> defaultMarginArticleGroupDesc
        .add(new WssDesignationsDto(lang, DEFAULT_MARGIN_ARTICLE_GROUP_DESC)));
    return SagJSONUtil.convertObjectToJson(defaultMarginArticleGroupDesc);

  }

  private void validateRequestBody(WssMarginArticleGroupDto wssMarginArticleGroup)
      throws WssMarginArticleGroupException {
    Assert.notNull(wssMarginArticleGroup, "Wss margin article group must not be null");
    final Optional<ConstraintViolation<WssMarginArticleGroupDto>> violationOpt =
        SagValidationUtils.validateObjectAndReturnFirstError(wssMarginArticleGroup);
    if (violationOpt.isPresent() || !WssMarginUtils.validMarginValues(wssMarginArticleGroup)) {
      throw new WssMarginArticleGroupException(WssMarginArticleGroupErrorCase.WMAGE_003,
          "Invalid request body");
    }
  }

  @Override
  @Transactional
  public Page<WssMarginArticleGroupDto> searchAllRootMarginArticleGroup(int orgId,
      Pageable pageable) {

    final Page<WssMarginsArticleGroup> marginArticleGroups =
        wssMarginsArticleGroupRepo.findAllRootMarginArticleGroup(orgId, pageable);

    return marginArticleGroups.map(WssMarginArticleGroupConverters::convertToDto);
  }

  @Override
  @Transactional
  public List<WssMarginArticleGroupDto> searchAllChildMarginArticleGroup(int orgId, int parentId) {

    final List<WssMarginsArticleGroup> marginArticleGroups =
        wssMarginsArticleGroupRepo.findAllChildMarginArticleGroupByParentId(orgId, parentId);

    return marginArticleGroups.stream().map(WssMarginArticleGroupConverters::convertToDto)
        .collect(Collectors.toList());
  }

  @Override
  public boolean updateWssShowNetPriceSetting(final boolean isWssShowNetPrice, int orgId)
      throws WssMarginArticleGroupException, WssBrandMaginException {
    CustomerSettings customerSetting = customerSettingsRepo.findSettingsByOrgId(orgId);
    if (customerSetting == null) {
      throw new ValidationException(
          String.format("Customer setting of orgId=%d is not found", orgId));
    }
    if (!wssMarginsArticleGroupRepo.checkDefaultMarginArticleGroupExist(orgId)) {
      throw new WssMarginArticleGroupException(WssMarginArticleGroupErrorCase.WMAGE_006,
          "Default margin article group of org id  " + orgId + " not setup");
    }
    if (!wssMarginBrandRepo.checkDefaultMarginBrandExist(orgId)) {
      throw new WssBrandMaginException(WssMarginBrandErrorCase.WMBEC_006,
          "Default margin brand of org id  " + orgId + " not setup");
    }
    customerSetting.setWssShowNetPrice(isWssShowNetPrice);
    customerSettingsRepo.save(customerSetting);
    return isWssShowNetPrice;
  }

  @Override
  @Transactional
  public WssImportMarginArticleGroupResponseDto importWssMarginArticleGroup(
      List<WssCsvMarginArticleGroup> marginArticleGroups, int orgId)
      throws WssMarginArticleGroupException {
    int successImported = 0;
    final int totalItemToImport = marginArticleGroups.size();
    marginArticleGroups = marginArticleGroups.parallelStream()
        .filter(validCsvMarginArticleGroupItem())
        .collect(Collectors.toList());
    for (WssCsvMarginArticleGroup csvMarginArticleGroup : marginArticleGroups) {
      Optional<WssMarginsArticleGroup> wssMarginArticleGroupOpt =
          wssMarginsArticleGroupRepo.findBySagArtGroupAndOrgId(
              CsvUtils.unwrapExcelText(csvMarginArticleGroup.getSagArtGroup()), orgId);
      if (wssMarginArticleGroupOpt.isPresent()) {
        successImported = updateCurrentMarginArticleGroupByCsvItem(csvMarginArticleGroup,
            wssMarginArticleGroupOpt.get(), successImported);
      } else {
        successImported =
            createNewMarginArticleGroupByCsvImport(orgId, csvMarginArticleGroup, successImported);
      }
    }
    return WssImportMarginArticleGroupResponseDto.builder().totalImported(successImported)
        .totalItemToImport(totalItemToImport).build();
  }

  @Override
  public ExportStreamedResult getCsvTemplate(String userLangCode) throws ServiceException {
    ArrayList<WssMarginArticleGroupExportItemDto> exportDtos =
        getCsvTemplateCsvExportItem(userLangCode);

    return wssMarginArticleGroupCsvExporter.exportCsv(exportDtos);
  }

  private ArrayList<WssMarginArticleGroupExportItemDto> getCsvTemplateCsvExportItem(
      String userLangCode) {
    List<WssMarginArticleGroupExportItemDto> allWssArticleGroupExportItem =
            wssArtGroupSearchService.getAll().stream()
            .map(articleGroupDoc -> new WssMarginArticleGroupExportItemDto(articleGroupDoc,
                userLangCode))
            .collect(Collectors.toList());
    allWssArticleGroupExportItem
        .removeIf(articleGroup -> StringUtils.isBlank(articleGroup.getParentid()));
    return reOrderWssArticleGroup(allWssArticleGroupExportItem);
  }

  @Override
  public ExportStreamedResult exportMappedMarginArticleGroup(int orgId, String userLangCode)
      throws ServiceException {
    ArrayList<WssMarginArticleGroupExportItemDto> exportDtos =
        findAllMarginArticleGroupToExport(orgId, userLangCode);

    exportDtos.removeIf(marginArticleGroup -> !marginArticleGroup.isMapped());
    return wssMarginArticleGroupCsvExporter.exportCsv(exportDtos);
  }

  @Override
  public ExportStreamedResult exportAllMarginArticleGroup(int orgId, String userLangCode)
      throws ServiceException {
    ArrayList<WssMarginArticleGroupExportItemDto> exportDtos =
        findAllMarginArticleGroupToExport(orgId, userLangCode);

    return wssMarginArticleGroupCsvExporter
        .exportCsv(mergeWssMarginsWithAllArticleGroup(exportDtos, userLangCode));
  }

  private List<WssMarginArticleGroupExportItemDto> mergeWssMarginsWithAllArticleGroup(
      List<WssMarginArticleGroupExportItemDto> wssMargins, String userLangCode) {
    final ArrayList<WssMarginArticleGroupExportItemDto> csvTemplateCsvExportItems =
        getCsvTemplateCsvExportItem(userLangCode);
    if (CollectionUtils.isEmpty(wssMargins)) {
      return csvTemplateCsvExportItems;
    }
    if (CollectionUtils.isEmpty(csvTemplateCsvExportItems)) {
      return wssMargins;
    }
    final Map<String, WssMarginArticleGroupExportItemDto> mappedArticleGroups =
        wssMargins.stream().filter(margin -> margin.isMapped()).collect(Collectors
            .toMap(WssMarginArticleGroupExportItemDto::getSagArticleGroup, margin -> margin));

    csvTemplateCsvExportItems.forEach(articleGroup -> {
      if (mappedArticleGroups.containsKey(articleGroup.getSagArticleGroup())) {
        SagBeanUtils.copyProperties(mappedArticleGroups.get(articleGroup.getSagArticleGroup()),
            articleGroup);
      }
    });

    return csvTemplateCsvExportItems;
  }

  private ArrayList<WssMarginArticleGroupExportItemDto> findAllMarginArticleGroupToExport(int orgId,
      String userLangCode) {
    List<WssMarginArticleGroupExportItemDto> allWssArticleGroupExportItem =
        wssMarginsArticleGroupRepo.findAllMarginArticleGroupExceptDefault(orgId).stream()
            .map(articleGroupDoc -> new WssMarginArticleGroupExportItemDto(articleGroupDoc,
                userLangCode))
            .collect(Collectors.toList());
    return reOrderWssArticleGroup(allWssArticleGroupExportItem);
  }

  private ArrayList<WssMarginArticleGroupExportItemDto> reOrderWssArticleGroup(
      List<WssMarginArticleGroupExportItemDto> allWssArticleGroupExportItem) {
    Set<String> allLeafIds = allWssArticleGroupExportItem.parallelStream()
        .map(WssMarginArticleGroupExportItemDto::getLeafid).collect(Collectors.toSet());
    List<WssMarginArticleGroupExportItemDto> rootArticleGroups =
        allWssArticleGroupExportItem.parallelStream()
            .filter(articleGroupDoc -> !allLeafIds.contains(articleGroupDoc.getParentid()))
            .sorted(Comparator.comparing(WssMarginArticleGroupExportItemDto::getSagArticleGroup))
            .collect(Collectors.toList());
    ArrayList<WssMarginArticleGroupExportItemDto> exportDtos = Lists.newArrayList();
    for (WssMarginArticleGroupExportItemDto wssMarginArticleGroupExportItemDto : rootArticleGroups) {
      appendChildArticleGroup(exportDtos, wssMarginArticleGroupExportItemDto,
          allWssArticleGroupExportItem);
    }
    return exportDtos;
  }

  private void appendChildArticleGroup(List<WssMarginArticleGroupExportItemDto> exportDtos,
      WssMarginArticleGroupExportItemDto currentParentArticleGroup,
      List<WssMarginArticleGroupExportItemDto> allArticleGroups) {
    if (CollectionUtils.isEmpty(allArticleGroups)) {
      return;
    }
    exportDtos.add(currentParentArticleGroup);
    List<WssMarginArticleGroupExportItemDto> childArticleGroups = allArticleGroups.parallelStream()
        .filter(articleGroupDoc -> StringUtils.equalsIgnoreCase(articleGroupDoc.getParentid(),
            currentParentArticleGroup.getLeafid()))
        .sorted(Comparator.comparing(WssMarginArticleGroupExportItemDto::getSagArticleGroup))
        .collect(Collectors.toList());
    if (CollectionUtils.isEmpty(childArticleGroups)) {
      return;
    }
    for (WssMarginArticleGroupExportItemDto wssMarginArticleGroupExportItemDto : childArticleGroups) {
      appendChildArticleGroup(exportDtos, wssMarginArticleGroupExportItemDto, allArticleGroups);
    }
  }

  @Override
  public Optional<WssMarginArticleGroupDto> unmapWssMarginArticleGroup(
      final Integer marginArticleGroupId, int orgId) throws WssMarginArticleGroupException {
    Optional<WssMarginsArticleGroup> wssMarginArticleGroupOpt =
        wssMarginsArticleGroupRepo.findById(marginArticleGroupId);
    if (!wssMarginArticleGroupOpt.isPresent()) {
      return Optional.empty();
    }
    WssMarginsArticleGroup wssMarginsArticleGroup = wssMarginArticleGroupOpt.get();
    checkMarginArticleGroupBelongToCustomer(orgId, wssMarginsArticleGroup);
    clearMarginArticleGroup(wssMarginsArticleGroup);
    return Optional.of(WssMarginArticleGroupConverters
        .convertToDto(wssMarginsArticleGroupRepo.save(wssMarginsArticleGroup)));
  }

  private int createNewMarginArticleGroupByCsvImport(int orgId,
      WssCsvMarginArticleGroup csvMarginArticleGroup, int successImported) {
    final String unwrappedSagArticleGroup =
        CsvUtils.unwrapExcelText(csvMarginArticleGroup.getSagArtGroup());
    Optional<WssArticleGroupDocDto> wssArticleGroupDocDtoOpt = ivdsWssArticleGroupService
        .findExactByArticleGroupId(unwrappedSagArticleGroup);
    if (!wssArticleGroupDocDtoOpt.isPresent()) {
      return successImported;
    }

    WssArticleGroupDocDto wssArticleGroupDocDto = wssArticleGroupDocDtoOpt.get();
    WssArtGrpTreeDto wssArtGrpTreeDto = wssArticleGroupDocDto.getWssArtGrpTree().get(0);
    WssMarginArticleGroupDto wssMarginArticleGroupDto = WssMarginArticleGroupDto.builder()
        .sagArticleGroup(unwrappedSagArticleGroup)
        .sagArticleGroupDesc(wssArticleGroupDocDto.getWssDesignations())
        .customArticleGroup(CsvUtils.unwrapExcelText(csvMarginArticleGroup.getCustomArticleGroup()))
        .customArticleGroupDesc(CsvUtils.unwrapExcelText(csvMarginArticleGroup
            .getCustomArticleGroupDesc()))
        .margin1(csvMarginArticleGroup.getMargin1()).margin2(csvMarginArticleGroup.getMargin2())
        .margin3(csvMarginArticleGroup.getMargin3()).margin4(csvMarginArticleGroup.getMargin4())
        .margin5(csvMarginArticleGroup.getMargin5()).margin6(csvMarginArticleGroup.getMargin6())
        .margin7(csvMarginArticleGroup.getMargin7()).parentLeafId(wssArtGrpTreeDto.getParentid())
        .leafId(wssArtGrpTreeDto.getLeafid()).build();
    try {
      create(wssMarginArticleGroupDto, orgId);
      return successImported + 1;
    } catch (WssMarginArticleGroupException e) {
      log.error(
          "Import failed for WSS Margin Article Group: " + csvMarginArticleGroup.getSagArtGroup());
    }
    return successImported;
  }

  private int updateCurrentMarginArticleGroupByCsvItem(
      WssCsvMarginArticleGroup csvMarginArticleGroup,
      WssMarginsArticleGroup wssMarginsArticleGroup, int successImported) {
    wssMarginsArticleGroup.setCustomArticleGroup(
        CsvUtils.unwrapExcelText(csvMarginArticleGroup.getCustomArticleGroup()));
    wssMarginsArticleGroup.setCustomArticleGroupDesc(
        CsvUtils.unwrapExcelText(csvMarginArticleGroup.getCustomArticleGroupDesc()));
    wssMarginsArticleGroup.setMargin1(csvMarginArticleGroup.getMargin1());
    wssMarginsArticleGroup.setMargin2(csvMarginArticleGroup.getMargin2());
    wssMarginsArticleGroup.setMargin3(csvMarginArticleGroup.getMargin3());
    wssMarginsArticleGroup.setMargin4(csvMarginArticleGroup.getMargin4());
    wssMarginsArticleGroup.setMargin5(csvMarginArticleGroup.getMargin5());
    wssMarginsArticleGroup.setMargin6(csvMarginArticleGroup.getMargin6());
    wssMarginsArticleGroup.setMargin7(csvMarginArticleGroup.getMargin7());
    wssMarginsArticleGroup.setMapped(true);
    wssMarginsArticleGroupRepo.save(wssMarginsArticleGroup);
    return successImported + 1;
  }

  private void clearMarginArticleGroup(WssMarginsArticleGroup wssMarginsArticleGroup) {
    wssMarginsArticleGroup.setCustomArticleGroup(null);
    wssMarginsArticleGroup.setCustomArticleGroupDesc(null);
    wssMarginsArticleGroup.setMargin1(null);
    wssMarginsArticleGroup.setMargin2(null);
    wssMarginsArticleGroup.setMargin3(null);
    wssMarginsArticleGroup.setMargin4(null);
    wssMarginsArticleGroup.setMargin5(null);
    wssMarginsArticleGroup.setMargin6(null);
    wssMarginsArticleGroup.setMargin7(null);
    wssMarginsArticleGroup.setMapped(false);
  }

  private void checkMarginArticleGroupBelongToCustomer(int orgId,
      WssMarginsArticleGroup wssMarginsArticleGroup) throws WssMarginArticleGroupException {
    if (wssMarginsArticleGroup.getOrgId() != orgId) {
      throw new WssMarginArticleGroupException(WssMarginArticleGroupErrorCase.WMAGE_007,
          "This margin article group with id " + wssMarginsArticleGroup.getId()
              + " does not belong to your org id " + orgId);
    }
  }

  private Predicate<? super WssCsvMarginArticleGroup> validCsvMarginArticleGroupItem() {
    return marginArticleGroup -> StringUtils.isNotBlank(marginArticleGroup.getSagArtGroup())
        && StringUtils.isNotBlank(marginArticleGroup.getCustomArticleGroup())
        && StringUtils.isNotBlank(marginArticleGroup.getCustomArticleGroupDesc())
        && WssMarginUtils.validMarginValues(marginArticleGroup);
  }

}
