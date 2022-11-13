package com.sagag.services.elasticsearch.query.vehicles.builder;

import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchTermCriteria;
import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.enums.VehicleDescTokenType;
import com.sagag.services.elasticsearch.query.vehicles.VehicleConstants;
import com.sagag.services.elasticsearch.query.vehicles.VehicleDescTokenCollector;
import com.sagag.services.elasticsearch.query.vehicles.VehicleQueryUtils;
import com.sagag.services.elasticsearch.query.vehicles.VehicleSearchMode;
import com.sagag.services.elasticsearch.utils.QueryUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
public class VehicleDescriptionQueryBuilder extends AbstractVehicleSearchTermQueryBuilder {

  private static final String[] VEHICLE_DESCRIPTION_FIELDS = {
      /* Hubraum */
      Index.Vehicle.VEHICLE_ENGINE.field(),
      Index.Vehicle.VEHICLE_CAPACITY_CC_TECH.field(),

      /* Bezeichnung */
      Index.Vehicle.VEHICLE_BRAND.field(),
      Index.Vehicle.VEHICLE_MODEL.field(),
      Index.Vehicle.VEHICLE_NAME.field(),
      Index.Vehicle.VEHICLE_POWER_KW.field(),
      Index.Vehicle.VEHICLE_POWER_HP.field(),
      Index.Vehicle.VEHICLE_ENGINE_CODE.field(),
      Index.Vehicle.VEHICLE_ENGINE_CODE_FULL.field(),

      /* Zylinder */
      Index.Vehicle.VEHICLE_ZYLINDER.field(),

      /* Antrieb */
      Index.Vehicle.VEHICLE_DRIVE_TYPE.field(),

      /* Karosserieart */
      Index.Vehicle.VEHICLE_BODY_TYPE.field(),

      /* Treibstoff */
      Index.Vehicle.VEHICLE_FUEL_TYPE.field(),

      Index.Vehicle.NAME_ALT.field(),

      Index.Vehicle.MODEL.field() };

  private static final String[] VEHICLE_HOUSEPOWER_FIELD = {
      Index.Vehicle.VEHICLE_POWER_HP.field()
  };

  private static final String[] VEHICLE_KWPOWER_FIELD = {
      Index.Vehicle.VEHICLE_POWER_KW.field()
  };

  private static final Map<String, Float> ATTRIBUTES_BOOST;

  static {
    ATTRIBUTES_BOOST = new HashMap<>();
    ATTRIBUTES_BOOST.put(Index.Vehicle.VEHICLE_BRAND.field(), VehicleConstants.VEH_BRAND_BOOST);
    ATTRIBUTES_BOOST.put(Index.Vehicle.VEHICLE_ENGINE_CODE.field(), 5.0f);
    ATTRIBUTES_BOOST.put(Index.Vehicle.VEHICLE_MODEL.field(), 1.0f);
    ATTRIBUTES_BOOST.put(Index.Vehicle.VEHICLE_NAME.field(), 2.0f);
    ATTRIBUTES_BOOST.put(Index.Vehicle.VEHICLE_POWER_KW.field(), VehicleConstants.DF_BOOST);
    ATTRIBUTES_BOOST.put(Index.Vehicle.VEHICLE_POWER_HP.field(), VehicleConstants.DF_BOOST);
    ATTRIBUTES_BOOST.put(Index.Vehicle.VEHICLE_BODY_TYPE.field(), VehicleConstants.DF_BOOST);
    ATTRIBUTES_BOOST.putIfAbsent(Index.Vehicle.MODEL.field(), 4.0f);
  }

  @Override
  public QueryBuilder build(VehicleSearchTermCriteria criteria) {
    String vehicleDesc = StringUtils.defaultString(criteria.getVehicleDesc());

    // Tokenization the search text
    Map<VehicleDescTokenType, List<String>> tokenMaps = Stream
        .of(vehicleDesc.split(StringUtils.SPACE))
        .collect(VehicleDescTokenCollector.toMap());
    List<String> horsePowerTokens = ListUtils.union(
        tokenMaps.get(VehicleDescTokenType.NUMERIC_PS),
        tokenMaps.get(VehicleDescTokenType.ALPHANUMERIC_PS));
    List<String> kiWatPowerTokens = ListUtils.union(
        tokenMaps.get(VehicleDescTokenType.NUMERIC_KW),
        tokenMaps.get(VehicleDescTokenType.ALPHANUMERIC_KW));
    List<String> normalTokens = ListUtils.union(
        tokenMaps.get(VehicleDescTokenType.ALPHABET),
        tokenMaps.get(VehicleDescTokenType.NUMERIC));

    // Create query for HP and KW power
    BoolQueryBuilder queryForPower = QueryBuilders.boolQuery();
    Stream.of(
        buildQuery(horsePowerTokens, VEHICLE_HOUSEPOWER_FIELD),
        buildQuery(kiWatPowerTokens, VEHICLE_KWPOWER_FIELD))
        .filter(Objects::nonNull)
        .forEach(queryForPower::should);

    // Combine query for power with text query for vehicle description
    BoolQueryBuilder resultQuery = QueryBuilders.boolQuery();
    Stream.of(queryForPower,
        buildQuery(tokenToTextParser().apply(normalTokens), VEHICLE_DESCRIPTION_FIELDS))
        .filter(Objects::nonNull)
        .forEach(resultQuery::must);

    return resultQuery;
  }

  private static Function<List<String>, String> tokenToTextParser() {
    return tokens -> {
      if (CollectionUtils.isEmpty(tokens)) {
        return StringUtils.EMPTY;
      }
      return VehicleFreetextQueryStrParser.parseQueryContainEngineCode(
          StringUtils.join(tokens, StringUtils.SPACE));
    };
  }

  private QueryBuilder buildQuery(String input, String[] field) {
    if (StringUtils.isBlank(input)) {
      return null;
    }
    return QueryUtils.queryStringBuilder(VehicleQueryUtils.analyzeVehName().apply(input), field,
        attributeBoosts());
  }

  private QueryBuilder buildQuery(List<String> input, String[] field) {
    if (CollectionUtils.isEmpty(input)) {
      return null;
    }
    return QueryUtils.queryStringBuilder(VehicleQueryUtils.analyzePowers().apply(input), field,
        attributeBoosts());
  }

  @Override
  public Map<String, Float> attributeBoosts() {
    return ATTRIBUTES_BOOST;
  }

  @Override
  public VehicleSearchMode mode() {
    return VehicleSearchMode.VEHICLE_DESC;
  }

  @Override
  public boolean isValid(VehicleSearchTermCriteria criteria) {
    return criteria != null && !StringUtils.isBlank(criteria.getVehicleDesc());
  }

}
