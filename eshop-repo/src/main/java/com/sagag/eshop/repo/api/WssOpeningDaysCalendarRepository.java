package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.WssOpeningDaysCalendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * WSS Opening days calendar JPA Repository interface.
 */
public interface WssOpeningDaysCalendarRepository
    extends JpaRepository<WssOpeningDaysCalendar, Integer>,
    JpaSpecificationExecutor<WssOpeningDaysCalendar> {

  Optional<WssOpeningDaysCalendar> findOneById(final Integer id);

  @Query("select o.id from WssOpeningDaysCalendar o where o.datetime= :date and o.country.id= :countryId and o.orgId = :orgId")
  Optional<Integer> findByDateAndCountryIdAndOrgId(@Param("date") Date date,
      @Param("countryId") Integer countryId, @Param("orgId") Integer orgId);

  @Query("select o.id from WssOpeningDaysCalendar o where o.datetime= :date and o.country.shortName= :countryName and o.orgId = :orgId")
  Integer findIdByDateAndCountryNameAndOrgId(@Param("date") Date date,
      @Param("countryName") String countryName, @Param("orgId") Integer orgId);

  @Query(value = "select case when count(o) > 0 then 'true' else 'false' end "
      + "from WssOpeningDaysCalendar o where YEAR(o.datetime) = :year and o.orgId = :orgId")
  boolean checkExistingByYearAndOrgId(@Param("year") final Integer year,
      @Param("orgId") Integer orgId);

  @Query(value = "select case when count(o.ID) > 0 then 'true' else 'false' end \r\n"
      + "from WSS_OPENING_DAYS_CALENDAR o join WSS_WORKING_DAY w on o.WSS_WORKING_DAY_ID = w.ID \r\n"
      + "where o.ORG_ID = :orgId and o.datetime = :date \r\n"
      + "and ((w.CODE = 'WORKING_DAY' and (o.EXCEPTIONS not like concat('%\"branches\":%\"', :branchNr, '%') "
      + "or o.EXCEPTIONS is null)) "
      + "or (w.CODE != 'WORKING_DAY' and o.EXCEPTIONS like concat('%\"branches\":%\"', :branchNr, '\"%\"workingDayCode\":\"WORKING_DAY\"%'))) ",
      nativeQuery = true)
  boolean checkExistingWorkingDayByDateAndOrgIdAndBranchNr(@Param("date") final LocalDate date,
      @Param("orgId") Integer orgId, @Param("branchNr") Integer branchNr);

  @Query(value = "select top 1 o.datetime "
      + "from WSS_OPENING_DAYS_CALENDAR o join WSS_WORKING_DAY w on o.WSS_WORKING_DAY_ID = w.ID \r\n "
      + "where o.ORG_ID = :orgId and o.datetime > :date \r\n "
      + "and ((w.CODE = 'WORKING_DAY' and (o.EXCEPTIONS not like concat('%\"branches\":%\"', :branchNr, '%')"
      + " or o.EXCEPTIONS is null)) "
      + "or (o.EXCEPTIONS like concat('%\"branches\":%\"', :branchNr, '\"%\"workingDayCode\":\"WORKING_DAY\"%'))) "
      + "order by o.datetime asc", nativeQuery = true)
  Optional<java.sql.Date> findNextWorkingDay(@Param("date") LocalDate date,
      @Param("branchNr") int branchNr, @Param("orgId") Integer orgId);

  @Query(value = "select top 1 o.datetime "
      + "from WSS_OPENING_DAYS_CALENDAR o join WSS_WORKING_DAY w on o.WSS_WORKING_DAY_ID = w.ID \r\n "
      + "where o.ORG_ID = :orgId and o.datetime > :date \r\n "
      + "and UPPER(DATENAME(dw, o.datetime)) in :weekDays "
      + "and ((w.CODE = 'WORKING_DAY' and (o.EXCEPTIONS not like concat('%\"branches\":%\"', :branchNr, '%')"
      + " or o.EXCEPTIONS is null)) "
      + "or (o.EXCEPTIONS like concat('%\"branches\":%\"', :branchNr, '\"%\"workingDayCode\":\"WORKING_DAY\"%'))) "
      + "order by o.datetime asc", nativeQuery = true)
  Optional<java.sql.Date> findNextWorkingDayLaterFromAndInWeekdays(
      @Param("date") final LocalDate date, @Param("orgId") Integer orgId,
      @Param("branchNr") Integer branchNr, @Param("weekDays") List<String> weekDays);
}
