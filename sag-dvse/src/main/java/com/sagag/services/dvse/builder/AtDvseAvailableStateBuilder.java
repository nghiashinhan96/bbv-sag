package com.sagag.services.dvse.builder;

import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.ax.availability.processor.AustriaArticleAvailabilityProcessor;
import com.sagag.services.ax.enums.AustriaArticleAvailabilityState;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.AtSbProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.NextWorkingDates;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.dvse.utils.DvseArticleUtils;
import com.sagag.services.dvse.wsdl.dvse.AvailableState;

/**
 * Implementation class of DVSE array of availability state builder.
 *
 */
@Component
@AtSbProfile
public class AtDvseAvailableStateBuilder implements IAvailableStateBuilder {

  @Autowired
  private AustriaArticleAvailabilityProcessor articleAvailabilityProcessor;

  @Override
  public AvailableState buildAvailableState(Optional<ArticleDocDto> articleOpt,
      final SupportedAffiliate affiliate, ErpSendMethodEnum sendMethodEnum,
      NextWorkingDates nextWorkingDate) {
    if (!articleOpt.isPresent() || !articleOpt.get().hasAvailabilities()) {
      return createAvailableState(articleAvailabilityProcessor.getDefaultResult(), null, affiliate);
    }
    final ArticleDocDto article = articleOpt.get();
    // #5031
    final Availability lastAvailability = article.findAvailWithLatestTime();
    DateTime lastTour =
        DvseArticleUtils.getLastTourOfLastAvail(lastAvailability, nextWorkingDate);
    final ArticleAvailabilityResult result =
        articleAvailabilityProcessor.process(lastAvailability, sendMethodEnum, lastTour);

    return createAvailableState(result, article, affiliate);
  }

  private static AvailableState createAvailableState(ArticleAvailabilityResult result,
      final ArticleDocDto articleDocDto, final SupportedAffiliate affiliate) {
    AvailableState availState = new AvailableState();
    // From #2855: we have new code for IN_24_HOURS cases is 10, It match other code in DVSE side.
    // #2963: we will adapt with availStateCode = 10, we adjust this to 4(YELLOW/GREEN)
    final boolean backOrderCase = NumberUtils.compare(AustriaArticleAvailabilityState
        .IN_144_HOURS.getCode(), result.getAvailablityStateCode()) == 0;
    if (backOrderCase) {
      availState.setAvailState(AustriaArticleAvailabilityState.YELLOW.getCode());
    } else {
      availState.setAvailState(result.getAvailablityStateCode());
    }

    if (Objects.isNull(articleDocDto)) {
      return availState;
    }

    if (!articleDocDto.hasGrossPrice()) { // maybe always not null
      availState.setAvailState(AustriaArticleAvailabilityState.YELLOW.getCode());
    }

    final String arrivalTimeStr = articleDocDto.findAvailWithLatestTime().getArrivalTime();
    if (Objects.nonNull(arrivalTimeStr)) {
      return availState;
    }

    // need to check Matik and arrival time == null
    if (SupportedAffiliate.MATIK_AT == affiliate) {
      availState.setAvailDescription(AustriaArticleAvailabilityState.AVAIL_DESCRIPTION_MATIK);
    } else if (DvseArticleUtils.equalsQwpSupplier(articleDocDto)) {
      availState.setAvailDescription(AustriaArticleAvailabilityState.AVAIL_DESCRIPTION_QWP);
      // Blue color lamp return code 9
      availState.setAvailState(AustriaArticleAvailabilityState.BLUE.getCode());
    }

    return availState;
  }
}
