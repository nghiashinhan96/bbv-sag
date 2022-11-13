package com.sagag.services.domain.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.contants.TyreConstants;
import com.sagag.services.common.enums.ArticlesOrigin;
import com.sagag.services.common.enums.CertifiedPartType;
import com.sagag.services.common.enums.RelevanceGroupType;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.article.oil.OilProduct;
import com.sagag.services.domain.autonet.erp.AutonetArticleInfos;
import com.sagag.services.domain.eshop.dto.VehicleUsageDto;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalStock;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceDto;
import com.sagag.services.domain.sag.erp.Article;
import com.sagag.services.domain.sag.erp.ArticleStock;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.BulkArticleResult;
import com.sagag.services.domain.sag.erp.ErpArticleMemo;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;

import lombok.Data;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.Transient;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = { "parts", "parts_ext" })
public class ArticleDocDto implements Serializable {

  private static final long serialVersionUID = -7552342678666999494L;

  @JsonProperty("id")
  private String id;

  @JsonProperty("id_source")
  private String idSource;

  @JsonProperty("artnr")
  private String artnr;

  @JsonProperty("id_umsart")
  private String idUmsart;

  @JsonProperty("is_100pr")
  private boolean is100pr;

  @JsonProperty("gaID")
  private String gaId;

  @JsonProperty("id_pim")
  private String idSagsys;

  @JsonProperty("id_dlnr")
  private String idDlnr;

  @JsonProperty("supplier")
  private String supplier;

  @JsonProperty("supplierId")
  protected int supplierId;

  @JsonProperty("supplierArticleNumber")
  protected String supplierArticleNumber;

  @JsonProperty("artid")
  private String artid;

  @JsonProperty("artnr_display")
  private String artnrDisplay;

  @JsonProperty("partDesc")
  private String partDesc;

  @JsonProperty("amountNumber")
  private Integer amountNumber;

  @JsonProperty("salesQuantity")
  private Integer salesQuantity;

  @JsonProperty("isPromotion")
  private String isPromotion;

  @JsonProperty("product_designation_addon")
  private String productDesignationAddon;

  @JsonProperty("product_brand")
  private String productBrand;

  @JsonProperty("id_product_brand")
  private String idProductBrand;

  @JsonProperty("source")
  private String source;

  @JsonProperty("name")
  private String name;

  @JsonProperty("product_addon")
  private String productAddon;

  @JsonProperty("sag_product_group")
  private String sagProductGroup;

  @JsonProperty("sag_product_group_2")
  private String sagProductGroup2;

  @JsonProperty("sag_product_group_3")
  private String sagProductGroup3;

  @JsonProperty("sag_product_group_4")
  private String sagProductGroup4;

  @JsonProperty("icat")
  private String icat;

  @JsonProperty("icat2")
  private String icat2;

  @JsonProperty("icat3")
  private String icat3;

  @JsonProperty("icat4")
  private String icat4;

  @JsonProperty("icat5")
  private String icat5;

  @JsonProperty("genArtTxts")
  private List<GenArtTxtDto> genArtTxts;

  @JsonProperty("genArtTxtEng")
  private GenArtTxtDto genArtTxtEng;

  @JsonProperty("accessoryLists")
  private List<ArticleAccessoryDto> accessoryLists;

  @JsonProperty("criteria")
  private List<ArticleCriteriaDto> criteria;

  @JsonProperty("images")
  private List<ArticleImageDto> images;

  @JsonProperty("info")
  private List<ArticleInfoDto> infos;

  @JsonProperty("parts")
  @Transient
  private List<ArticlePartDto> parts;

  @JsonProperty("parts_ext")
  @Transient
  private List<ArticlePartDto> partsExt;

  @JsonProperty("vehicles")
  private List<VehicleUsageDto> vehicles;

  @JsonProperty("article")
  private Article article;

  @JsonProperty("price")
  private PriceWithArticle price;

  private List<ErpArticleMemo> memos;

  @JsonProperty("availabilities")
  private List<Availability> availabilities;

  @JsonProperty("stock")
  private ArticleStock stock;

  @JsonIgnore
  private List<ArticleStock> wtStocks;

  private OilProduct oilProduct;

