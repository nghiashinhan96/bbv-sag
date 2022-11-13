package com.sagag.services.ivds.request.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.elasticsearch.criteria.article.BatteryArticleSearchCriteria;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(value = Include.NON_NULL)
public class BatteryArticleSearchRequest
  implements Serializable, ArticleSearchCriteriaConverter<BatteryArticleSearchCriteria> {

  private static final long serialVersionUID = -8102786482758962212L;

  private List<String> voltages; // Spannung

  @JsonProperty("ampere_hours")
  private List<String> ampereHours; // Amperestunden

  private List<String> lengths; // Lange

  private List<String> widths; // Breite

  private List<String> heights; // Höhe

  private List<String> interconnections; // Schaltung

  private List<String> typeOfPoles; // Polart

  private boolean withoutStartStop;

  private boolean withStartStop;

  @Override
  public BatteryArticleSearchCriteria toCriteria() {
    return BatteryArticleSearchCriteria.builder()
        .voltages(getVoltages())
        .ampereHours(getAmpereHours())
        .interconnections(getInterconnections())
        .typeOfPoles(getTypeOfPoles())
        .lengths(getLengths())
        .widths(getWidths())
        .heights(getHeights())
        .withoutStartStop(isWithoutStartStop())
        .withStartStop(isWithStartStop())
        .build();
  }

}
