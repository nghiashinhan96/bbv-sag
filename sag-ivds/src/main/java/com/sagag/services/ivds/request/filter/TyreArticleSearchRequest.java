package com.sagag.services.ivds.request.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.common.contants.TyreConstants;
import com.sagag.services.common.contants.TyreConstants.FzCategory;
import com.sagag.services.common.contants.TyreConstants.Season;
import com.sagag.services.elasticsearch.criteria.article.TyreArticleSearchCriteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class TyreArticleSearchRequest
    implements Serializable, ArticleSearchCriteriaConverter<TyreArticleSearchCriteria> {
  private static final long serialVersionUID = -1828877746104220204L;

  @JsonProperty("season")
  private String season;

  @JsonProperty("width")
  private String width;

  @JsonProperty("height")
  private String height;

  @JsonProperty("radius")
  private String radius;

  @JsonProperty("speed_index")
  private List<String> speedIndexes;

  @JsonProperty("tyre_segment")
  private List<String> tyreSegments;

  @JsonProperty("supplier")
  private String supplier;

  @JsonProperty("fz_category")
  private String fzCategory;

  @JsonProperty("runflat")
  private boolean runflat;

  @JsonProperty("spike")
  private boolean spike;

  @JsonProperty("offset")
  private int offset;

  @JsonProperty("size")
  private int size;

  @JsonProperty("match_code")
  private String matchCode;

  @JsonProperty("total_elements")
  private int totalElements;

  @JsonProperty("load_index")
  private List<String> loadIndexes;

  @Override
  public TyreArticleSearchCriteria toCriteria() {

    final TyreArticleSearchCriteria criteria = new TyreArticleSearchCriteria();
    final Set<String> seasonGenArtIds = new HashSet<>();
    if (StringUtils.isBlank(getSeason())) {
      seasonGenArtIds.addAll(TyreConstants.Season.SUMMER.getGenArtIds());
      seasonGenArtIds.addAll(TyreConstants.Season.WINTER.getGenArtIds());
      seasonGenArtIds.addAll(TyreConstants.Season.ALL_YEAR.getGenArtIds());
    } else {
      seasonGenArtIds.addAll(Season.valueOf(getSeason()).getGenArtIds());
    }
    criteria.setSeasonGenArtIds(seasonGenArtIds);

    if (!StringUtils.isBlank(getFzCategory())) {
      criteria.setFzCategoryGenArtIds(
          FzCategory.valueOf(getFzCategory()).getGenArtIds().stream().collect(Collectors.toSet()));
    }

    criteria.setWidthCvp(getWidth());
    criteria.setHeightCvp(getHeight());
    criteria.setRadiusCvp(getRadius());
    criteria.setSpeedIndexCvps(getSpeedIndexes());
    criteria.setRunflat(isRunflat());
    criteria.setSpike(isSpike());
    criteria.setTyreSegmentCvps(getTyreSegments());
    criteria.setSupplier(getSupplier());
    criteria.setLoadIndexCvps(getLoadIndexes());
    return criteria;
  }

  @JsonIgnore
  public boolean isMatchCodeSearch() {
    return !StringUtils.isBlank(matchCode);
  }
}
