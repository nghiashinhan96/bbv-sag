package com.sagag.services.service.api;

import com.sagag.eshop.service.dto.WssImportMarginArticleGroupResponseDto;
import com.sagag.eshop.service.dto.WssMarginArticleGroupDto;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.domain.eshop.criteria.WssMarginArticleGroupSearchCriteria;
import com.sagag.services.domain.eshop.dto.WssCsvMarginArticleGroup;
import com.sagag.services.service.exception.WssBrandMaginException;
import com.sagag.services.service.exception.WssMarginArticleGroupException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface WssMarginArticleGroupService {

  /**
   * Create new WSS Margin Article Group
   *
   * @param wssMarginArticleGroup
   * @param orgId
   * @return the object of {@link WssMarginArticleGroupDto}
   * @throws WssMarginArticleGroupException
   */
  WssMarginArticleGroupDto create(WssMarginArticleGroupDto wssMarginArticleGroup, int orgId)
      throws WssMarginArticleGroupException;

  /**
   * @param wssMarginArticleGroup
   * @param orgId
   * @return the object of {@link WssMarginArticleGroupDto}
   * @throws WssMarginArticleGroupException
   */
  WssMarginArticleGroupDto update(WssMarginArticleGroupDto wssMarginArticleGroup, int orgId)
      throws WssMarginArticleGroupException;

  /**
   * Delete WSS Margin Article Group
   *
   * @param orgId
   * @param id
   * @throws WssMarginArticleGroupException
   */
  void delete(int orgId, Integer id) throws WssMarginArticleGroupException;

  /**
   * Search WSS Margin Article Group by criteria
   *
   * @param criteria
   * @param pageable
   * @return Page of {@link WssMarginArticleGroupDto}
   */
  Page<WssMarginArticleGroupDto> searchByCriteria(WssMarginArticleGroupSearchCriteria criteria,
      Pageable pageable);

  /**
   * Find default WSS Margin Article Group
   *
   * @param orgId
   * @return Optional of {@link WssMarginArticleGroupDto}
   */
  Optional<WssMarginArticleGroupDto> findDefaultMarginArticleGroup(int orgId);

  /**
   * Search root WSS Margin Article Group
   *
   * @param orgId
   * @param pageable
   * @return Page of {@link WssMarginArticleGroupDto}
   */
  Page<WssMarginArticleGroupDto> searchAllRootMarginArticleGroup(int orgId, Pageable pageable);

  /**
   * Search child WSS Margin Article Group from parent node id
   *
   * @param orgId
   * @param parentId
   * @return List of {@link WssMarginArticleGroupDto}
   */
  List<WssMarginArticleGroupDto> searchAllChildMarginArticleGroup(int orgId, int parentId);

  boolean updateWssShowNetPriceSetting(boolean isWssShowNetPrice, int orgId)
      throws WssMarginArticleGroupException, WssBrandMaginException;

  /**
   * Import WSS margin article group by CSV
   *
   * @param marginArticleGroups
   * @param orgId
   * @return result of import {@link WssImportMarginArticleGroupResponseDto}
   */
  WssImportMarginArticleGroupResponseDto importWssMarginArticleGroup(
      List<WssCsvMarginArticleGroup> marginArticleGroups, int orgId)
      throws WssMarginArticleGroupException;

  /**
   * Get WSS margin by article group to CSV template
   *
   * @return margin by article group CSV template
   */
  ExportStreamedResult getCsvTemplate(String languageCode) throws ServiceException;

  Optional<WssMarginArticleGroupDto> unmapWssMarginArticleGroup(Integer marginArticleGroupId,
      int orgId) throws WssMarginArticleGroupException;

  ExportStreamedResult exportMappedMarginArticleGroup(int orgId, String userLangCode)
      throws ServiceException;

  ExportStreamedResult exportAllMarginArticleGroup(int orgId, String userLangCode)
      throws ServiceException;

}