  @JsonProperty("tyre_article")
  private boolean tyreArticle;

  @JsonProperty("qty_standard_ch")
  private Integer qtyStandardCh;

  @JsonProperty("qty_lowest_ch")
  private Integer qtyLowestCh;

  @JsonProperty("qty_multiple_ch")
  private Integer qtyMultipleCh;

  @JsonProperty("qty_standard_at")
  private Integer qtyStandardAt;

  @JsonProperty("qty_lowest_at")
  private Integer qtyLowestAt;

  @JsonProperty("qty_multiple_at")
  private Integer qtyMultipleAt;

  @JsonProperty("qty_standard_be")
  private Integer qtyStandardBe;

  @JsonProperty("qty_lowest_be")
  private Integer qtyLowestBe;

  @JsonProperty("qty_multiple_be")
  private Integer qtyMultipleBe;

  @JsonProperty("qty_standard_cz")
  private Integer qtyStandardCz;

  @JsonProperty("qty_lowest_cz")
  private Integer qtyLowestCz;

  @JsonProperty("qty_multiple_cz")
  private Integer qtyMultipleCz;

  @JsonProperty("qty_standard")
  private Integer qtyStandard;

  @JsonProperty("qty_lowest")
  private Integer qtyLowest;

  @JsonProperty("seqNo")
  private Integer seqNo;

  @JsonProperty("accessoryLinkText")
  private String accessoryLinkText;

  @JsonProperty("accesoryListsText")
  private String accesoryListsText;

  // #6774 Split Article Search results into different “Relevance Groups”
  @JsonProperty("relevanceGroupType")
  private RelevanceGroupType relevanceGroupType;

  // #4126 use to indicate articles origin
  @JsonProperty("origin")
  private ArticlesOrigin origin;

  private String freetextDisplayDesc;

  // #2266: Category tree- matching of Genart in Fitment
  private List<String> combinedGenArtIds;

  private CertifiedPartType certifiedPartType;

  private Map<String, List<String>> oeNumbers;

  private Map<String, List<String>> iamNumbers;

  private DisplayedPriceDto displayedPrice;

  // #4027 store the pnrn values of all articlePart has type "EAN"
  private List<String> pnrnEANs;

  private List<String> pnrnPccs;

  private boolean isGlassOrBody;

  private String hasReplacement;

  private String isReplacementFor;

  private boolean availRequested;

  @JsonIgnore
  private Availability venExternalAvailability;

  private ExternalStock externalStock;

  private Double totalAxStock;
  
  private Double deliverableStock;

  @JsonProperty("oilArticle")
  private boolean isOilArticle;

  private ArticleDocDto depositArticle;

  private ArticleDocDto vocArticle;

  private ArticleDocDto vrgArticle;

  private ArticleDocDto pfandArticle;

  private boolean allowedAddToShoppingCart = true;

  private boolean isFavorite;

  private String favoriteComment;

  private boolean bom;

  private boolean pseudo;

  @JsonIgnore
  private Double vatRate;

  private String basketItemSourceId;

  private String basketItemSourceDesc;

  private AutonetArticleInfos autonetInfos; // Autonet ERP response.

  private Integer qtyMultiple;

  private boolean isVin;

  private String itemDesc;

  @JsonIgnore
  private String originIdSagsys;

  @JsonProperty("parts_list_items")
  private List<ArticlePartItemDto> partListItems;

  @JsonIgnore
  public Availability findFirstAvailability() {
    if (!hasAvailabilities()) {
      // AX API exception handle
      return new Availability();
    }
    return this.availabilities.get(NumberUtils.INTEGER_ZERO);
  }

  @JsonIgnore
  public boolean hasAvailabilities() {
    return CollectionUtils.isNotEmpty(this.availabilities);
  }

  @JsonIgnore
  public boolean hasNoAvailabilities() {
    return !hasAvailabilities();
  }

  @JsonIgnore
  public boolean hasEnoughQuantity() {
    return hasAvailabilities() && amountNumber <= this.availabilities.stream()
        .filter(avail -> BooleanUtils.isNotTrue(avail.getBackOrder()))
        .mapToInt(Availability::getQuantity).sum();
  }

