package com.sagag.services.service.api;

import com.sagag.eshop.service.dto.WssImportMarginBrandResponseDto;
import com.sagag.eshop.service.dto.WssMarginBrandDto;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.domain.eshop.criteria.WssMarginBrandSearchCriteria;
import com.sagag.services.domain.eshop.dto.WssCsvMarginBrand;
import com.sagag.services.elasticsearch.domain.SupplierTxt;
import com.sagag.services.service.exception.WssBrandMaginException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface WssMarginBrandService {

  WssMarginBrandDto create(WssMarginBrandDto wssMarginBrand, int orgId)
      throws WssBrandMaginException;

  WssMarginBrandDto update(WssMarginBrandDto wssMarginBrand, int orgId)
      throws WssBrandMaginException;

  void delete(Integer id) throws WssBrandMaginException;

  Page<SupplierTxt> searchBrandByName(String brandName, Pageable pageable);

  Page<WssMarginBrandDto> searchByCriteria(WssMarginBrandSearchCriteria criteria,
      Pageable pageable);

  /**
   * get wholesaler default margin by brand setting
   * @param orgId organization id of wholesaler
   * @return WssMarginBrandDto
   */
  Optional<WssMarginBrandDto> getWholeSalerDefaultMarginBrand(int orgId);

  WssImportMarginBrandResponseDto importWssMarginBrand(List<WssCsvMarginBrand> wssMarginBrands,
      int orgId) throws WssBrandMaginException;

  /**
   * Export WSS margin by brand to CSV
   *
   * @param criteria criteria for querying
   * @param orgId organization id of wholesaler
   * @return margin by brand report
   */
  ExportStreamedResult exportToCsvByCriteria(WssMarginBrandSearchCriteria criteria)
      throws ServiceException;

  /**
   * Get WSS margin by brand to CSV template
   *
   * @return margin by brand CSV template
   */
  ExportStreamedResult getCsvTemplate() throws ServiceException;

}
