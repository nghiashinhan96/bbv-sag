package com.sagag.services.dvse.builder;

import java.util.Optional;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.dvse.wsdl.dvse.ArrayOfPrice;

/**
 * Interface of article price builder for DVSE.
 *
 */
@FunctionalInterface
public interface IArrayOfPriceBuilder {

  /**
   * Returns the array of price by source.
   *
   * @param articleOpt the input article optional
   * @return the result of {@link ArrayOfPrice}
   */
  ArrayOfPrice buildArrayOfPrice(Optional<ArticleDocDto> articleOpt);
}
