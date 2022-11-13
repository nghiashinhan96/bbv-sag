package com.sagag.services.elasticsearch.query.vehicles;

import com.sagag.services.elasticsearch.query.IAggregationQueryBuilder;
import com.sagag.services.elasticsearch.query.vehicles.builder.AggregationKwHpQueryBuilder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VehicleSearchAggregationMode {

  VEHICLE_POWER_KW_HP(new AggregationKwHpQueryBuilder());

  private IAggregationQueryBuilder<?> queryBuilder;

}