  @JsonIgnore
  public boolean hasVenAvailabilities() {
    return hasAvailabilities()
        && this.availabilities.stream().anyMatch(Availability::isVenExternalSource);
  }

  @JsonIgnore
  public boolean hasConAvailabilities() {
    return hasAvailabilities()
        && this.availabilities.stream().anyMatch(Availability::isConExternalSource);
  }

  @JsonIgnore
  public List<Availability> getBackorderFalseAvails() {
    return CollectionUtils.emptyIfNull(getAvailabilities()).stream()
        .filter(a -> BooleanUtils.isFalse(a.getBackOrder())).collect(Collectors.toList());
  }

  @JsonIgnore
  public Integer getBackorderFalseAmount() {
    return getBackorderFalseAvails().stream()
        .collect(Collectors.summingInt(Availability::getQuantity));
  }

  @JsonIgnore
  public boolean hasExternalAvail() {
    return hasAvailabilities()
        && this.availabilities.stream().anyMatch(Availability::isExternalSource);
  }

  @JsonIgnore
  public boolean isTyreMotorbikeArticle() {
    return TyreConstants.MOTORBIKE_GEN_ART_ID_1.equalsIgnoreCase(this.getGaId())
        || TyreConstants.MOTORBIKE_GEN_ART_ID_2.equalsIgnoreCase(this.getGaId());
  }

  public boolean hasAttachedArticle() {
    if (!hasErpArticle()) {
      return false;
    }
    return !StringUtils.isBlank(article.getDepotArticleId())
        || !StringUtils.isBlank(article.getRecycleArticleId())
        || !StringUtils.isBlank(article.getVocArticleId())
        || !StringUtils.isBlank(article.getVrgArticleId());
  }

  public boolean hasArticle(BulkArticleResult bulkArticle) {
    return Objects.nonNull(bulkArticle);
  }

  public boolean hasNetPrice() {
    return this.getPrice() != null && this.getPrice().hasNetPrice();
  }

  public boolean hasGrossPrice() {
    return Objects.nonNull(getPrice()) && Objects.nonNull(getPrice().getPrice())
        && Objects.nonNull(getPrice().getPrice().defaultGrossPrice())
        && Double.compare(getPrice().getPrice().defaultGrossPrice(), NumberUtils.DOUBLE_ZERO) != 0;
  }

  @JsonIgnore
  public Availability findAvailWithLatestTime() {
    if (CollectionUtils.isEmpty(this.availabilities)) {
      return new Availability();
    }
    Optional<Availability> availability24Hours =
        this.availabilities.stream().filter(Availability::isDelivery24Hours).findFirst();
    if (availability24Hours.isPresent()) {
      return availability24Hours.get();
    }
    final Optional<Date> dateOpt = this.availabilities.stream().map(
        availability -> DateUtils.toDate(availability.getArrivalTime(), DateUtils.UTC_DATE_PATTERN))
        .filter(Objects::nonNull).max(Date::compareTo);
    if (!dateOpt.isPresent()) {
      return new Availability();
    }
    final String latestDate = DateUtils.toStringDate(dateOpt.get(), DateUtils.UTC_DATE_PATTERN);
    return this.availabilities.stream()
        .filter(availability -> latestDate.equals(availability.getArrivalTime())).findFirst()
        .orElse(new Availability());
  }

  @JsonIgnore
  public boolean hasLatestArrivalTime() {
    if (!hasAvailabilities()) {
      return false;
    }
    final Availability latestAvailability = getLastestAvailability();
    if (latestAvailability == null) {
      return false;
    }
    return !StringUtils.isBlank(latestAvailability.getArrivalTime());
  }

  @JsonIgnore
  public boolean hasStock() {
    return getStockNr() > NumberUtils.DOUBLE_ZERO;
  }

  @JsonIgnore
  public boolean isPseudoArticle() {
    return Objects.nonNull(this.pseudo) && this.pseudo;
  }

  @JsonIgnore
  public boolean isNotPseudoArticle() {
    return !isPseudoArticle();
  }


  @JsonIgnore
  public boolean isOnStock() {
    return Objects.nonNull(stock) && stock.getStockAmount() >= this.amountNumber;
  }

