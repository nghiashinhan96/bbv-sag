package com.sagag.services.elasticsearch.query.vehicles.builder;

import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleFilteringCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchSortCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchTermCriteria;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

@RunWith(MockitoJUnitRunner.class)
public class VehicleAdvanceSearchQueryBuilderTest {

  private static final String INDEX = "vehicles_de";

  @InjectMocks
  private VehicleAdvanceSearchQueryBuilder queryBuilder;

  private VehicleSearchTermCriteria searchTerm;

  @Before
  public void setup() {
    searchTerm = VehicleSearchTermCriteria.builder().makeId(139).modelId(8547).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldBuildQueryWithException() {
    final VehicleFilteringCriteria filtering = VehicleFilteringCriteria.builder().build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(null).filtering(filtering).build();
    queryBuilder.buildQuery(criteria, PageUtils.DEF_PAGE, INDEX);
  }

  @Test
  public void shouldBuildQueryWithEmptyMakeModel() {
    final VehicleFilteringCriteria filtering = VehicleFilteringCriteria.builder().build();
    searchTerm.setMakeId(null);
    searchTerm.setModelId(null);
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(searchTerm).filtering(filtering).build();
    SearchQuery query = queryBuilder.buildQuery(criteria, PageUtils.DEF_PAGE, INDEX);
    Assert.assertNull(query.getQuery());
    Assert.assertNull(query.getFilter());
  }

  @Test
  public void shouldBuildQueryWithFullSearchTerm() {
    final VehicleFilteringCriteria filtering = VehicleFilteringCriteria.builder().build();
    searchTerm.setYearFrom("2022");
    searchTerm.setFuelType("ch");
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(searchTerm).filtering(filtering).build();
    SearchQuery query = queryBuilder.buildQuery(criteria, PageUtils.DEF_PAGE, INDEX);
    Assert.assertNotNull(query.getQuery());
  }

  @Test
  public void shouldBuildQueryWithSort() {
    final VehicleFilteringCriteria filtering = VehicleFilteringCriteria.builder().build();
    final VehicleSearchSortCriteria sort = VehicleSearchSortCriteria.builder()
        .vehBuiltYearMonthFrom(SortOrder.ASC).build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(searchTerm).filtering(filtering).sort(sort).build();
    SearchQuery query = queryBuilder.buildQuery(criteria, PageUtils.DEF_PAGE, INDEX);
    Assert.assertNotNull(query.getQuery());
    Assert.assertEquals(1, query.getElasticsearchSorts().size());
  }

  @Test
  public void shouldBuildQueryWithAggregation() {
    final VehicleFilteringCriteria filtering = VehicleFilteringCriteria.builder().build();
    final VehicleSearchSortCriteria sort = VehicleSearchSortCriteria.builder().build();
    final VehicleSearchCriteria criteria =
        VehicleSearchCriteria.builder().searchTerm(searchTerm).filtering(filtering).sort(sort).aggregation(true)
            .build();
    SearchQuery query = queryBuilder.buildQuery(criteria, PageUtils.DEF_PAGE, INDEX);
    Assert.assertNotNull(query.getQuery());
    Assert.assertEquals(9, query.getAggregations().size());
  }
}