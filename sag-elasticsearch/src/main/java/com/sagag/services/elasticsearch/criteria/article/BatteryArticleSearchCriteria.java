package com.sagag.services.elasticsearch.criteria.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatteryArticleSearchCriteria extends ArticleAggregateCriteria {

  private List<String> voltages; // Spannung

  private List<String> ampereHours; // Amperestunden

  private List<String> lengths; // Lange

  private List<String> widths; // Breite

  private List<String> heights; // Höhe

  private List<String> interconnections; // Schaltung

  private List<String> typeOfPoles; // Polart

  private boolean withStartStop; // with Start Stop

  private boolean withoutStartStop; // without Start Stop

}
