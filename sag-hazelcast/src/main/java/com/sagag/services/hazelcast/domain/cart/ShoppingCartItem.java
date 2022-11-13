package com.sagag.services.hazelcast.domain.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.SagPriceUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;
import com.sagag.services.hazelcast.domain.CartItemDto;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartItem implements Serializable {

  private static final long serialVersionUID = -1148450302434835031L;

  private String cartKey;
  private int quantity;
  @JsonIgnore
  private ArticleDocDto article;
  private CategoryDto category;
  private VehicleDto vehicle;
  private String itemDesc;
  private CartItemType itemType;

  // STARTING: to not affect to UI
  @JsonIgnore
  private ArticlePriceItem priceItem;
  private boolean recycle;
  private boolean depot;
  private boolean pfand;
  private boolean voc;
  private boolean vrg;

  private List<ShoppingCartItem> attachedCartItems;

  private Date addedTime;

  private String basketItemSourceId;

  private String basketItemSourceDesc;

  @JsonIgnore
  private List<ArticleDocDto> attachedArticles;

  private Integer finalCustomerOrgId;

  @JsonIgnore
  public ShoppingCartItem(ArticleDocDto articleDoc, int quantity, final double vatRate) {
    this.article = articleDoc;
    this.quantity = quantity;
    this.setPriceItem(articleDoc, vatRate);
  }

  private String reference;

  /**
   * Set priceItem base on the updated article.
   *
   * @param articleDoc the updated article
   */
  public void setPriceItem(ArticleDocDto articleDoc, final double vatRate) {
    if (articleDoc == null) {
      this.priceItem = new ArticlePriceItem(new PriceWithArticlePrice(), vatRate);
      return;
    }
    if (!articleDoc.hasPrice()) {
      this.priceItem = new ArticlePriceItem(new PriceWithArticlePrice(), vatRate);
      return;
    }
    this.priceItem = new ArticlePriceItem(articleDoc.getPrice().getPrice(), vatRate);
  }

  public void setPriceItem(ArticlePriceItem priceItem) {
    this.priceItem = priceItem;
  }

  @JsonIgnore
  public void incrementQuantity(int qty) {
    quantity += qty;
  }

  @JsonIgnore
  public void decrementQuantity() {
    quantity--;
  }

  /**
   * Returns net price without VAT.
   *
   * @return net price
   */
  public double getNetPrice() {
    return Objects.nonNull(priceItem) && Objects.nonNull(priceItem.getNetPrice())
        ? priceItem.getNetPrice().doubleValue()
        : NumberUtils.DOUBLE_ZERO;
  }

  /**
   * Returns net price with VAT.
   *
   * @return net price
   */
  public double getNetPriceWithVat() {
    if (Objects.isNull(priceItem) || Objects.isNull(priceItem.getNetPrice())) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return SagPriceUtils.calculateVATPrice(priceItem.getNetPrice().doubleValue(),
        priceItem.getVatInPercent());
  }

  /**
    * Returns promotion relative.
    *
    * @return promotion in percent
    */
  public double getPromotionInPercent() {
      if (Objects.isNull(priceItem) || Objects.isNull(priceItem.getPromotionInPercent())) {
          return NumberUtils.DOUBLE_ZERO;
      }
      return priceItem.getPromotionInPercent().doubleValue();
  }

  /**
   * Returns grossPrice, if there is no grossPrice return originalBrandPrice instead.
   *
   * @return gross price
   */
  public double getGrossPrice() {
    if (hasDisplayedPrice()) {
      return article.getDisplayedPrice().getPrice();
    }
    return getGrossPriceIgnoreDisplayedPrice();
  }

  public double getOriginalBrandPrice() {
    if (this.article == null || !this.article.hasPrice()
        || this.article.getPrice().getPrice().getOriginalBrandPrice() == null) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return this.article.getPrice().getPrice().getOriginalBrandPrice();
  }

  public double getGrossPriceIgnoreDisplayedPrice() {
    if (Objects.isNull(priceItem) || Objects.isNull(priceItem.getGrossPrice())) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return (priceItem.getGrossPrice().doubleValue() > 0.0) ? priceItem.getGrossPrice().doubleValue()
        : priceItem.getOriginalBrandPrice().doubleValue();
  }

  /**
   * Returns gross price with VAT.
   *
   * @return gross price
   */
  public double getGrossPriceWithVat() {
    if (Objects.isNull(priceItem) || Objects.isNull(priceItem.getVatInPercent())) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return SagPriceUtils.calculateVATPrice(getGrossPrice(), priceItem.getVatInPercent());
  }

  /**
   * Returns discount price.
   *
   * @return discount price
   */
  public double getDiscountPrice() {
    if (Objects.isNull(priceItem) || Objects.isNull(priceItem.getDiscountPrice())) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return priceItem.getDiscountPrice().doubleValue();
  }

  /**
   * Returns discount price with VAT.
   *
   * @return discount price
   */
  public double getDiscountPriceWithVat() {
    if (Objects.isNull(priceItem) || Objects.isNull(priceItem.getVatInPercent())
        || Objects.isNull(priceItem.getDiscountPrice())) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return SagPriceUtils.calculateVATPrice(priceItem.getDiscountPrice().doubleValue(),
        priceItem.getVatInPercent());
  }

  /**
   * Returns discount relative.
   *
   * @return discount in percent
   */
  public double getDiscountInPercent() {
    if (Objects.isNull(priceItem) || Objects.isNull(priceItem.getDiscountPrice())) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return priceItem.getDiscountInPercent().doubleValue();
  }

  /**
   * Returns total gross price without VAT.
   *
   * @return total gross price without VAT
   */
  public double getTotalGrossPrice() {
    if (hasDisplayedPrice()) {
      return getGrossPrice() * quantity;
    }
    if (Objects.isNull(priceItem) || Objects.isNull(priceItem.getTotalGrossPrice())) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return priceItem.getTotalGrossPrice();
  }

  /**
   * Returns total gross price with VAT.
   *
   * @return total gross price with VAT
   */
  public double getTotalGrossPriceWithVat() {
    if (hasDisplayedPrice() && hasVatInPercent()) {
      return SagPriceUtils.calculateVATPrice(getTotalGrossPrice(),
          priceItem.getVatInPercent());
    }
    if (Objects.isNull(priceItem) || Objects.isNull(priceItem.getVatInPercent())
        || Objects.isNull(priceItem.getTotalGrossPrice())) {
      return NumberUtils.DOUBLE_ZERO;
    }

    // It should be returned from ERP but, ERP is not ready for this
    return SagPriceUtils.calculateVATPrice(priceItem.getTotalGrossPrice(),
        priceItem.getVatInPercent());
  }

  /**
   * Returns total net price without VAT.
   *
   * @return total net price without VAT
   */
  public double getTotalNetPrice() {
    if (Objects.isNull(priceItem) || Objects.isNull(priceItem.getTotalNetPrice())) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return priceItem.getTotalNetPrice();
  }

  /**
   * Returns total final customer net price without VAT.
   *
   * @return total final customer net price without VAT
   */
  public double getTotalFinalCustomerNetPrice() {
    if (Objects.isNull(article.getFinalCustomerNetPrice())) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return getFinalCustomerNetPrice() * quantity;
  }

  /**
   * Returns total net price with VAT.
   *
   * @return total net price with VAT
   */
  public double getTotalNetPriceWithVat() {
    if (Objects.isNull(priceItem) || Objects.isNull(priceItem.getTotalNetPrice())
        || Objects.isNull(priceItem.getVatInPercent())) {
      return NumberUtils.DOUBLE_ZERO;
    }

    // It should be returned from ERP but, ERP is not ready for this
    return SagPriceUtils.calculateVATPrice(priceItem.getTotalNetPrice(),
        priceItem.getVatInPercent());
  }

  /**
   * Returns net 1 price without VAT.
   *
   * @return net 1 price without VAT
   */
  public double getNet1Price() {
    return Objects.nonNull(priceItem) && Objects.nonNull(priceItem.getNet1Price())
        ? priceItem.getNet1Price().doubleValue()
        : NumberUtils.DOUBLE_ZERO;
  }

  /**
   * Returns net 1 price with VAT.
   *
   * @return net 1 price with VAT
   */
  public double getNet1PriceWithVat() {
    if (Objects.isNull(priceItem) || Objects.isNull(priceItem.getNet1Price())) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return SagPriceUtils.calculateVATPrice(priceItem.getNet1Price().doubleValue(),
        priceItem.getVatInPercent());
  }

  /**
   * Returns total net 1 price without VAT.
   *
   * @return total net 1 price without VAT
   */
  public double getTotalNet1Price() {
    if (Objects.isNull(priceItem) || Objects.isNull(priceItem.getTotalNet1Price())) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return priceItem.getTotalNet1Price();
  }

  /**
   * Returns total net 1 price with VAT.
   *
   * @return total net 1 price with VAT
   */
  public double getTotalNet1PriceWithVat() {
    if (Objects.isNull(priceItem) || Objects.isNull(priceItem.getTotalNet1Price())
        || Objects.isNull(priceItem.getVatInPercent())) {
      return NumberUtils.DOUBLE_ZERO;
    }

    // It should be returned from ERP but, ERP is not ready for this
    return SagPriceUtils.calculateVATPrice(priceItem.getTotalNet1Price(),
        priceItem.getVatInPercent());
  }

  /**
   * Returns total gross price without VAT of attached articles if it has.
   *
   * @return total gross price without VAT of attached articles
   */
  @JsonIgnore
  public double getAttachedArticleTotalGrossPrice() {
    return AttachedCartItemHelper.getAttachedArticleTotalGrossPrice(attachedCartItems);
  }

  /**
   * Returns total gross price with VAT of attached articles if it has.
   *
   * @return total gross price with VAT of attached articles
   */
  @JsonIgnore
  public double getAttachedArticleTotalGrossPriceInclVat() {
    return AttachedCartItemHelper.getAttachedArticleTotalGrossPriceInclVat(attachedCartItems);
  }

  /**
   * Returns total net price without VAT of attached articles if it has.
   *
   * @return total net price without VAT if attached articles
   */
  @JsonIgnore
  public double getAttachedArticleTotalNetPrice() {
    return AttachedCartItemHelper.getAttachedArticleTotalNetPrice(attachedCartItems);
  }

  /**
   * Returns total net 1 price without VAT of attached articles if it has.
   *
   * @return total net 1 price without VAT if attached articles
   */
  @JsonIgnore
  public double getAttachedArticleTotalNet1Price() {
    return AttachedCartItemHelper.getAttachedArticleTotalNet1Price(attachedCartItems);
  }

  /**
   * Returns total net price with VAT of attached articles if it has.
   *
   * @return total net price with VAT of attached articles
   */
  @JsonIgnore
  public double getAttachedArticleTotalNetPriceInclVat() {
    return AttachedCartItemHelper.getAttachedArticleTotalNetPriceInclVat(attachedCartItems);
  }

  /**
   * Returns total VAT of attached articles based on Net if it has.
   *
   * @return total VAT of attached articles based on Net
   */
  @JsonIgnore
  public double getAttachedArticleVatTotalOnNet() {
    return AttachedCartItemHelper.getAttachedArticleVatTotalOnNet(attachedCartItems);
  }

  /**
   * Returns total VAT of attached articles based on Net 1 if it has.
   *
   * @return total VAT of attached articles based on Net 1
   */
  @JsonIgnore
  public double getAttachedArticleVatTotalOnNet1() {
    return AttachedCartItemHelper.getAttachedArticleVatTotalOnNet1(attachedCartItems);
  }

  /**
   * Returns total VAT of attached articles on Gross if it has.
   *
   * @return total VAT of attached articles based on Gross
   */
  @JsonIgnore
  public double getAttachedArticleVatTotalOnGross() {
    return AttachedCartItemHelper.getAttachedArticleVatTotalOnGross(attachedCartItems);
  }

  /**
   * Returns the article item in the shopping cart item.
   *
   * @return the {@link ArticleDocDto}
   */
  public ArticleDocDto getArticleItem() {
    return this.article;
  }

  public void setArticleItem(final ArticleDocDto articleItem) {
    this.article = articleItem;
  }

  // To remove when cleaning UI
  @JsonIgnore
  public int getSalesQuantity() {
    if (Objects.isNull(this.article) || Objects.isNull(this.article.getSalesQuantity())) {
      return NumberUtils.INTEGER_ZERO;
    }
    return this.article.getSalesQuantity().intValue();
  }

  public String getCurGaId() {
    if (Objects.isNull(category)) {
      return StringUtils.EMPTY;
    }
    return category.getGaId();
  }

  public String getCurGenArtDescription() {
    if (Objects.isNull(category)) {
      return StringUtils.EMPTY;
    }
    return category.getGaDesc();
  }

  public String getRootDescription() {
    if (Objects.isNull(category)) {
      return StringUtils.EMPTY;
    }
    return category.getRootDesc();
  }

  public String getVehicleId() {
    if (!hasVehicle()) {
      return StringUtils.EMPTY;
    }
    return vehicle.getVehId();
  }

  public String getVehicleInfo() {
    if (!hasVehicle()) {
      return StringUtils.EMPTY;
    }
    return vehicle.getVehicleInfo();
  }

  /**
   * Checks if the shopping cart item has vehicle in context.
   *
   * @return <code> true </code> if the vehicle is not null
   */
  public boolean hasVehicle() {
    if (Objects.isNull(vehicle)) {
      return false;
    }
    final String vehId = vehicle.getVehId();
    if (StringUtils.isBlank(vehId)) {
      return false;
    }
    return !StringUtils.equals(vehId, SagConstants.KEY_NO_VEHICLE);
  }

  /**
   * Checks if this shopping cart item is vin type.
   *
   * @return <code>true</code> if the shopping cart item is vin type, <code>false</code> otherwise.
   */
  public boolean isVin() {
    return this.itemType == CartItemType.VIN;
  }

  public boolean isNonReference() {
    return this.itemType == CartItemType.DVSE_NON_REF_ARTICLE;
  }

  @JsonIgnore
  public boolean hasAttachedCartItems() {
    return !CollectionUtils.isEmpty(attachedCartItems);
  }

  public boolean isAttachedCartItem() {
    return depot || pfand || recycle || voc || vrg;
  }

  private boolean hasVatInPercent() {
    return null != priceItem && null != priceItem.getVatInPercent();
  }

  private boolean hasDisplayedPrice() {
    return null != article && null != article.getDisplayedPrice()
        && null != article.getDisplayedPrice().getPrice();
  }

  public static BiFunction<CartItemDto, Double, ShoppingCartItem> itemConverter() {
    return (item, vatRate) -> {

      final List<ArticleDocDto> attachedArticles = item.getAttachedArticles();
      final ArticleDocDto article = item.getArticle();
      final CategoryDto category = item.getCategory();
      final ShoppingCartItem cartItem = new ShoppingCartItem(article, item.getQuantity(), vatRate);
      cartItem.setCartKey(item.getCartKey()); // Support end-user delete exactly item
      cartItem.setCategory(category);
      cartItem.setAddedTime(item.getAddedTime());
      cartItem.setItemDesc(item.getItemDesc());
      cartItem.setItemType(item.getItemType());
      cartItem.setFinalCustomerOrgId(item.getFinalCustomerOrgId());
      cartItem.setBasketItemSourceId(item.getBasketItemSourceId());
      cartItem.setBasketItemSourceDesc(item.getBasketItemSourceDesc());

      VehicleDto vehicle = item.getVehicle();
      if (Objects.isNull(vehicle)) {
        vehicle = new VehicleDto();
        vehicle.setVehId(SagConstants.KEY_NO_VEHICLE); // just for grouping the articles by vehicle
      }
      cartItem.setVehicle(vehicle);
      if (!CollectionUtils.isEmpty(attachedArticles)) {
        cartItem.setAttachedArticles(attachedArticles);
      }
      return cartItem;
    };
  }

  public static Function<ShoppingCartItem, CartItemDto> entityConverter() {
    return item -> {
      final CartItemDto cartItem = new CartItemDto();
      cartItem.setCartKey(item.getCartKey());
      cartItem.setArticle(item.getArticle());
      cartItem.setCategory(item.getCategory());
      cartItem.setVehicle(item.getVehicle());
      cartItem.setAddedTime(item.getAddedTime());
      cartItem.setItemDesc(item.getItemDesc());
      cartItem.setItemType(item.getItemType());
      cartItem.setAttachedArticles(item.getAttachedArticles());
      cartItem.setQuantity(item.getQuantity());
      cartItem.setFinalCustomerOrgId(item.getFinalCustomerOrgId());

      return cartItem;
    };
  }

  @JsonIgnore
  public String getIdSagsys() {
    return Optional.ofNullable(article).map(ArticleDocDto::getIdSagsys).orElse(StringUtils.EMPTY);
  }

  @JsonIgnore
  public boolean isNotAvailable() {
    return !isAvailable();
  }

  @JsonIgnore
  public boolean isAvailable() {
    return (article.hasAvailabilities()
        && article.getAvailabilities().stream().allMatch(avail -> !avail.isBackOrderTrue())
        && article.getAvailabilities().stream().allMatch(avail -> !avail.isConExternalSource()))
        || isVin();
  }

  @JsonIgnore
  public boolean hasVenAvailability() {
    return article.hasAvailabilities()
        && article.getAvailabilities().stream().anyMatch(avail -> avail.isVenExternalSource());
  }

  public double getFinalCustomerNetPrice() {
    return Objects.nonNull(article.getFinalCustomerNetPrice())
        ? article.getFinalCustomerNetPrice().doubleValue()
        : NumberUtils.DOUBLE_ZERO;
  }

  /**
   * Returns total final customer net price of attached articles if it has.
   *
   * @return total final customer net price of attached articles
   */
  @JsonIgnore
  public double getAttachedArticleTotalFinalCustomerNetPrice() {
    return AttachedCartItemHelper.getAttachedArticleTotalFinalCustomerNetPrice(attachedCartItems);
  }

  public double getFinalCustomerNetPriceWithVat() {
    if (Objects.isNull(article.getFinalCustomerNetPrice())) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return SagPriceUtils.calculateVATPrice(article.getFinalCustomerNetPrice().doubleValue(),
        priceItem.getVatInPercent());
  }

  public double getTotalFinalCustomerNetPriceWithVat() {
    if (Objects.isNull(article.getTotalFinalCustomerNetPrice())) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return SagPriceUtils.calculateVATPrice(article.getTotalFinalCustomerNetPrice().doubleValue(),
        priceItem.getVatInPercent());
  }

  public boolean equalsArticleId(String artId) {
    if (this.getArticle() == null) {
      return false;
    }
    return StringUtils.equalsIgnoreCase(this.getArticle().getIdSagsys(), artId);
  }

  public boolean equalsVehicleId(String vehId) {
    if (this.getVehicle() == null) {
      return StringUtils.isBlank(vehId)
        || StringUtils.equalsIgnoreCase(SagConstants.KEY_NO_VEHICLE, vehId);
    }
    return StringUtils.equalsIgnoreCase(this.getVehicle().getVehId(), vehId);
  }
}
