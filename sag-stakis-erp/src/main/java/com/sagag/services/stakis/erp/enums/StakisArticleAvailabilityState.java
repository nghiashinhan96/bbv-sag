package com.sagag.services.stakis.erp.enums;

import java.util.Arrays;

import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.article.api.availability.IArticleAvailabilityState;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StakisArticleAvailabilityState implements IArticleAvailabilityState {

  GREEN(2, "availability.text.in_stock_branch"),
  YELLOW(3, "availability.text.partly_available"),
  RED(4, "availability.text.unavailable"),
  YELLOW_GREEN(5, "availability.text.in_stock_central_warehouse"),
  BLACK(9, "availability.text.non_orderale");

  private int code;

  private String msgCode;

  public static StakisArticleAvailabilityState fromCode(int code) {
    return Arrays.asList(values()).stream().filter(item -> code == item.getCode()).findFirst()
        .orElse(BLACK);
  }

  @Override
  public ArticleAvailabilityResult toResult() {
    return new ArticleAvailabilityResult(this.name(), this.code, false);
  }

}
