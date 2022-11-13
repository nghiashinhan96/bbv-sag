package com.sagag.services.dvse.builder;

import java.util.Objects;
import java.util.Optional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.ax.availability.processor.SwissArticleAvailabilityProcessor;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.ChProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.NextWorkingDates;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.dvse.enums.DvseChAvailabilityState;
import com.sagag.services.dvse.utils.DvseArticleUtils;
import com.sagag.services.dvse.wsdl.dvse.AvailableState;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation class of DVSE array of availability state builder.
 *
 */

@Component
@ChProfile
@Slf4j
public class ChDvseAvailableStateBuilder implements IAvailableStateBuilder {

  @Autowired
  private SwissArticleAvailabilityProcessor articleAvailabilityProcessor;

  @Override
  public AvailableState buildAvailableState(Optional<ArticleDocDto> articleOpt,
      final SupportedAffiliate affiliate, ErpSendMethodEnum sendMethodEnum,
      NextWorkingDates nextWorkingDate) {
    if (!articleOpt.isPresent() || !articleOpt.get().hasAvailabilities()) {
      AvailableState availState = new AvailableState();
      availState.setAvailState(DvseChAvailabilityState.YELLOW.getCode());
      return availState;
    }
    final ArticleDocDto article = articleOpt.get();
    // #5031
    final Availability lastAvailability = article.findAvailWithLatestTime();
    DateTime lastTour =
        DvseArticleUtils.getLastTourOfLastAvail(lastAvailability, nextWorkingDate);
    log.info(
        "\nChDvseAvailableStateBuilder lastAvailability {} \nChDvseAvailableStateBuilder nextWorkingDate {} ",
        lastAvailability, nextWorkingDate);
    final ArticleAvailabilityResult result =
        articleAvailabilityProcessor.process(lastAvailability, sendMethodEnum, lastTour);
    return createAvailableState(article, lastTour, result);
  }

  private static AvailableState createAvailableState(final ArticleDocDto article, DateTime lastTour,
      ArticleAvailabilityResult result) {

    AvailableState availState = new AvailableState();
    final Availability lastAvailability = article.findAvailWithLatestTime();
    final String arrivalTimeStr = lastAvailability.getArrivalTime();
    if (Objects.isNull(arrivalTimeStr)) {
      availState.setAvailState(DvseChAvailabilityState.YELLOW.getCode());
      return availState;
    }
    DvseChAvailabilityState
        .fromAricleAvailability(lastAvailability, lastTour, result.getAvailablityStateCode())
        .ifPresent(state -> availState.setAvailState(state.getCode()));

    if (DvseArticleUtils.equalsQwpSupplier(article)) {
      log.info(
          "\nChDvseAvailableStateBuilder articleSupplier {} \nChDvseAvailableStateBuilder articleSupplierId {} ",
          article.getSupplier(), article.getSupplierId());
      availState.setAvailDescription(DvseChAvailabilityState.AVAIL_DESCRIPTION_QWP);
      availState.setAvailState(DvseChAvailabilityState.BLUE.getCode());
    }

    if (!article.hasGrossPrice()) {
      availState.setAvailState(DvseChAvailabilityState.YELLOW.getCode());
    }

    return availState;
  }

}
