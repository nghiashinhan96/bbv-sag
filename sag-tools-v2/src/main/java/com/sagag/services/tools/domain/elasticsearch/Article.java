package com.sagag.services.tools.domain.elasticsearch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{
  "id",
  "description",
  "number",
  "keyword",
  "salesQuantity",
  "depotArticleId",
  "recycleArticleId",
  "lockTypeCode",
  "lockType",
  "lockTypeDesc",
  "inLiquidation",
  "frontEndLockForAustria"
})
public class Article implements Serializable {

  private static final long serialVersionUID = -8740747229623101677L;

  // Unit of consumption, i.e. number of units to be sold (SAG speech for 'Verbrauchseinheit').,
  private long salesQuantity;

  @JsonSerialize(using = ToStringSerializer.class)
  private long id;

  private String description;

  private String number;

  @JsonSerialize(using = ToStringSerializer.class)
  private long depotArticleId;

  @JsonSerialize(using = ToStringSerializer.class)
  private long recycleArticleId;

  private String keyword;

  private String lockTypeCode;

  private String lockTypeDesc;

  private Boolean inLiquidation;

  private Boolean frontEndLockForAustria;

  private String lockType;

  private boolean articleLock;

  private boolean fitmentLock;

  // Custom field to detect article is lock on SAG
  private boolean displayArticle;

}
