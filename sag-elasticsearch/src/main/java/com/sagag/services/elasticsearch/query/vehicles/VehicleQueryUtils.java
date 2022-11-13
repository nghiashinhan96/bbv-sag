package com.sagag.services.elasticsearch.query.vehicles;

import com.sagag.services.elasticsearch.criteria.vehicle.VehicleFilteringCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchSortCriteria;
import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.utils.ElasticsearchConstants;
import com.sagag.services.elasticsearch.utils.FreeTextNormalizeStringUtils;
import com.sagag.services.elasticsearch.utils.QueryUtils;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public final class VehicleQueryUtils {

  private static final int MIN_LENGTH_CHARS = 2;

  private static final char WILDCARD = ElasticsearchConstants.WILDCARD;

  private static final Pattern UPPER_ALPHABET_PATTERN = Pattern.compile("^[A-Z]");

  private static final Predicate<String> WILDCARD_VALIDATOR;

  private static final String VEHICLE_BUILT_YEAR_MONTH_FORMAT = "yyyyMM";

  private static final String VEHICLE_BUILT_YEAR_FORMAT = "yyyy";

  private static final String[] VEHICLE_BUILT_YEAR_FORMATS = { VEHICLE_BUILT_YEAR_FORMAT, "yy" };

  private static final String[] VEHICLE_BUILT_YEAR_MONTH_FORMATS = {
      VEHICLE_BUILT_YEAR_MONTH_FORMAT, "MM-yyyy", "MM-yy", "MM/yyyy", "MM/yy"
  };

  private static final List<DateTimeFormatter> SUPPORTED_DATE_FORMATTERES_WITH_YEAR_MONTH;

  private static final List<DateTimeFormatter> SUPPORTED_DATE_FORMATTERES_WITH_YEAR;

  private static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern(
      VEHICLE_BUILT_YEAR_MONTH_FORMAT);

  private static final DateTimeFormatter YEAR_FORMATTER = DateTimeFormatter.ofPattern(
      VEHICLE_BUILT_YEAR_FORMAT);

  private static final String JAN_MONTH_STR = "01";

  private static final String DEC_MONTH_STR = "12";

  static  {
    SUPPORTED_DATE_FORMATTERES_WITH_YEAR_MONTH = Stream.of(VEHICLE_BUILT_YEAR_MONTH_FORMATS)
      .map(dateTimeFormatterConverter()).collect(Collectors.toList());
    SUPPORTED_DATE_FORMATTERES_WITH_YEAR = Stream.of(VEHICLE_BUILT_YEAR_FORMATS)
        .map(dateTimeFormatterConverter()).collect(Collectors.toList());

    WILDCARD_VALIDATOR = UPPER_ALPHABET_PATTERN.asPredicate()
        .and(value -> value.length() > MIN_LENGTH_CHARS);
  }

  private static Function<String, DateTimeFormatter> dateTimeFormatterConverter() {
    return formatStr -> {
      final DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
      builder.appendPattern(formatStr);
      if (!StringUtils.contains(formatStr, "M")) {
        builder.parseDefaulting(ChronoField.MONTH_OF_YEAR, 1);
      }
      builder.parseDefaulting(ChronoField.DAY_OF_MONTH, 1);
      return builder.toFormatter();
    };
  }

  public static Optional<String> parseBuiltYearMonthString(final String str) {
    return parseBuiltYearMonthString(SUPPORTED_DATE_FORMATTERES_WITH_YEAR_MONTH, str, false);
  }

  public static Optional<String> parseBuiltYearString(final String str) {
    return parseBuiltYearMonthString(SUPPORTED_DATE_FORMATTERES_WITH_YEAR, str, true);
  }

  private static Optional<String> parseBuiltYearMonthString(
      final List<DateTimeFormatter> formatters, final String str, boolean yearMode) {
    if (StringUtils.isBlank(str)) {
      return Optional.empty();
    }
    LocalDate date = null;
    for (DateTimeFormatter formatter : formatters) {
      try {
        date = LocalDate.parse(str, formatter);
        break;
      } catch (DateTimeParseException ex) {
        // do nothing
      }
    }
    return Optional.ofNullable(date).map(item -> {
      if (yearMode) {
        return YEAR_FORMATTER.format(item);
      }
      return YEAR_MONTH_FORMATTER.format(item);
    });
  }

  public static Function<String, String> analyzeVehName() {
    return vehName -> FreeTextNormalizeStringUtils.or(
        extractQueryVehicleName().apply(vehName),
        QueryUtils.removeNonAlphaChars(vehName, true),
        vehName);
  }

  public static Function<List<String>, String> analyzePowers() {
    return powers -> FreeTextNormalizeStringUtils.or(powers.toArray(new String[powers.size()]));
  }

  public static Function<String, String> extractQueryVehicleName() {
    return vehName -> {
      if (StringUtils.isBlank(vehName)) {
        return StringUtils.EMPTY;
      }
      // #3506: [AT-AX] Improvements for the new Vehicle Search, Part 2: AC 2-3-4-5-6 and 9 of 3443
      return StringUtils.join(Stream.of(vehName)
        .map(modifyQueryVehicleName()).flatMap(Stream::of).map(convertSearchTxt())
        .toArray(String[]::new), StringUtils.SPACE);
    };
  }

  private static Function<String, String[]> modifyQueryVehicleName() {
    return vehName -> StringUtils.split(StringUtils.trim(StringUtils.upperCase(vehName)),
      StringUtils.SPACE);
  }

  private static Function<String, String> convertSearchTxt() {
    return str -> {
      if (WILDCARD_VALIDATOR.test(str)) {
        return str + WILDCARD;
      }
      return str;
    };
  }

  public static Integer getFormattedBuiltYearAndJanMonth(final String str) {
    return getFormattedBuiltYear(str, JAN_MONTH_STR);
  }

  public static Integer getFormattedBuiltYearAndDecMonth(final String str) {
    return getFormattedBuiltYear(str, DEC_MONTH_STR);
  }

  private static Integer getFormattedBuiltYear(final String str, final String defaultMonth) {
    final StringBuilder dateBuilder = new StringBuilder();
    final Optional<String> builtYearFrom = VehicleQueryUtils.parseBuiltYearString(str);
    if (builtYearFrom.isPresent()) {
      dateBuilder.append(builtYearFrom.get())
        .append(defaultMonth);
    } else {
      dateBuilder.append(str);
    }
    return VehicleQueryUtils.parseBuiltYearMonthString(dateBuilder.toString()).map(Integer::valueOf)
      .orElseThrow(() -> new IllegalArgumentException("Date value must be present"));
  }

  public static List<FieldSortBuilder> getSortingQueries(final VehicleSearchSortCriteria sort) {
    final List<FieldSortBuilder> sortedBuilders = new ArrayList<>();

    Optional.ofNullable(sort.getVehInfo())
            .map(sortOrder -> SortBuilders.fieldSort(Index.Vehicle.VEHICLE_FULL_NAME_RAW.field())
                    .order(sortOrder))
            .ifPresent(sortedBuilders::add);

    Optional.ofNullable(sort.getVehBuiltYearMonthFrom())
            .map(sortOrder -> SortBuilders.fieldSort(Index.Vehicle.SEARCH_BUILT_YEAR_FROM.field())
                    .order(sortOrder))
            .ifPresent(sortedBuilders::add);

    Optional.ofNullable(sort.getVehBodyType())
            .map(sortOrder -> SortBuilders.fieldSort(Index.Vehicle.VEHICLE_BODY_TYPE.field())
                    .order(sortOrder))
            .ifPresent(sortedBuilders::add);

    Optional.ofNullable(sort.getVehFuelType())
            .map(sortOrder -> SortBuilders.fieldSort(Index.Vehicle.VEHICLE_FUEL_TYPE_RAW.field())
                    .order(sortOrder))
            .ifPresent(sortedBuilders::add);

    Optional.ofNullable(sort.getVehZylinder())
            .map(sortOrder -> SortBuilders.fieldSort(Index.Vehicle.VEHICLE_ZYLINDER_SORT.field())
                    .order(sortOrder))
            .ifPresent(sortedBuilders::add);

    Optional.ofNullable(sort.getVehEngine())
            .map(sortOrder -> SortBuilders.fieldSort(Index.Vehicle.VEHICLE_ENGINE.field())
                    .order(sortOrder))
            .ifPresent(sortedBuilders::add);

    Optional.ofNullable(sort.getVehMotorCode())
            .map(sortOrder -> SortBuilders.fieldSort(Index.Vehicle.VEHICLE_ENGINE_CODE_RAW.field())
                    .order(sortOrder))
            .ifPresent(sortedBuilders::add);

    Optional.ofNullable(sort.getVehPower())
            .map(sortOrder -> SortBuilders.fieldSort(Index.Vehicle.VEHICLE_POWER_KW_SORT.field())
                    .order(sortOrder))
            .ifPresent(sortedBuilders::add);

    Optional.ofNullable(sort.getVehDriveType())
            .map(sortOrder -> SortBuilders.fieldSort(Index.Vehicle.VEHICLE_DRIVE_TYPE_RAW.field())
                    .order(sortOrder))
            .ifPresent(sortedBuilders::add);

    Optional.ofNullable(sort.getVehAdvanceName())
        .map(sortOrder -> SortBuilders.fieldSort(Index.Vehicle.VEHICLE_ADVANCE_NAME_RAW.field())
            .order(sortOrder))
        .ifPresent(sortedBuilders::add);

    Optional.ofNullable(sort.getVehCapacityCcTech())
        .map(sortOrder -> SortBuilders.fieldSort(Index.Vehicle.VEHICLE_CAPACITY_CC_TECH.field())
            .order(sortOrder))
        .ifPresent(sortedBuilders::add);

    return sortedBuilders;
  }

  public static List<QueryBuilder> getFilteringQueries(final VehicleFilteringCriteria filtering) {
    final List<QueryBuilder> filteringQueryBuilders = new ArrayList<>();

    final String vehBuiltYearMonth = filtering.getVehBuiltYearMonthFrom();
    buildBuiltYearFromFiltering(vehBuiltYearMonth)
        .ifPresent(filteringQueryBuilders::add);

    buildBuiltYearTillFiltering(vehBuiltYearMonth)
        .ifPresent(filteringQueryBuilders::add);

    Optional.ofNullable(filtering.getVehBodyType())
        .filter(StringUtils::isNotBlank)
        .map(value -> QueryBuilders.termQuery(Index.Vehicle.VEHICLE_BODY_TYPE_RAW.field(), value))
        .ifPresent(filteringQueryBuilders::add);

    Optional.ofNullable(filtering.getVehFuelType())
        .filter(StringUtils::isNotBlank)
        .map(value -> QueryBuilders.termQuery(Index.Vehicle.VEHICLE_FUEL_TYPE_RAW.field(), value))
        .ifPresent(filteringQueryBuilders::add);

    Optional.ofNullable(filtering.getVehZylinder())
        .map(value -> QueryBuilders.termQuery(Index.Vehicle.VEHICLE_ZYLINDER.field(), value))
        .ifPresent(filteringQueryBuilders::add);

    Optional.ofNullable(filtering.getVehEngine())
        .filter(StringUtils::isNotBlank)
        .map(value -> QueryBuilders.termQuery(Index.Vehicle.VEHICLE_ENGINE.field(), value))
        .ifPresent(filteringQueryBuilders::add);

    Optional.ofNullable(filtering.getVehMotorCode())
        .filter(StringUtils::isNotBlank)
        .map(value -> QueryBuilders.termQuery(Index.Vehicle.VEHICLE_ENGINE_CODE_RAW.field(), value))
        .ifPresent(filteringQueryBuilders::add);

    Optional.ofNullable(filtering.getVehPower())
        .filter(StringUtils::isNotBlank)
        .ifPresent(value -> {
          String[] splitKwHp = value.split("/");
          filteringQueryBuilders
              .add(QueryBuilders.termQuery(Index.Vehicle.VEHICLE_POWER_KW.field(), splitKwHp[0]));
          if (splitKwHp.length > 1) {
            filteringQueryBuilders.add(
                QueryBuilders.termQuery(Index.Vehicle.VEHICLE_POWER_HP.field(), splitKwHp[1]));
          }
        });

    Optional.ofNullable(filtering.getVehDriveType())
        .filter(StringUtils::isNotBlank)
        .map(value -> QueryBuilders.termQuery(Index.Vehicle.VEHICLE_DRIVE_TYPE_RAW.field(), value))
        .ifPresent(filteringQueryBuilders::add);

    Optional.ofNullable(filtering.getVehFullName())
        .filter(StringUtils::isNotBlank)
        .map(item -> StringUtils.split(item, StringUtils.SPACE))
        .map(FreeTextNormalizeStringUtils::and)
        .map(value -> QueryBuilders.queryStringQuery(value).field(
            Index.Vehicle.VEHICLE_FULL_NAME.field()))
        .ifPresent(filteringQueryBuilders::add);

    Optional.ofNullable(filtering.getVehAdvanceName())
        .filter(StringUtils::isNotBlank)
        .map(value -> QueryBuilders.termQuery(Index.Vehicle.VEHICLE_ADVANCE_NAME_RAW.field(), value))
        .ifPresent(filteringQueryBuilders::add);

    Optional.ofNullable(filtering.getVehCapacityCcTech())
        .filter(StringUtils::isNotBlank)
        .map(value -> QueryBuilders.termQuery(Index.Vehicle.VEHICLE_CAPACITY_CC_TECH.field(), value))
        .ifPresent(filteringQueryBuilders::add);

    return filteringQueryBuilders;
  }

  private static Optional<RangeQueryBuilder> buildBuiltYearFromFiltering(
      final String vehBuiltYearMonthFrom) {
    if (StringUtils.isBlank(vehBuiltYearMonthFrom)) {
      return Optional.empty();
    }
    return Optional.of(QueryBuilders.rangeQuery(Index.Vehicle.SEARCH_BUILT_YEAR_FROM.field())
        .lte(VehicleQueryUtils.getFormattedBuiltYearAndDecMonth(vehBuiltYearMonthFrom)));
  }

  private static Optional<RangeQueryBuilder> buildBuiltYearTillFiltering(
      final String vehBuiltYearMonthTill) {
    if (StringUtils.isBlank(vehBuiltYearMonthTill)) {
      return Optional.empty();
    }
    return Optional.of(QueryBuilders.rangeQuery(Index.Vehicle.SEARCH_BUILT_YEAR_TILL.field())
        .gte(VehicleQueryUtils.getFormattedBuiltYearAndJanMonth(vehBuiltYearMonthTill)));
  }
}
