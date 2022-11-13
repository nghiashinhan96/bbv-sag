package com.sagag.services.stakis.erp.availability;

import java.util.Optional;
import java.util.stream.Stream;

import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.article.api.availability.IArticleAvailabilityState;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CzArticleAvailabilityState implements IArticleAvailabilityState {

  AVAILABLE(1), NOT_AVAILABLE(2), PARTIALLY_AVAILABLE(3), EXTENDED(4);

  private int code;

  @Override
  public ArticleAvailabilityResult toResult() {
    boolean isExtended = EXTENDED == this;
    return new ArticleAvailabilityResult(this.name(), this.code, isExtended);
  }

  public static Optional<CzArticleAvailabilityState> fromCode(int code) {
    return Stream.of(values()).filter(value -> value.code == code).findFirst();
  }

}
