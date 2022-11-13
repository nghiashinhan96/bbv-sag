package com.sagag.services.elasticsearch.query.vehicles.builder;

import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchTermCriteria;
import com.sagag.services.elasticsearch.query.IQueryBuilder;
import com.sagag.services.elasticsearch.query.vehicles.VehicleSearchMode;

public abstract class AbstractVehicleSearchTermQueryBuilder
  implements IQueryBuilder<VehicleSearchTermCriteria> {

  public abstract VehicleSearchMode mode();

  public abstract boolean isValid(VehicleSearchTermCriteria criteria);

}
