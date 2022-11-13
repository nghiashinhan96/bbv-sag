package com.sagag.services.autonet.erp.enums;

import java.util.Arrays;

import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.article.api.availability.IArticleAvailabilityState;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AutonetArticleAvailabilityState implements IArticleAvailabilityState {

  BLACK(0), GREEN(1), RED(2), BLUE(3), YELLOW(4), YELLOW_GREEN(5), NOT_AVAILABLE_GRAY(6);

  private int code;

  public static AutonetArticleAvailabilityState fromCode(int code) {
    return Arrays.asList(values()).stream()
        .filter(item -> code == item.getCode())
        .findFirst().orElse(NOT_AVAILABLE_GRAY);
  }

  @Override
  public ArticleAvailabilityResult toResult() {
    return new ArticleAvailabilityResult(this.name(), this.code, false);
  }
}
