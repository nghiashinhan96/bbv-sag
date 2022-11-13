package com.sagag.services.ax.availability.externalvendor;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExternalVendorUtils {

  public static boolean matchSagProductGroups(String externalVendorSagArticleGroup,
      List<String> articleSagProductGroups) {
    return StringUtils.isBlank(externalVendorSagArticleGroup)
        || articleSagProductGroups.contains(externalVendorSagArticleGroup);
  }

  public static boolean matchBrandId(Long externalVendorBrandId, String articleIdProductBrand) {
    return Objects.isNull(externalVendorBrandId)
        || String.valueOf(externalVendorBrandId).equals(articleIdProductBrand);
  }

  public static boolean matchVendorId(String vendorId, String externalVendorId) {
    return Objects.isNull(externalVendorId) || Objects.equals(vendorId, externalVendorId);
  }

  public static Comparator<ExternalVendorDto> compareByLayerRule() {
    // #3426
    return (ven1, ven2) -> {
      int vendorCompared = compareVendorId(ven1.getVendorId(), ven2.getVendorId());
      if (vendorCompared != 0) {
        return vendorCompared;
      }

      int brandCompared = compareBrandId(ven1.getBrandId(), ven2.getBrandId());
      if (brandCompared != 0) {
        return brandCompared;
      }

      return compareSagArticleGroup(ven1.getSagArticleGroup(), ven2.getSagArticleGroup());
    };
  }

  private static int compareVendorId(String vendorId1, String vendorId2) {
    if (Objects.nonNull(vendorId1) && Objects.isNull(vendorId2)) {
      return -1;
    }
    if (Objects.isNull(vendorId1) && Objects.nonNull(vendorId2)) {
      return 1;
    }
    return 0;
  }

  private static int compareBrandId(Long brandId1, Long brandId2) {
    if (Objects.nonNull(brandId1) && Objects.isNull(brandId2)) {
      return -1;
    }
    if (Objects.isNull(brandId1) && Objects.nonNull(brandId2)) {
      return 1;
    }
    return 0;
  }

  private static int compareSagArticleGroup(String sagArticleGroup1, String sagArticleGroup2) {
    if (StringUtils.isNotEmpty(sagArticleGroup1) && StringUtils.isEmpty(sagArticleGroup2)) {
      return -1;
    }
    if (StringUtils.isEmpty(sagArticleGroup1) && StringUtils.isNotEmpty(sagArticleGroup2)) {
      return 1;
    }
    return 0;
  }
}
