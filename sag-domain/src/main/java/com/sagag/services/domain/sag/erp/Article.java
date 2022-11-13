package com.sagag.services.domain.sag.erp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
  "vocArticleId",
  "vrgArticleId",
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
  private String id;

  private String description;

  private String number;

  @JsonSerialize(using = ToStringSerializer.class)
  private String depotArticleId;

  @JsonSerialize(using = ToStringSerializer.class)
  private String recycleArticleId;

  @JsonSerialize(using = ToStringSerializer.class)
  private String vocArticleId;

  @JsonSerialize(using = ToStringSerializer.class)
  private String vrgArticleId;

  private String keyword;

  private String lockTypeCode;

  private String lockTypeDesc;

  private Boolean inLiquidation;

  private Boolean frontEndLockForAustria;

  private String lockType;

  private boolean articleLock;

  private boolean fitmentLock;

  private List<ItemApprovalTypeName> itemApprovalTypeNames;

  // Custom field to detect article is lock on SAG
  private boolean displayArticle;

  private boolean nonReturnable;

  public boolean hasDepotArticleId() {
    return hasValidAttachedArticleId(this.getDepotArticleId());
  }

  public boolean hasRecycleArticleId() {
    return hasValidAttachedArticleId(this.getRecycleArticleId());
  }

  public boolean hasVocArticleId() {
    return hasValidAttachedArticleId(this.getVocArticleId());
  }

  public boolean hasVrgArticleId() {
    return hasValidAttachedArticleId(this.getVrgArticleId());
  }

  private static boolean hasValidAttachedArticleId(String artId) {
    return !StringUtils.isBlank(artId)
        && !StringUtils.equalsIgnoreCase(NumberUtils.INTEGER_ZERO.toString(), artId);
  }

}
