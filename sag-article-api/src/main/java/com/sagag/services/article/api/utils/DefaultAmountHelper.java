package com.sagag.services.article.api.utils;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.VehicleUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticleQuantityDto;
import com.sagag.services.domain.eshop.dto.VehGenArtArticleDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.eshop.dto.VehicleGenArtArtDto;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

/**
 * Utility class to get default amout number of articles
 *
 * <pre>
 * Article menge:
 * menge: Custom defined sales quantities array separated by comma "," . Eg: 2, vehicle_zylinder, 4, 6
 * genart: Custom genart id separated by comma ",". If separated by space " ", genart will have same menge. Eg: 82, 243 686, 40, 41
 * menge and genart should be consistency
 *
 * Base on:
 * article:
 *   salesquantity:
 *     default: 1
 *     genart: 78 82, 243 686, 11697 1169710 116971 11698 1169810 116981 11699 1169910 116991
 *     menge: 2, vehicle_zylinder, 4
 * </pre>
 */
@UtilityClass
public final class DefaultAmountHelper {

  private static final AmountNumberInfo AMOUNT_NUBMER_TWO =
      new AmountNumberInfo(2, Arrays.asList("82"));

  private static final AmountNumberInfo AMOUNT_NUBMER_FOUR =
      new AmountNumberInfo(4, Arrays.asList("11697", "1169710", "116971", "11698", "1169810",
          "116981", "11699", "1169910", "116991"));

  private static final AmountNumberInfo AMOUNT_NUBMER_VEHICLE_ZYLINDER =
      new AmountNumberInfo(Arrays.asList("243", "686"));

  // #2761: Management of Quantities
  public static void updateArticleQuantities(final ArticleDocDto article,
      final Optional<VehicleDto> vehicleOpt, Optional<VehicleGenArtArtDto> fitment,
      final SupportedAffiliate affiliate) {

    final ArticleQuantityDto articleQty = getArticleQtyByCountry(article, affiliate);

    if (Objects.isNull(articleQty.getQtyLowest()) && Objects.isNull(articleQty.getQtyMultiple())) {
      article.setSalesQuantity(getArticleSalesQuantity(article.getGaId(), vehicleOpt));
      article.setQtyMultiple(SagConstants.DEFAULT_MULTIPLE_QUANTITY);
      return;
    }

    final int qtyLowest =
        Objects.isNull(articleQty.getQtyLowest()) ? SagConstants.DEFAULT_SALES_QUANTITY
            : articleQty.getQtyLowest();

    int qtyMultiple =
        Objects.isNull(articleQty.getQtyMultiple()) ? qtyLowest : articleQty.getQtyMultiple();

    int salesQuantity;

    if (vehicleOpt.isPresent() && VehicleUtils.isValidVehId(vehicleOpt.get().getVehId())) {
      salesQuantity =
          getMengeValueForFitment(fitment, article.getIdSagsys(), qtyLowest);
      qtyMultiple = NumberUtils.max(qtyMultiple, qtyLowest);
    } else {
      salesQuantity =
          Objects.isNull(articleQty.getQtyStandard()) ? SagConstants.DEFAULT_SALES_QUANTITY
              : articleQty.getQtyStandard();
    }

    article.setSalesQuantity(NumberUtils.max(salesQuantity, qtyMultiple));
    article.setQtyMultiple(qtyMultiple);
  }

  private static Integer getMengeValueForFitment(final Optional<VehicleGenArtArtDto> fitmentOpt,
      final String idSagsys, final Integer defaultValue) {
    if (!fitmentOpt.isPresent() || CollectionUtils.isEmpty(fitmentOpt.get().getArticles())) {
      return defaultValue;
    }

    final List<VehGenArtArticleDto> articleByFitment = fitmentOpt.get().getArticles().stream()
        .filter(article -> idSagsys.equals(article.getIdSagsys())).collect(Collectors.toList());

    if (CollectionUtils.isEmpty(articleByFitment)) {
      return defaultValue;
    }

    final OptionalInt requiredQty = articleByFitment.get(0).getCriteria()
        .stream().filter(criteria -> SagConstants.REQUIRED_QUANTITY_CID.equals(criteria.getCid()))
        .mapToInt(item -> NumberUtils.toInt(item.getCvp(), defaultValue)).max();

    if (!requiredQty.isPresent()) {
      return defaultValue;
    }

    return NumberUtils.max(requiredQty.getAsInt(), defaultValue);

  }

  private ArticleQuantityDto getArticleQtyByCountry(final ArticleDocDto article,
      final SupportedAffiliate affiliate) {
    switch (affiliate) {
      case DERENDINGER_CH:
      case TECHNOMAG:
      case MATIK_CH:
      case WBB:
        return ArticleQuantityDto.builder().qtyLowest(article.getQtyLowestCh())
            .qtyMultiple(article.getQtyMultipleCh()).qtyStandard(article.getQtyStandardCh())
            .build();
      case DERENDINGER_AT:
      case MATIK_AT:
        return ArticleQuantityDto.builder().qtyLowest(article.getQtyLowestAt())
            .qtyMultiple(article.getQtyMultipleAt()).qtyStandard(article.getQtyStandardAt())
            .build();
      case RBE:
        return ArticleQuantityDto.builder().qtyLowest(article.getQtyLowestBe())
            .qtyMultiple(article.getQtyMultipleBe()).qtyStandard(article.getQtyStandardBe())
            .build();
      case STAKIS_CZECH:
        return ArticleQuantityDto.builder().build();
      case SAG_CZECH:
        return ArticleQuantityDto.builder().qtyLowest(article.getQtyLowestCz())
            .qtyMultiple(article.getQtyMultipleCz()).qtyStandard(article.getQtyStandardCz())
            .build();
      default:
        return ArticleQuantityDto.builder().qtyLowest(article.getQtyLowest())
            .qtyMultiple(article.getQtyMultiple()).qtyStandard(article.getQtyStandard())
            .build();
    }
  }

  public static int getArticleSalesQuantity(final String gaid,
      final Optional<VehicleDto> vehicleOpt) {

    if (AMOUNT_NUBMER_TWO.containsGenArt(gaid)) {
      return AMOUNT_NUBMER_TWO.getDefaultAmountNumber();
    }

    if (AMOUNT_NUBMER_FOUR.containsGenArt(gaid)) {
      return AMOUNT_NUBMER_FOUR.getDefaultAmountNumber();
    }

    if (AMOUNT_NUBMER_VEHICLE_ZYLINDER.containsGenArt(gaid) && vehicleOpt.isPresent()
        && !StringUtils.equalsIgnoreCase(SagConstants.KEY_NO_VEHICLE,
            vehicleOpt.get().getVehId())) {
      return defaultIfNull(vehicleOpt.get().getVehicleZylinder());
    }

    return SagConstants.DEFAULT_SALES_QUANTITY;
  }

  private static int defaultIfNull(Integer value) {
    return Objects.isNull(value) ? SagConstants.DEFAULT_SALES_QUANTITY : value.intValue();
  }

  public static Integer parseAmountNumberInteger(int amountNumber) {
    return (amountNumber > 0) ? amountNumber : null;
  }
}
