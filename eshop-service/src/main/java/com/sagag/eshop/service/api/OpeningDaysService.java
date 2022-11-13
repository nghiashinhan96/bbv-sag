package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.WorkingDay;
import com.sagag.eshop.service.dto.CsvOpeningCalendar;
import com.sagag.eshop.service.dto.OpeningDaysDetailDto;
import com.sagag.eshop.service.dto.OpeningDaysDto;
import com.sagag.eshop.service.exception.OpeningDaysValidationException;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.criteria.OpeningDaysSearchCriteria;
import com.sagag.services.domain.eshop.openingdays.dto.OpeningDaysRequestBody;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Interface to define services for opening days calendar.
 */
public interface OpeningDaysService {

  /**
   * Gets all working day code
   *
   * @return the list of {@link WorkingDay}
   */
  List<WorkingDay> getWorkingDayCodes();

  /**
   * Creates new opening days calendar.
   *
   * @param request body request to create new opening days calendar
   *
   * @return {@link OpeningDaysDetailDto}
   * @throws OpeningDaysValidationException
   */
  OpeningDaysDetailDto create(OpeningDaysRequestBody request) throws OpeningDaysValidationException;

  /**
   * Updates existing opening days calendar.
   *
   * @param request body request to update existing opening days calendar
   *
   * @return {@link OpeningDaysDetailDto}
   * @throws OpeningDaysValidationException
   */
  OpeningDaysDetailDto update(OpeningDaysRequestBody request) throws OpeningDaysValidationException;

  /**
   * Removes existing opening days calendar.
   *
   * @param id opening days calendar id
   *
   */
  void remove(Integer id);

  /**
   * Gets existing opening days calendar by id.
   *
   * @param id id of opening days calendar
   *
   * @return the optional of {@link OpeningDaysDetailDto}
   */
  Optional<OpeningDaysDetailDto> getOpeningDaysCalendar(Integer id);

  /**
   * Searching existing opening days calendar base on criteria.
   *
   * @param criteria search conditions
   * @param pageable pageable
   * @return {@link Page<OpeningDaysDto>}
   */
  Page<OpeningDaysDto> searchByCriteria(OpeningDaysSearchCriteria criteria, Pageable pageable);

  /**
   * Imports list opening days calendar
   *
   * @param openingDaysCalendar list to be imported
   * @param checkExisting - if checkExisting is true, check the existing and do the import if not
   *        exist, otherwise throw exception - if checkExisting is false, do override import
   * @throws OpeningDaysValidationException
   */
  void importOpeningDays(List<CsvOpeningCalendar> openingDaysCalendar, Boolean checkExisting)
      throws OpeningDaysValidationException;

  /**
   * Gets next working date from opening days calendar
   *
   * @param affiliate
   * @param pickupBranchId
   * @param days
   * @return the optional of {@link Date}
   */
  Optional<Date> getDateLaterFromToday(SupportedAffiliate affiliate, String pickupBranchId, int days);
}
