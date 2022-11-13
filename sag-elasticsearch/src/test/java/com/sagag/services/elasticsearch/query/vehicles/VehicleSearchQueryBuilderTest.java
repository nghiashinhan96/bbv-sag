package com.sagag.services.elasticsearch.query.vehicles;

import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleFilteringCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchSortCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchTermCriteria;
import com.sagag.services.elasticsearch.query.vehicles.builder.AbstractVehicleSearchTermQueryBuilder;
import com.sagag.services.elasticsearch.query.vehicles.builder.VehicleDescriptionQueryBuilder;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class VehicleSearchQueryBuilderTest {

  private static final String INDEX = "vehicles_de";

  @InjectMocks
  private VehicleSearchQueryBuilder builder;

  @Spy
  private List<AbstractVehicleSearchTermQueryBuilder> searchTermQueryBuilders = new ArrayList<>();

  @Mock
  private VehicleDescriptionQueryBuilder vehDescQueryBuilder;

  private VehicleSearchTermCriteria term;

  @Before
  public void setup() {
    term = VehicleSearchTermCriteria.builder().vehicleDesc("audi").build();
    Mockito.doCallRealMethod().when(vehDescQueryBuilder).isValid(Mockito.any());
    Mockito.doCallRealMethod().when(vehDescQueryBuilder).build(Mockito.any());
    searchTermQueryBuilders.clear();
    searchTermQueryBuilders.add(vehDescQueryBuilder);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldBuildQuery_WithNullTerm() {
    final VehicleFilteringCriteria filtering =
        VehicleFilteringCriteria.builder().vehBuiltYearMonthFrom("2016").build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(null).filtering(filtering).build();
    builder.buildQuery(criteria, PageUtils.DEF_PAGE, INDEX);
  }

  @Test
  public void shouldBuildQuery_WithNullFiltering() {
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(term).filtering(null).build();
    builder.buildQuery(criteria, PageUtils.DEF_PAGE, INDEX);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldBuildQuery_WithWrongBuiltYearFormat() {
    final VehicleFilteringCriteria filtering =
        VehicleFilteringCriteria.builder().vehBuiltYearMonthFrom("016").build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(term).filtering(filtering).build();
    builder.buildQuery(criteria, PageUtils.DEF_PAGE, INDEX);
  }

  @Test
  public void shouldBuildQuery_WithYearFormat_4Digits() {
    final VehicleFilteringCriteria filtering =
        VehicleFilteringCriteria.builder().vehBuiltYearMonthFrom("2016").build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(term).filtering(filtering).build();
    builder.buildQuery(criteria, PageUtils.DEF_PAGE, INDEX);
  }

  @Test
  public void shouldBuildQuery_WithYearFormat_2Digits() {
    final VehicleFilteringCriteria filtering =
        VehicleFilteringCriteria.builder().vehBuiltYearMonthFrom("16").build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(term).filtering(filtering).build();
    builder.buildQuery(criteria, PageUtils.DEF_PAGE, INDEX);
  }

  @Test
  public void shouldBuildQuery_WithMonthYearFormat() {
    final VehicleFilteringCriteria filtering =
        VehicleFilteringCriteria.builder().vehBuiltYearMonthFrom("01/2016").build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(term).filtering(filtering).build();
    builder.buildQuery(criteria, PageUtils.DEF_PAGE, INDEX);
  }

  @Test
  public void shouldBuildQuery_WithMonthYearEmpty() {
    final VehicleFilteringCriteria filtering =
        VehicleFilteringCriteria.builder().vehBuiltYearMonthFrom(StringUtils.EMPTY).build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(term).filtering(filtering).build();
    builder.buildQuery(criteria, PageUtils.DEF_PAGE, INDEX);
  }

  @Test
  public void shouldBuildQuery_WithFullFiltering() {
    final VehicleFilteringCriteria filtering =
        VehicleFilteringCriteria.builder()
        .vehBuiltYearMonthFrom("01/2016")
        .vehBodyType("vehBodyType")
        .vehFuelType("vehFuelType")
        .vehZylinder(0)
        .vehEngine("vehEngine")
        .vehMotorCode("vehMotorCode")
        .vehPower("vehPower")
        .vehDriveType("vehDriveType")
        .vehFullName("VW PAS 85")
        .build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(term).filtering(filtering)
        .aggregation(true).build();
    builder.buildQuery(criteria, PageUtils.DEF_PAGE, INDEX);
  }

  @Test
  public void shouldBuildSortedQuery() {
    final VehicleFilteringCriteria filtering =
      VehicleFilteringCriteria.builder()
        .vehBuiltYearMonthFrom("01/2016")
        .vehBodyType("vehBodyType")
        .vehFuelType("vehFuelType")
        .vehZylinder(0)
        .vehEngine("vehEngine")
        .vehMotorCode("vehMotorCode")
        .vehPower("vehPower")
        .vehDriveType("vehDriveType")
        .build();
    final VehicleSearchSortCriteria sort = VehicleSearchSortCriteria.builder()
      .vehBodyType(SortOrder.ASC)
      .vehFuelType(SortOrder.DESC)
      .vehZylinder(SortOrder.ASC)
      .vehEngine(SortOrder.DESC)
      .vehMotorCode(SortOrder.ASC)
      .vehPower(SortOrder.ASC)
      .vehDriveType(SortOrder.DESC)
      .vehInfo(SortOrder.ASC)
      .vehBuiltYearMonthFrom(SortOrder.DESC)
      .build();
    final VehicleSearchCriteria criteria =
      VehicleSearchCriteria.builder().searchTerm(term).filtering(filtering).sort(sort)
        .aggregation(true).build();

    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

}
