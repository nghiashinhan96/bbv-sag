package com.sagag.services.elasticsearch.query.vehicles.builder;

import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchTermCriteria;
import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.enums.TransType;
import com.sagag.services.elasticsearch.enums.VehicleCodeAttribute;
import com.sagag.services.elasticsearch.query.vehicles.VehicleSearchMode;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class VehicleDataQueryBuilder extends AbstractVehicleSearchTermQueryBuilder {

  @Override
  public QueryBuilder build(VehicleSearchTermCriteria criteria) {
    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    final String vehData = criteria.getVehicleData();
    Stream.of(vehicleDataQueryBuilder(VehicleCodeAttribute.TYPENSCHEIN, TransType.CH, vehData),
        vehicleDataQueryBuilder(VehicleCodeAttribute.NATIONAL_CODE, TransType.AT, vehData),
        vehicleDataQueryBuilder(VehicleCodeAttribute.VEHICLE_KBA_CODE, TransType.KBANR, vehData),
        vehicleDataQueryBuilder(VehicleCodeAttribute.CNIT, TransType.FR, vehData))
    .forEach(queryBuilder::should);
    return QueryBuilders.nestedQuery(Index.Vehicle.VEH_CODE.path(), queryBuilder,
        ScoreMode.None);
  }

  protected static BoolQueryBuilder vehicleDataQueryBuilder(final VehicleCodeAttribute vehCodeAttr,
      final TransType vehTransType, final String... values) {
    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    queryBuilder.must(QueryBuilders.matchQuery(Index.Vehicle.VEH_CODE_ATTR.fullQField(),
        vehCodeAttr.getAttr()));

    queryBuilder.must(QueryBuilders.matchQuery(Index.Vehicle.VEH_CODE_TYPE.fullQField(),
        vehTransType.getCode()));

    queryBuilder.must(defaultVehCodeQueryBuilder(values));
    return queryBuilder;
  }

  private static QueryBuilder defaultVehCodeQueryBuilder(final String... values) {
    if (ArrayUtils.getLength(values) == 1) {
      return QueryBuilders.matchQuery(Index.Vehicle.VEH_CODE_VALUE.fullQField(), values[0]);
    }
    return QueryBuilders.termsQuery(Index.Vehicle.VEH_CODE_VALUE.fullQField(), values);
  }

  @Override
  public VehicleSearchMode mode() {
    return VehicleSearchMode.VEHICLE_DATA;
  }

  @Override
  public boolean isValid(VehicleSearchTermCriteria criteria) {
    return criteria != null && !StringUtils.isBlank(criteria.getVehicleData());
  }
}
