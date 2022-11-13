package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.OpeningDaysCalendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

/**
 * Opening days calendar JPA Repository interface.
 */
public interface OpeningDaysCalendarRepository extends JpaRepository<OpeningDaysCalendar, Integer>,
    JpaSpecificationExecutor<OpeningDaysCalendar> {

  Optional<OpeningDaysCalendar> findOneById(final Integer id);

  @Query("select o.id from OpeningDaysCalendar o where o.datetime= :date and o.country.id= :countryId")
  Optional<Integer> findByDateAndCountryId(@Param("date") Date date,
      @Param("countryId") Integer countryId);

  @Query("select o.id from OpeningDaysCalendar o where o.datetime= :date and o.country.shortName= :countryName")
  Integer findIdByDateAndCountryName(@Param("date") Date date,
      @Param("countryName") String countryName);

  @Query(value = "select case when count(o) > 0 then 'true' else 'false' end "
      + "from OpeningDaysCalendar o where YEAR(o.datetime) = :year")
  boolean checkExistingByYear(@Param("year") final Integer year);

  @Query("select o from OpeningDaysCalendar o where o.datetime= :date and o.country.shortName= :countryName")
  Optional<OpeningDaysCalendar> findByDateAndCountryName(@Param("date") Date date,
      @Param("countryName") String countryName);

  @Query(nativeQuery = true, value = "select top 1 o.datetime " +
      "from OPENING_DAYS_CALENDAR o " +
      "join COUNTRY c on c.ID = o.COUNTRY_ID " +
      "where c.SHORT_NAME = :countryName and o.datetime > :date " +
      "and ((o.WORKING_DAY_ID = 1 and (o.exceptions is null " +
      "or o.exceptions not like concat('%\"branches\":%\"', :pickupBranchId, '%') or (o.exceptions like concat('%\"branches\":%\"', :pickupBranchId, '%') " +
      " and o.exceptions not like concat('%\"affiliate\":\"', :companyName, '\"%') and o.exceptions not like '%\"affiliate\":null%')))" +
      "or (o.WORKING_DAY_ID <> 1 and ((o.exceptions like concat('%\"affiliate\":\"', :companyName, '\"%') or o.exceptions like '%\"affiliate\":null%') " +
      "and o.exceptions like concat('%\"branches\":%\"', :pickupBranchId, '\"%\"workingDayCode\":\"WORKING_DAY\"%')))) " +
      "and UPPER(DATENAME(dw, o.datetime)) in (select UPPER(bo.WEEK_DAY) from BRANCH_OPENING_TIME bo where bo.BRANCH_ID = " +
      "(select ID from BRANCH b where b.BRANCH_NUMBER = :pickupBranchId)) " +
      "order by o.datetime")
  Optional<Date> findNextWorkingDay(@Param("date") Date date,
      @Param("countryName") String countryName, @Param("companyName") String companyName,
      @Param("pickupBranchId") String pickupBranchId);

  @Query(nativeQuery = true, value = "select o.datetime "
      + "from OPENING_DAYS_CALENDAR o "
      + "join COUNTRY c on c.ID = o.COUNTRY_ID "
      + "where c.SHORT_NAME = :countryName and o.datetime > :date "
      + "and ((o.WORKING_DAY_ID = 1 and (o.exceptions is null "
      + "or o.exceptions not like concat('%\"branches\":%\"', :pickupBranchId, '%') or (o.exceptions like concat('%\"branches\":%\"', :pickupBranchId, '%') "
      + "and o.exceptions not like concat('%\"affiliate\":\"', :companyName, '\"%') and o.exceptions not like '%\"affiliate\":null%'))) "
      + "or (o.WORKING_DAY_ID <> 1 and ((o.exceptions like concat('%\"affiliate\":\"', :companyName, '\"%') or o.exceptions like '%\"affiliate\":null%') "
      + "and o.exceptions like concat('%\"branches\":%\"', :pickupBranchId, '\"%\"workingDayCode\":\"WORKING_DAY\"%')))) "
      + "order by o.datetime asc OFFSET :ammount ROWS FETCH NEXT 1 ROWS ONLY")
  Optional<Date> findWorkingDayLaterFrom(@Param("date") Date date,
      @Param("countryName") String countryName,
      @Param("companyName") String companyName,
      @Param("pickupBranchId") String pickupBranchId,
      @Param("ammount") int ammount);
}
