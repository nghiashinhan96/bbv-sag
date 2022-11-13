package com.sagag.services.elasticsearch.query.vehicles.builder;

import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchTermCriteria;
import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.parser.VehicleFreetextStringParser;
import com.sagag.services.elasticsearch.query.vehicles.VehicleConstants;
import com.sagag.services.elasticsearch.query.vehicles.VehicleSearchMode;
import com.sagag.services.elasticsearch.utils.QueryUtils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class VehicleFreetextQueryBuilder extends AbstractVehicleSearchTermQueryBuilder {

  private static final String[] SEARCH_VEHICLE_FREETEXT_FIELDS = new String[] {
      Index.Vehicle.VEHICLE_BRAND.field(),
      Index.Vehicle.VEHICLE_MODEL.field(),
      Index.Vehicle.VEHICLE_NAME.field(),
      Index.Vehicle.VEHICLE_BUILT_YEAR_FROM.field(),
      Index.Vehicle.VEHICLE_BUILT_YEAR_TIL.field(),
      Index.Vehicle.VEHICLE_ENGINE_CODE.field(),
      Index.Vehicle.VEHICLE_ENGINE_CODE_FULL.field(),
      Index.Vehicle.VEHICLE_FUEL_TYPE.field(),
      Index.Vehicle.VEHICLE_ENGINE.field(),
      Index.Vehicle.VEHICLE_POWER_KW.field(),
      Index.Vehicle.VEHICLE_BODY_TYPE.field(),
      Index.Vehicle.VEHICLE_DRIVE_TYPE.field(),
      Index.Vehicle.NAME_ALT.field(),
      Index.Vehicle.MODEL.field() };

  private static final Map<String, Float> VEHICLE_FREETEXT_ATTRIBUTES_BOOST;

  static {
    VEHICLE_FREETEXT_ATTRIBUTES_BOOST = new HashMap<>();
    VEHICLE_FREETEXT_ATTRIBUTES_BOOST.putIfAbsent(Index.Vehicle.VEHICLE_BRAND.field(),
        VehicleConstants.VEH_BRAND_BOOST);
    VEHICLE_FREETEXT_ATTRIBUTES_BOOST.putIfAbsent(Index.Vehicle.VEHICLE_MODEL.field(), 1.0f);
    VEHICLE_FREETEXT_ATTRIBUTES_BOOST.putIfAbsent(Index.Vehicle.VEHICLE_ENGINE_CODE.field(), 2.0f);
    VEHICLE_FREETEXT_ATTRIBUTES_BOOST.putIfAbsent(Index.Vehicle.MODEL.field(), 4.0f);
  }

  @Autowired
  private VehicleFreetextStringParser vehFreetextStrParser;

  @Override
  public QueryBuilder build(VehicleSearchTermCriteria criteria) {
    final String freetext =
        vehFreetextStrParser.apply(criteria.getFreeText(), ArrayUtils.EMPTY_OBJECT_ARRAY);
    return QueryUtils.queryStringBuilder(freetext, SEARCH_VEHICLE_FREETEXT_FIELDS,
        attributeBoosts());
  }

  @Override
  public Map<String, Float> attributeBoosts() {
    return VEHICLE_FREETEXT_ATTRIBUTES_BOOST;
  }

  @Override
  public VehicleSearchMode mode() {
    return VehicleSearchMode.FREE_TEXT;
  }

  @Override
  public boolean isValid(VehicleSearchTermCriteria criteria) {
    return criteria != null && !StringUtils.isBlank(criteria.getFreeText());
  }

}
