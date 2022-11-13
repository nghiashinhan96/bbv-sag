package com.sagag.services.ax.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.sag.erp.Article;
import com.sagag.services.domain.sag.erp.ItemApprovalTypeName;
import com.sagag.services.domain.sag.external.CustomLink;

import lombok.Data;

import static com.sagag.services.ax.utils.AxConstants.NON_RETURNABLE;

/**
 * Class to receive Ax article info response from Dynamic AX ERP.
 *
 */
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
  "vocArticleId",
  "vrgArticleId",
  "lockType",
  "lockType",
  "lockTypeDesc",
  "inLiquidation",
  "frontEndLockForAustria"
})
public class AxArticle implements Serializable {

  private static final long serialVersionUID = -8740747229623101677L;

  private String id;

  private String description;

  private String number;

  private String keyword;

  private Double salesQuantity;

  private Long depotArticleId;

  private Long recycleArticleId;

  private Long vocArticleId;

  private Long vrgArticleId;

  private Boolean articleLock;

  private Boolean fitmentLock;

  private String allowReturnOrders;

  @JsonProperty("itemApprovalTypeName")
  private List<AxItemApprovalTypeName> itemApprovalTypeNames;

  @JsonProperty("_links")
  private Map<String, CustomLink> links;

  @JsonIgnore
  public Article toArticleDto() {
    return Article.builder()
        .id(this.id).number(this.number)
        .description(this.description).keyword(this.keyword)
        .articleLock(BooleanUtils.toBoolean(this.articleLock))
        .fitmentLock(BooleanUtils.toBoolean(this.fitmentLock))
        .salesQuantity(Objects.isNull(this.salesQuantity) ? 0 : this.salesQuantity.longValue())
        .depotArticleId(defaultAttachedArtId(this.depotArticleId))
        .recycleArticleId(defaultAttachedArtId(this.recycleArticleId))
        .vocArticleId(defaultAttachedArtId(this.vocArticleId))
        .vrgArticleId(defaultAttachedArtId(this.vrgArticleId))
        .nonReturnable(NON_RETURNABLE.equals(this.allowReturnOrders))
        .itemApprovalTypeNames(CollectionUtils.emptyIfNull(this.itemApprovalTypeNames).stream()
            .map(approvalType -> SagBeanUtils.map(approvalType, ItemApprovalTypeName.class))
            .collect(Collectors.toList()))
        .build();
  }

  private static String defaultAttachedArtId(Long attArtId) {
    return Objects.isNull(attArtId) || attArtId.longValue() == 0 ? StringUtils.EMPTY
        : attArtId.toString();
  }

}