  @JsonIgnore
  public boolean isNotOnStock() {
    return !isOnStock();
  }

  @JsonIgnore
  public double getStockNr() {
    Double axStock =
        Optional.ofNullable(totalAxStock).map(Function.identity()).orElse(NumberUtils.DOUBLE_ZERO);
    Double extStock = Optional.ofNullable(externalStock).map(ExternalStock::getStock)
        .orElse(NumberUtils.DOUBLE_ZERO);
    return axStock + extStock;
  }

  @JsonIgnore
  public boolean isDisplayArticle() {
    return Objects.nonNull(getArticle()) && getArticle().isDisplayArticle();
  }

  @JsonIgnore
  public boolean hasErpArticle() {
    return Objects.nonNull(article);
  }

  @JsonIgnore
  public long getLongIdSagsys() {
    if (StringUtils.isBlank(idSagsys) || !NumberUtils.isDigits(idSagsys)) {
      return NumberUtils.LONG_ZERO;
    }
    return NumberUtils.toLong(idSagsys);
  }

  @JsonIgnore
  public String getErpArticleId() {
    if (!hasErpArticle()) {
      return StringUtils.EMPTY;
    }
    return article.getId();
  }

  private String getGenArtDesc() {
    if (CollectionUtils.isEmpty(genArtTxts)) {
      return StringUtils.EMPTY;
    }
    final GenArtTxtDto firstGenArt = genArtTxts.get(0);
    if (StringUtils.isBlank(firstGenArt.getGatxtdech())) {
      return StringUtils.EMPTY;
    }
    return firstGenArt.getGatxtdech();
  }

  /**
   * Returns the displayed article description when freetext search.
   * <p>
   * Move the logic from FE to BE (from file search-freetext.component.ts)
   *
   * @return the displayed article description
   */
  public String getFreetextDisplayDesc() {

    if (!StringUtils.isBlank(this.freetextDisplayDesc)) {
      return this.freetextDisplayDesc;
    }

    final String[] descParts = new String[] { getGenArtDesc(), supplier, artnrDisplay };
    return StringUtils.join(descParts, SagConstants.SPACE);
  }

  @JsonIgnore
  public Availability getLastestAvailability() {
    if (!hasAvailabilities()) {
      return null;
    }
    return Iterables.getLast(this.availabilities);
  }

  @JsonIgnore
  public boolean hasPrice() {
    return !Objects.isNull(price) && !Objects.isNull(price.getPrice());
  }

  @JsonIgnore
  public boolean hasValidArticlePrice() {
    if (!hasPrice()) {
      return false;
    }
    PriceWithArticlePrice articlePrice = price.getPrice();
    return !(isEmptyPrice(articlePrice.getRecommendedRetailPrice())
        && isEmptyPrice(articlePrice.getGrossPrice()) && isEmptyPrice(articlePrice.getNetPrice())
        && isEmptyPrice(articlePrice.getOriginalBrandPrice()));
  }

  private static boolean isEmptyPrice(Double priceValue) {
    return Objects.isNull(priceValue) || NumberUtils.DOUBLE_ZERO.compareTo(priceValue) >= 0;
  }

  @JsonIgnore
  public Optional<Double> getGrossPriceForDvse() {
    if (!hasPrice()) {
      return Optional.empty();
    }
    PriceWithArticlePrice articlePrice = price.getPrice();
    if (!Objects.isNull(articlePrice.getRecommendedRetailPrice())) {
      return Optional.of(articlePrice.getRecommendedRetailPrice());
    }
    return Optional.ofNullable(articlePrice.getGrossPrice());
  }

  @JsonIgnore
  public Optional<Double> getNetPrice() {
    if (!hasNetPrice()) {
      return Optional.empty();
    }
    return Optional.of(price.getPrice().getNetPrice());
  }

  @JsonIgnore
  public Optional<Double> getOepPrice() {
    if (!hasPrice()) {
      return Optional.empty();
    }
    final Double oepPrice = price.getPrice().getOepPrice();
    if (Objects.isNull(oepPrice) || NumberUtils.DOUBLE_ZERO.equals(oepPrice)) {
      return Optional.empty();
    }
    return Optional.of(oepPrice);
  }

