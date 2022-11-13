package com.sagag.services.ivds.request.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.common.contants.TyreConstants;
import com.sagag.services.common.contants.TyreConstants.MotorbikeCategory;
import com.sagag.services.elasticsearch.criteria.article.MotorTyreArticleSearchCriteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class MotorTyreArticleSearchRequest
    implements Serializable, ArticleSearchCriteriaConverter<MotorTyreArticleSearchCriteria> {
  private static final long serialVersionUID = -1828877746104220204L;

  @JsonProperty("category")
  private String category;

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

  @JsonProperty("load_index")
  private List<String> loadIndexes;

  @Override
  public MotorTyreArticleSearchCriteria toCriteria() {

    final MotorTyreArticleSearchCriteria criteria = new MotorTyreArticleSearchCriteria();
    final Set<String> categoryGenArtIds = new HashSet<>();
    if (StringUtils.isBlank(getCategory())) {
      categoryGenArtIds.addAll(TyreConstants.MotorbikeCategory.STRASSENREIFEN.getGenArtIds());
      categoryGenArtIds.addAll(TyreConstants.MotorbikeCategory.ROLLER.getGenArtIds());
      categoryGenArtIds.addAll(TyreConstants.MotorbikeCategory.OFFROAD.getGenArtIds());
      categoryGenArtIds.addAll(TyreConstants.MotorbikeCategory.RENNREIFEN.getGenArtIds());
    } else {
      categoryGenArtIds.addAll(MotorbikeCategory.valueOf(getCategory()).getGenArtIds());
    }
    criteria.setCategoryGenArtIds(categoryGenArtIds);
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
