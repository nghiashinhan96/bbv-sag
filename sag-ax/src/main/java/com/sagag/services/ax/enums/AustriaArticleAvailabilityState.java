package com.sagag.services.ax.enums;

import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.article.api.availability.IArticleAvailabilityState;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * Get article price, availability and details from sagsys.
 * </p>
 *
 * <p>
 * Following availability states/codes are defined in current dvse shop
 * </p>
 *
 * <pre>
 *    green => 1
 *    yellow => 3
 *    yellow_green => 4
 *    yellow_orange => 7
 * </pre>
 */
@Getter
@AllArgsConstructor
public enum AustriaArticleAvailabilityState implements IArticleAvailabilityState {

  GREEN(1), YELLOW(3), YELLOW_GREEN(4), YELLOW_ORANGE(7), BLUE(9), IN_144_HOURS(10), NO_STOCK(11);

  public static final String AVAIL_DESCRIPTION_MATIK =
      "*Lieferzeit anfragen, eventuell Frachtkosten*";
  public static final String AVAIL_DESCRIPTION_QWP =
      "Sonderbestellung ! Lieferzeit bis zu 48 Stunden (vorbehaltlich Verfügbarkeit)";

  private int code;

  @Override
  public ArticleAvailabilityResult toResult() {
    boolean isExtended = YELLOW == this;
    return new ArticleAvailabilityResult(this.name(), this.code, isExtended);
  }
}
