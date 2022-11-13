package com.sagag.services.ivds.request.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.elasticsearch.criteria.article.ArticleAggregateMultiLevel;
import com.sagag.services.ivds.request.ArticlePartListSearchRequest;
import com.sagag.services.ivds.request.AccessorySearchRequest;
import com.sagag.services.ivds.request.WSPSearchRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class ArticleFilterRequest implements Serializable {

  private static final long serialVersionUID = -6703260395921760977L;

  @JsonProperty("gaids")
  private List<String> gaIds;

  @JsonProperty("suppliers")
  private List<String> suppliers;

  @JsonProperty("multiple_level_gaids")
  private List<ArticleAggregateMultiLevel> gaIdsMultiLevels;

  @JsonProperty("idDlnr")
  private String idDlnr;

  private boolean needSubAggregated;

  private boolean perfectMatched;

  private boolean fullRequest;

  private boolean directMatch;

  private boolean useMultipleLevelAggregation;

  @JsonProperty("criteriaIds")
  private List<String> cids;

  @JsonProperty("keyword")
  private String keyword;

  @JsonProperty("filter_mode")
  private String filterMode;

  @JsonProperty("offset")
  private int offset;

  @JsonProperty("size")
  private int size;

  @JsonProperty("context_key")
  private String contextKey;

  @JsonProperty("total_elements_of_searching")
  private int totalElementsOfSearching;

  @JsonProperty("tyre_search_request")
  private TyreArticleSearchRequest tyreSearchRequest;

  @JsonProperty("motor_tyre_search_request")
  private MotorTyreArticleSearchRequest motorTyreSearchRequest;

  @JsonProperty("bulb_search_request")
  private BulbArticleSearchRequest bulbSearchRequest;

  @JsonProperty("battery_search_request")
  private BatteryArticleSearchRequest batterySearchRequest;

  @JsonProperty("oil_search_request")
  private OilArticleSearchRequest oilSearchRequest;

  @JsonProperty("wsp_search_request")
  private WSPSearchRequest wspSearchRequest;
  
  @JsonProperty("accessory_search_request")
  private AccessorySearchRequest accessorySearchRequest;

  @JsonProperty("article_part_list_search_request")
  private ArticlePartListSearchRequest articlePartListSearchRequest;

  private CrossReferenceRequest crossReferenceRequest;
  
  @JsonIgnore
  public String getMatchCodeStr() {
    if (this.tyreSearchRequest != null
        && !StringUtils.isBlank(this.tyreSearchRequest.getMatchCode())) {
      return this.tyreSearchRequest.getMatchCode();
    }

    if (this.motorTyreSearchRequest != null
        && !StringUtils.isBlank(this.motorTyreSearchRequest.getMatchCode())) {
      return this.motorTyreSearchRequest.getMatchCode();
    }

    return StringUtils.EMPTY;
  }

  @JsonIgnore
  private List<String> preferSagsysIds;

  private Integer finalCustomerId;

  private boolean keepDirectAndOriginalMatch;

  private boolean hasPreviousData;

  // To check request from: true - shopping list; false - freeText, description, articleNumber
  private boolean isShoppingList;
}
