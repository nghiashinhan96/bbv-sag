package com.sagag.services.article.api.price.finalcustomer.impl;

import com.sagag.eshop.repo.api.WssMarginsArticleGroupRepository;
import com.sagag.eshop.repo.entity.WssMarginsArticleGroup;
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
public class WssMarginValueByArticleGroupFinderImpl implements WssMarginValueFinder {

  @Autowired
  private WssMarginsArticleGroupRepository wssMarginsArticleGroupRepository;

  @Autowired
  private WssMarginConverter wssMarginConverter;

  @Override
  public double findMarginValue(Integer orgId, Integer marginGroupNumber, Integer brandId,
      List<String> articleGroups) {
    List<WssMarginsArticleGroup> wssMarginsArticleGroups =
        wssMarginsArticleGroupRepository.findFirstOrDefaultSettingByOrgIdAndSagArticleGroupIds(
            orgId, articleGroups, PageUtils.FIRST_ITEM_PAGE);
    if (wssMarginsArticleGroups.isEmpty()) {
      return NumberUtils.DOUBLE_ZERO.doubleValue();
    }
    WssMarginsArticleGroup marginArticleGroupSetting = wssMarginsArticleGroups.get(0);
    WssMarginGroupValueDto marginDto =
        wssMarginConverter.fromWssMarginArticleGroup(marginArticleGroupSetting);
    WssMarginGroupEnum marginGroup = WssMarginGroupEnum.findByValue(marginGroupNumber);
    if (marginGroup == null) {
      marginGroup = WssMarginGroupEnum.MARGIN_COL_1;
    }
    return defaultMarginValue(marginGroup.getMarginColValue(marginDto));
  }

}
