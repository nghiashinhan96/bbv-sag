package com.sagag.services.domain.eshop.dto;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WssCsvMarginArticleGroup implements Serializable {

  private static final long serialVersionUID = 2881258746038949452L;

  @CsvBindByName(column = "SAG_ART_GROUP_ID")
  private String sagArtGroup;

  @CsvBindByName(column = "CUSTOMER_ART_GROUP_ID")
  private String customArticleGroup;

  @CsvBindByName(column = "CUSTOMER_ART_GROUP_TEXT")
  private String customArticleGroupDesc;

  @CsvBindByName(column = "MARGIN_1")
  private Double margin1;

  @CsvBindByName(column = "MARGIN_2")
  private Double margin2;

  @CsvBindByName(column = "MARGIN_3")
  private Double margin3;

  @CsvBindByName(column = "MARGIN_4")
  private Double margin4;

  @CsvBindByName(column = "MARGIN_5")
  private Double margin5;

  @CsvBindByName(column = "MARGIN_6")
  private Double margin6;

  @CsvBindByName(column = "MARGIN_7")
  private Double margin7;

  @CsvBindByName(column = "SAG_ART_GROUP_DESC")
  private String sagArticleGroupDesc;
}
