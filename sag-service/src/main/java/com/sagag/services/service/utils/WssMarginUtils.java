package com.sagag.services.service.utils;

import com.sagag.eshop.service.dto.WssMarginArticleGroupDto;
import com.sagag.eshop.service.dto.WssMarginBrandDto;
import com.sagag.services.domain.eshop.dto.WssCsvMarginArticleGroup;
import com.sagag.services.domain.eshop.dto.WssCsvMarginBrand;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class WssMarginUtils {

  private static final int MIN_MARGIN_VALUE = 0;

  private static final double MAX_MARGIN_VALUE = 99.9;

  public static boolean validMarginValues(WssCsvMarginArticleGroup marginArticleGroup) {
    return validMarginValues(marginArticleGroup.getMargin1(), marginArticleGroup.getMargin2(),
        marginArticleGroup.getMargin3(), marginArticleGroup.getMargin4(),
        marginArticleGroup.getMargin5(), marginArticleGroup.getMargin6(),
        marginArticleGroup.getMargin7());
  }

  public static boolean validMarginValues(WssCsvMarginBrand marginBrand) {
    return validMarginValues(marginBrand.getMargin1(), marginBrand.getMargin2(),
        marginBrand.getMargin3(), marginBrand.getMargin4(), marginBrand.getMargin5(),
        marginBrand.getMargin6(), marginBrand.getMargin7());
  }

  public static boolean validMarginValues(WssMarginArticleGroupDto wssMarginArticleGroup) {
    return validMarginValues(wssMarginArticleGroup.getMargin1(), wssMarginArticleGroup.getMargin2(),
        wssMarginArticleGroup.getMargin3(), wssMarginArticleGroup.getMargin4(),
        wssMarginArticleGroup.getMargin5(), wssMarginArticleGroup.getMargin6(),
        wssMarginArticleGroup.getMargin7());
  }

  public static boolean validMarginValues(WssMarginBrandDto wssMarginBrand) {
    return validMarginValues(wssMarginBrand.getMargin1(), wssMarginBrand.getMargin2(),
        wssMarginBrand.getMargin3(), wssMarginBrand.getMargin4(), wssMarginBrand.getMargin5(),
        wssMarginBrand.getMargin6(), wssMarginBrand.getMargin7());
  }

  private boolean validMarginValues(Double margin1, Double margin2, Double margin3, Double margin4,
      Double margin5, Double margin6, Double margin7) {
    return validMarginValue(margin1) && validMarginValue(margin2) && validMarginValue(margin3)
        && validMarginValue(margin4) && validMarginValue(margin5) && validMarginValue(margin6)
        && validMarginValue(margin7);
  }

  private boolean validMarginValue(Double marginValue) {
    return marginValue != null && marginValue >= MIN_MARGIN_VALUE
        && marginValue <= MAX_MARGIN_VALUE;
  }
}
