package com.sagag.services.elasticsearch.query.vehicles.builder;

import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.query.IAggregationQueryBuilder;

import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.scripted.ScriptedMetricAggregationBuilder;

public class AggregationKwHpQueryBuilder
  implements IAggregationQueryBuilder<ScriptedMetricAggregationBuilder> {

  private static final String SCRIPT_NAME = "vehicle_power_kw_hp";

  private static final String SCRIPT_INIT_TEMPLATE = "params._agg.%s = []";

  private static final String SCRIPT_MAP_TEMPLATE = "params._agg.%s.add(doc.%s.value + '/' + doc.%s.value)";

  @Override
  public ScriptedMetricAggregationBuilder build() {
    return AggregationBuilders.scriptedMetric(SCRIPT_NAME)
        .initScript(new Script(String.format(SCRIPT_INIT_TEMPLATE, SCRIPT_NAME)))
        .mapScript(new Script(String.format(SCRIPT_MAP_TEMPLATE, SCRIPT_NAME,
            Index.Vehicle.VEHICLE_POWER_KW.field(), Index.Vehicle.VEHICLE_POWER_HP.field())));
  }

}
