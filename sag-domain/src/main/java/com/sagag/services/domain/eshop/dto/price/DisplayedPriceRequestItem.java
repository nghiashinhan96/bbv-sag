package com.sagag.services.domain.eshop.dto.price;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.domain.article.ArticleDocDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisplayedPriceRequestItem implements Serializable {

  private static final long serialVersionUID = 8510841343578254396L;

  private String articleId;
  private Integer amount;
  private List<DisplayedPriceBrand> brands;

  @JsonIgnore
  private ArticleDocDto article;

  @JsonIgnore
  public boolean hasVehicleBrand() {
    return CollectionUtils.isNotEmpty(brands);
  }

  @JsonIgnore
  public boolean hasNoVehicleBrand() {
    return !hasVehicleBrand();
  }

  @JsonIgnore
  public boolean isValidItem() {
    return Objects.nonNull(article);
  }

  @JsonIgnore
  public boolean isMultipleBrandItem() {
    return CollectionUtils.isNotEmpty(brands) && !isSingleBrandItem();
  }

  @JsonIgnore
  public boolean isSingleBrandItem() {
    return CollectionUtils.isNotEmpty(brands) && brands.size() == 1;
  }

  @JsonIgnore
  public Long getBrandIdForSingleBrandItem() {
    return brands.stream().findFirst().map(DisplayedPriceBrand::getBrandId).orElse(null);
  }

  @JsonIgnore
  public String getBrandForSingleBrandItem() {
    return brands.stream().findFirst().map(DisplayedPriceBrand::getBrand).orElse(StringUtils.EMPTY);
  }
}
