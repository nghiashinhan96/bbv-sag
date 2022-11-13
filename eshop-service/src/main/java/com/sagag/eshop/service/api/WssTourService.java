package com.sagag.eshop.service.api;

import com.sagag.eshop.service.exception.WssTourValidationException;
import com.sagag.services.domain.eshop.dto.WssTourDto;
import com.sagag.services.domain.eshop.tour.dto.WssTourSearchRequestCriteria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface to define services for customer WSS Tour.
 */
public interface WssTourService {


  /**
   * Creates new WSS Tour.
   *
   * @param request body request to create new WSS Tour
   * @param orgId wholesaler organization id
   *
   * @return {@link WssTourDto}
   * @throws WssTourValidationException
   */
  WssTourDto create(WssTourDto wssTourDto, int orgId) throws WssTourValidationException;

  /**
   * Update existing WSS Tour.
   *
   * @param request body request to update WSS Tour
   * @param orgId wholesaler organization id
   *
   * @return {@link WssTourDto}
   * @throws WssTourValidationException
   */
  WssTourDto update(WssTourDto wssTourDto, int orgId) throws WssTourValidationException;

  /**
   * Remove WSS Tour.
   *
   * @param wssTourId id of WSS Tour to be deleted
   * @param orgId wholesaler organization id
   *
   * @throws WssTourValidationException
   */
  void remove(Integer wssTourId, int orgId) throws WssTourValidationException;

  /**
   * Search WSS Tour.
   *
   * @param criteria criteria of search WSS Tour request
   *
   * @return {@link Page<WssTourDto>}
   */
  Page<WssTourDto> searchTourByCriteria(WssTourSearchRequestCriteria criteria, Pageable pageable);

  /**
   * Get WSS Tour by id.
   *
   * @param wssTourId id of WSS Tour to be search
   * @param orgId wholesaler organization id
   *
   * @return {@link WssTourDto}
   * @throws WssTourValidationException
   */
  WssTourDto getWssTourDetail(Integer wssTourId, int orgId) throws WssTourValidationException;
}
