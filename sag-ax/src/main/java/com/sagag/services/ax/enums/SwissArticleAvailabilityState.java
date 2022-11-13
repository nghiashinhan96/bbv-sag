package com.sagag.services.ax.enums;

import java.util.Optional;
import java.util.stream.Stream;

import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.article.api.availability.IArticleAvailabilityState;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SwissArticleAvailabilityState implements IArticleAvailabilityState {

  IMMEDIATE(1),
  TOUR_PLUS_ONE(2),
  SAME_DAY(3),
  NEXT_DAY(4),
  EXTENDED(5),
  NOT_AVAILABLE(6),
  IN_24_HOURS(10);

  private int code;

  @Override
  public ArticleAvailabilityResult toResult() {
    boolean isExtended = EXTENDED == this;
    return new ArticleAvailabilityResult(this.name(), this.code, isExtended);
  }

  public static Optional<SwissArticleAvailabilityState> fromCode(int code) {
    return Stream.of(values()).filter(value -> value.code == code).findFirst();
  }

}
