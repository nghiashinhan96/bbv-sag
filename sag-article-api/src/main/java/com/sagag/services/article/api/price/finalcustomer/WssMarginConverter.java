package com.sagag.services.article.api.price.finalcustomer;

import org.springframework.stereotype.Component;

import com.sagag.eshop.repo.entity.WssMarginsArticleGroup;
import com.sagag.eshop.repo.entity.WssMarginsBrand;
import com.sagag.services.common.domain.wss.WssMarginGroupValueDto;

@Component
public class WssMarginConverter {

  public WssMarginGroupValueDto fromWssMarginBrand(WssMarginsBrand entity) {
    return WssMarginGroupValueDto.builder().margin1(entity.getMargin1()).margin2(entity.getMargin2())
        .margin3(entity.getMargin3()).margin4(entity.getMargin4()).margin5(entity.getMargin5())
        .margin6(entity.getMargin6()).margin7(entity.getMargin7()).build();
  }

  public WssMarginGroupValueDto fromWssMarginArticleGroup(WssMarginsArticleGroup entity) {
    return WssMarginGroupValueDto.builder().margin1(entity.getMargin1()).margin2(entity.getMargin2())
        .margin3(entity.getMargin3()).margin4(entity.getMargin4()).margin5(entity.getMargin5())
        .margin6(entity.getMargin6()).margin7(entity.getMargin7()).build();
  }
}
