package com.sagag.services.dvse.builder;

import java.util.Optional;

import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.NextWorkingDates;
import com.sagag.services.dvse.wsdl.dvse.AvailableState;

/**
 * Interface of article availability state builder for DVSE.
 *
 */
@FunctionalInterface
public interface IAvailableStateBuilder {

  /**
   * Returns the available state of article info.
   *
   * @param articleOpt
   * @param affiliate
   * @param sendMethodEnum
   * @param nextWorkingDate
   * @return the result of {@link AvailableState}
   */
  AvailableState buildAvailableState(Optional<ArticleDocDto> articleOpt,
      final SupportedAffiliate affiliate, final ErpSendMethodEnum sendMethodEnum,
      final NextWorkingDates nextWorkingDate);
}
