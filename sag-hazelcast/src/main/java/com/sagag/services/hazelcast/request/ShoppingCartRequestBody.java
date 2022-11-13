package com.sagag.services.hazelcast.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.LicenseSettingsDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceDto;
import com.sagag.services.hazelcast.domain.cart.CategoryDto;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

@Data
public class ShoppingCartRequestBody implements Serializable {

  private static final long serialVersionUID = -309462170381436579L;

  private int quantity;

  private ArticleDocDto article;

  @JsonIgnore
  private CategoryDto category;

  private VehicleDto vehicle;

  private LicenseSettingsDto license;

  private boolean overideExisting;

  private Long finalCustomerOrgId;

  private String basketItemSourceId;

  private String basketItemSourceDesc;

  /**
   * Returns the vehicle id if exists in the added cart item.
   *
   * @return the vehicle id.
   */
  public String vehId() {
    if (Objects.isNull(this.vehicle)) {
      return StringUtils.EMPTY;
    }
    return this.vehicle.getVehId();
  }

  /**
   * Returns the article idSagsys if exists in the added cart item.
   *
   * @return the article idSagsys.
   */
  public String idSagsys() {
    if (Objects.isNull(this.article)) {
      return StringUtils.EMPTY;
    }
    return this.article.getIdSagsys();
  }

  /**
   * Checks if the shopping cart request to add has a valid article.
   *
   * @return <code>true</code> if the article is not null, <code>false</code> otherwise.
   */
  public boolean hasArticle() {
    return !Objects.isNull(this.article);
  }

  /**
   * Checks if the shopping cart request to add has a valid license.
   *
   * @return <code>true</code> if the license is not null, <code>false</code> otherwise.
   */
  public boolean hasLicense() {
    return !Objects.isNull(this.license);
  }

  public boolean hasDisplayedPrice() {
    return Objects.nonNull(article) && Objects.nonNull(article.getDisplayedPrice());
  }

  public DisplayedPriceDto getDisplayedPrice() {
    return Optional.ofNullable(article).map(ArticleDocDto::getDisplayedPrice).orElse(null);
  }

  public void setDisplayedPrice(DisplayedPriceDto displayedPrice) {
    article.setDisplayedPrice(displayedPrice);
  }
}
