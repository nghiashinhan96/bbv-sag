package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.WssWorkingDay;
import com.sagag.eshop.service.exception.WssOpeningDaysValidationException;
import com.sagag.services.domain.eshop.criteria.WssOpeningDaysSearchCriteria;
import com.sagag.services.domain.eshop.dto.WssCsvOpeningCalendar;
import com.sagag.services.domain.eshop.dto.WssOpeningDaysDetailDto;
import com.sagag.services.domain.eshop.dto.WssOpeningDaysDto;
import com.sagag.services.domain.eshop.openingdays.dto.WssOpeningDaysRequestBody;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interface to define services for WSS opening days calendar.
 */
public interface WssOpeningDaysService {

  /**
   * Gets all WSS working day code
   *
   * @return the list of {@link WssWorkingDay}
   */
  List<WssWorkingDay> getWorkingDayCodes();

  /**
   * Creates new WSS opening days calendar.
   *
   * @param request body request to create new opening days calendar
   * @param wholesaler orgId
   *
   * @return {@link WssOpeningDaysDetailDto}
   * @throws WssOpeningDaysValidationException
   */
  WssOpeningDaysDetailDto create(WssOpeningDaysRequestBody request, int orgId)
      throws WssOpeningDaysValidationException;

  /**
   * Updates existing WSS opening days calendar.
   *
   * @param request body request to update existing opening days calendar
   * @param wholesaler orgId
   *
   * @return {@link WssOpeningDaysDetailDto}
   * @throws WssOpeningDaysValidationException
   */
  WssOpeningDaysDetailDto update(WssOpeningDaysRequestBody request, int orgId)
      throws WssOpeningDaysValidationException;

  /**
   * Removes existing WSS opening days calendar.
   *
   * @param id opening days calendar id
   *
   */
  void remove(Integer id);

  /**
   * Gets existing WSS opening days calendar by id.
   *
   * @param id id of WSS opening days calendar
   *
   * @return the optional of {@link WssOpeningDaysDetailDto}
   */
  Optional<WssOpeningDaysDetailDto> getOpeningDaysCalendar(Integer id);

  /**
   * Searching existing WSS opening days calendar base on criteria.
   *
   * @param criteria search conditions
   * @param pageable pageable
   * @return {@link Page<WssOpeningDaysDto>}
   */
  Page<WssOpeningDaysDto> searchByCriteria(WssOpeningDaysSearchCriteria criteria,
      Pageable pageable);

  /**
   * Imports list WSS opening days calendar
   *
   * @param wssOpeningDaysCalendar list to be imported
   * @param checkExisting - if checkExisting is true, check the existing and do the import if not
   *        exist, otherwise throw exception - if checkExisting is false, do override import
   * @param wholesaler orgId
   * @throws WssOpeningDaysValidationException
   */
  void importWssOpeningDays(List<WssCsvOpeningCalendar> wssOpeningDaysCalendar,
      Boolean checkExisting, int orgId) throws WssOpeningDaysValidationException;


}
