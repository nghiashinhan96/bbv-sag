package com.sagag.services.autonet.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemoArticleSourceIdEnum {

  SEARCH(120), DETAIL(121);

  private int value;

  public static MemoArticleSourceIdEnum valueOf(boolean isDetailRequest) {
    return isDetailRequest ? DETAIL : SEARCH;
  }
}
