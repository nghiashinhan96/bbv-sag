package com.sagag.services.elasticsearch.query.vehicles.builder;

import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchTermCriteria;

import org.elasticsearch.index.query.QueryBuilder;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class VehicleCodesQueryBuilderTest {

  @InjectMocks
  private VehicleCodesQueryBuilder builder;

  @Test
  public void test() {
    VehicleSearchTermCriteria term = new VehicleSearchTermCriteria();
    term.setVehicleCodes(Arrays.asList("", ""));
    QueryBuilder query = builder.build(term);
    Assert.assertThat(query, Matchers.notNullValue());
  }

}