  @JsonIgnore
  public String getArticleTitle() {
    return new StringBuilder(StringUtils.trimToEmpty(getSupplier())).append(StringUtils.SPACE)
        .append(StringUtils.trimToEmpty(getProductAddon())).append(StringUtils.SPACE)
        .append(StringUtils.trimToEmpty(getName())).append(StringUtils.SPACE)
        .append(StringUtils.trimToEmpty(getPartDesc())).toString();
  }

  @JsonIgnore
  public boolean hasOeNumbers() {
    return !MapUtils.isEmpty(oeNumbers);
  }

  @JsonIgnore
  public boolean hasIamNumbers() {
    return !MapUtils.isEmpty(iamNumbers);
  }

  @JsonIgnore
  public boolean hasPnrnPccs() {
    return CollectionUtils.isNotEmpty(pnrnPccs);
  }

  @JsonIgnore
  public boolean hasPnrnEANs() {
    return CollectionUtils.isNotEmpty(pnrnEANs);
  }

  @JsonIgnore
  public List<ArticlePartDto> getPartInfosByTypes(String... types) {
    if (CollectionUtils.isEmpty(parts) || ArrayUtils.isEmpty(types)) {
      return Collections.emptyList();
    }
    return parts.stream().filter(part -> ArrayUtils.contains(types, part.getPtype()))
        .collect(Collectors.toList());
  }

  public void addCriterias(List<ArticleCriteriaDto> criterias) {
    if (CollectionUtils.isEmpty(criterias)) {
      return;
    }
    if (this.criteria == null) {
      this.criteria = new LinkedList<>();
    }
    this.criteria.addAll(criterias);
  }

  public List<ArticleInfoDto> getInfos() {
    if (CollectionUtils.isEmpty(this.infos)) {
      return new ArrayList<>();
    }
    return this.infos;
  }

  public boolean hasGaid() {
    return !StringUtils.isBlank(this.gaId);
  }

  @JsonIgnore
  public List<String> getArticleGroups() {
    return Lists.newArrayList(sagProductGroup, sagProductGroup2, sagProductGroup3,
        sagProductGroup4);
  }

  public Double getFinalCustomerNetPrice() {
    return Optional.ofNullable(price).map(PriceWithArticle::getFinalCustomerNetPrice).orElse(null);
  }

  public Double getTotalFinalCustomerNetPrice() {
    final Double finalCustomerNetPrice = getFinalCustomerNetPrice();
    if (Objects.isNull(amountNumber) || Objects.isNull(finalCustomerNetPrice)) {
      return null;
    }
    return amountNumber * finalCustomerNetPrice;
  }

  public void setVatRate(Double vatRate) {
    this.vatRate = Objects.isNull(vatRate) ? NumberUtils.DOUBLE_MINUS_ONE : vatRate;
  }

  public Double getFinalCustomerNetPriceWithVat() {
    return Optional.ofNullable(price).map(PriceWithArticle::getFinalCustomerNetPriceWithVat)
        .orElse(null);
  }

  public Double getTotalFinalCustomerNetPriceWithVat() {
    final Double finalCustomerNetPriceWithVat = getFinalCustomerNetPriceWithVat();
    if (Objects.isNull(finalCustomerNetPriceWithVat) || Objects.isNull(amountNumber)) {
      return null;
    }
    return amountNumber * finalCustomerNetPriceWithVat;
  }

  public void setFinalCustomerNetPrice(Double finalCustomerNetPrice) {
    if (price == null || price.getPrice() == null) {
      return;
    }
    price.getPrice().setFinalCustomerNetPrice(finalCustomerNetPrice);
  }

  public List<ArticleCriteriaDto> getCriteria() {
    return CollectionUtils.emptyIfNull(this.criteria).stream()
        .sorted(Comparator.comparing(ArticleCriteriaDto::getCsort).thenComparing(
            ArticleCriteriaDto::getCndech, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
        .collect(Collectors.toList());
  }
  
  public Double getDeliverableStock() {
    return Optional.ofNullable(this.deliverableStock).orElse(NumberUtils.DOUBLE_ZERO);
  }

}
