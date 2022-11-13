package com.sagag.services.article.api.price.finalcustomer.impl;

import com.sagag.eshop.repo.api.WssMarginsBrandRepository;
import com.sagag.eshop.repo.entity.WssMarginsBrand;
import com.sagag.services.article.api.price.finalcustomer.WssMarginConverter;
import com.sagag.services.article.api.price.finalcustomer.WssMarginValueFinder;
import com.sagag.services.common.domain.wss.WssMarginGroupValueDto;
import com.sagag.services.common.enums.WssMarginGroupEnum;
import com.sagag.services.common.utils.PageUtils;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WssMarginValueByBrandFinderImpl implements WssMarginValueFinder {

  @Autowired
  private WssMarginsBrandRepository wssMarginsBrandRepository;

  @Autowired
  private WssMarginConverter wssMarginConverter;

  @Override
  public double findMarginValue(Integer orgId, Integer marginGroupNumber, Integer brandId,
      List<String> articleGroups) {
    List<WssMarginsBrand> wssMarginsBrandGroupSettings = wssMarginsBrandRepository
        .findFirstOrDefaultByOrgIdAndBrandId(orgId, brandId, PageUtils.FIRST_ITEM_PAGE);
    if (wssMarginsBrandGroupSettings.isEmpty()) {
      return NumberUtils.DOUBLE_ZERO.doubleValue();
    }
    WssMarginsBrand marginBrandSetting = wssMarginsBrandGroupSettings.get(0);
    WssMarginGroupValueDto marginDto = wssMarginConverter.fromWssMarginBrand(marginBrandSetting);
    WssMarginGroupEnum marginGroup = WssMarginGroupEnum.findByValue(marginGroupNumber);
    if (marginGroup == null) {
      marginGroup = WssMarginGroupEnum.MARGIN_COL_1;
    }
    return defaultMarginValue(marginGroup.getMarginColValue(marginDto));
  }

}
