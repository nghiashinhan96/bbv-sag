package com.sagag.services.dvse.builder;

import java.util.Arrays;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.ax.availability.processor.AustriaArticleAvailabilityProcessor;
import com.sagag.services.ax.enums.AustriaArticleAvailabilityState;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.NextWorkingDates;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.dvse.wsdl.dvse.AvailableState;

import lombok.extern.slf4j.Slf4j;

/**
 * UT for {@link DvseAvailableStateBuilder}.
 */
@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class DvseAvailableStateBuilderTest {

  private static final String WARN_NPE_MSG = "The builder object is null";

  @InjectMocks
  private AtDvseAvailableStateBuilder builder;

  @Mock
  private AustriaArticleAvailabilityProcessor austriaArticleAvailabilityProcessor;

  @Test
  public void givenNullArt_shouldReturnYellowForDdat() {
    final Optional<ArticleDocDto> articleOpt = Optional.empty();
    final SupportedAffiliate affiliate = SupportedAffiliate.DERENDINGER_AT;
    final ErpSendMethodEnum sendMethod = ErpSendMethodEnum.PICKUP;
    final NextWorkingDates nextWorkingDate = buildNextWorkingDates(DateTime.now().toString());

    final AustriaArticleAvailabilityState expectedState = AustriaArticleAvailabilityState.YELLOW;
    Mockito.when(austriaArticleAvailabilityProcessor.getDefaultResult())
    .thenReturn(expectedState.toResult());

    if (builder == null) {
      log.warn(WARN_NPE_MSG);
      return;
    }

    final AvailableState state =
        builder.buildAvailableState(articleOpt, affiliate, sendMethod, nextWorkingDate);
    Assert.assertThat(state.getAvailState(), Matchers.equalTo(expectedState.getCode()));

    Mockito.verify(austriaArticleAvailabilityProcessor, Mockito.times(1)).getDefaultResult();
  }

  @Test
  public void givenEmptyAvails_shouldReturnYellowForDdat() {
    final Optional<ArticleDocDto> articleOpt = Optional.of(new ArticleDocDto());
    final SupportedAffiliate affiliate = SupportedAffiliate.DERENDINGER_AT;
    final ErpSendMethodEnum sendMethod = ErpSendMethodEnum.PICKUP;
    final NextWorkingDates nextWorkingDate = buildNextWorkingDates(DateTime.now().toString());

    final AustriaArticleAvailabilityState expectedState = AustriaArticleAvailabilityState.YELLOW;
    Mockito.when(austriaArticleAvailabilityProcessor.getDefaultResult())
    .thenReturn(expectedState.toResult());

    if (builder == null) {
      log.warn(WARN_NPE_MSG);
      return;
    }

    final AvailableState state =
        builder.buildAvailableState(articleOpt, affiliate, sendMethod, nextWorkingDate);
    Assert.assertThat(state.getAvailState(), Matchers.equalTo(expectedState.getCode()));

    Mockito.verify(austriaArticleAvailabilityProcessor, Mockito.times(1)).getDefaultResult();
  }

  @Test
  public void givenNoGrossPrice_shouldReturnYellowForDdat() {
    final ArticleDocDto article = new ArticleDocDto();
    final Availability availability = new Availability();
    article.setAvailabilities(Arrays.asList(availability));
    article.setPrice(PriceWithArticle.empty());

    final Optional<ArticleDocDto> articleOpt = Optional.of(article);
    final SupportedAffiliate affiliate = SupportedAffiliate.DERENDINGER_AT;
    final ErpSendMethodEnum sendMethod = ErpSendMethodEnum.PICKUP;
    final DateTime nextWorkingTour = DateTime.now();
    final NextWorkingDates nextWorkingDate = buildNextWorkingDates(nextWorkingTour.toString());

    final AustriaArticleAvailabilityState greenState = AustriaArticleAvailabilityState.GREEN;
    Mockito.when(austriaArticleAvailabilityProcessor.process(availability, sendMethod, nextWorkingTour))
    .thenReturn(greenState.toResult());

    if (builder == null) {
      log.warn(WARN_NPE_MSG);
      return;
    }

    final AustriaArticleAvailabilityState expectedState = AustriaArticleAvailabilityState.YELLOW;
    final AvailableState state =
        builder.buildAvailableState(articleOpt, affiliate, sendMethod, nextWorkingDate);
    Assert.assertThat(state.getAvailState(), Matchers.equalTo(expectedState.getCode()));

    Mockito.verify(austriaArticleAvailabilityProcessor, Mockito.times(1))
    .process(availability, sendMethod, nextWorkingTour);
  }

  @Test
  public void givenFullArticle_shouldReturnGreenForDdat() {
    final ArticleDocDto article = new ArticleDocDto();
    final Availability availability = new Availability();
    article.setAvailabilities(Arrays.asList(availability));
    PriceWithArticle price = PriceWithArticle.empty();
    price.getPrice().setGrossPrice(10.0d);
    article.setPrice(price);

    final Optional<ArticleDocDto> articleOpt = Optional.of(article);
    final SupportedAffiliate affiliate = SupportedAffiliate.DERENDINGER_AT;
    final ErpSendMethodEnum sendMethod = ErpSendMethodEnum.PICKUP;
    final DateTime nextWorkingTour = DateTime.now();
    final NextWorkingDates nextWorkingDate = buildNextWorkingDates(nextWorkingTour.toString());

    final AustriaArticleAvailabilityState expectedState = AustriaArticleAvailabilityState.GREEN;
    Mockito.when(austriaArticleAvailabilityProcessor.process(availability, sendMethod, nextWorkingTour))
    .thenReturn(expectedState.toResult());

    if (builder == null) {
      log.warn(WARN_NPE_MSG);
      return;
    }

    final AvailableState state =
        builder.buildAvailableState(articleOpt, affiliate, sendMethod, nextWorkingDate);
    Assert.assertThat(state.getAvailState(), Matchers.equalTo(expectedState.getCode()));

    Mockito.verify(austriaArticleAvailabilityProcessor, Mockito.times(1))
    .process(availability, sendMethod, nextWorkingTour);
  }

  @Test
  public void givenFullArticle_shouldReturnGreenWithAvailDescForMatat() {
    final ArticleDocDto article = new ArticleDocDto();
    final Availability availability = new Availability();
    article.setAvailabilities(Arrays.asList(availability));
    PriceWithArticle price = PriceWithArticle.empty();
    price.getPrice().setGrossPrice(10.0d);
    article.setPrice(price);

    final Optional<ArticleDocDto> articleOpt = Optional.of(article);
    final SupportedAffiliate affiliate = SupportedAffiliate.MATIK_AT;
    final ErpSendMethodEnum sendMethod = ErpSendMethodEnum.PICKUP;
    final DateTime nextWorkingTour = DateTime.now();
    final NextWorkingDates nextWorkingDate = buildNextWorkingDates(nextWorkingTour.toString());

    final AustriaArticleAvailabilityState expectedState = AustriaArticleAvailabilityState.GREEN;
    Mockito.when(austriaArticleAvailabilityProcessor.process(availability, sendMethod, nextWorkingTour))
    .thenReturn(expectedState.toResult());

    if (builder == null) {
      log.warn(WARN_NPE_MSG);
      return;
    }

    final AvailableState state =
        builder.buildAvailableState(articleOpt, affiliate, sendMethod, nextWorkingDate);
    Assert.assertThat(state.getAvailState(), Matchers.equalTo(expectedState.getCode()));
    Assert.assertThat(state.getAvailDescription(),
        Matchers.equalTo(AustriaArticleAvailabilityState.AVAIL_DESCRIPTION_MATIK));

    Mockito.verify(austriaArticleAvailabilityProcessor, Mockito.times(1))
    .process(availability, sendMethod, nextWorkingTour);
  }

  @Test
  public void givenFullArticleHasQwpSupplierName_shouldReturnBlueForDdat() {
    final ArticleDocDto article = new ArticleDocDto();
    final Availability availability = new Availability();
    article.setAvailabilities(Arrays.asList(availability));
    PriceWithArticle price = PriceWithArticle.empty();
    price.getPrice().setGrossPrice(10.0d);
    article.setPrice(price);
    article.setSupplier("QWP");

    final Optional<ArticleDocDto> articleOpt = Optional.of(article);
    final SupportedAffiliate affiliate = SupportedAffiliate.DERENDINGER_AT;
    final ErpSendMethodEnum sendMethod = ErpSendMethodEnum.PICKUP;
    final DateTime nextWorkingTour = DateTime.now();
    final NextWorkingDates nextWorkingDate = buildNextWorkingDates(nextWorkingTour.toString());

    final AustriaArticleAvailabilityState expectedState = AustriaArticleAvailabilityState.BLUE;
    Mockito.when(austriaArticleAvailabilityProcessor.process(availability, sendMethod, nextWorkingTour))
    .thenReturn(expectedState.toResult());

    if (builder == null) {
      log.warn(WARN_NPE_MSG);
      return;
    }

    final AvailableState state =
        builder.buildAvailableState(articleOpt, affiliate, sendMethod, nextWorkingDate);
    Assert.assertThat(state.getAvailState(), Matchers.equalTo(expectedState.getCode()));
    Assert.assertThat(state.getAvailDescription(),
        Matchers.equalTo(AustriaArticleAvailabilityState.AVAIL_DESCRIPTION_QWP));

    Mockito.verify(austriaArticleAvailabilityProcessor, Mockito.times(1))
    .process(availability, sendMethod, nextWorkingTour);
  }

  @Test
  public void givenFullArticleHasQwpSupplierId_shouldReturnBlueForDdat() {
    final ArticleDocDto article = new ArticleDocDto();
    final Availability availability = new Availability();
    article.setAvailabilities(Arrays.asList(availability));
    PriceWithArticle price = PriceWithArticle.empty();
    price.getPrice().setGrossPrice(10.0d);
    article.setPrice(price);
    article.setSupplierId(1962);

    final Optional<ArticleDocDto> articleOpt = Optional.of(article);
    final SupportedAffiliate affiliate = SupportedAffiliate.DERENDINGER_AT;
    final ErpSendMethodEnum sendMethod = ErpSendMethodEnum.PICKUP;
    final DateTime nextWorkingTour = DateTime.now();
    final NextWorkingDates nextWorkingDate = buildNextWorkingDates(nextWorkingTour.toString());

    final AustriaArticleAvailabilityState expectedState = AustriaArticleAvailabilityState.BLUE;
    Mockito.when(austriaArticleAvailabilityProcessor.process(availability, sendMethod, nextWorkingTour))
    .thenReturn(expectedState.toResult());

    if (builder == null) {
      log.warn(WARN_NPE_MSG);
      return;
    }

    final AvailableState state =
        builder.buildAvailableState(articleOpt, affiliate, sendMethod, nextWorkingDate);
    Assert.assertThat(state.getAvailState(), Matchers.equalTo(expectedState.getCode()));
    Assert.assertThat(state.getAvailDescription(),
        Matchers.equalTo(AustriaArticleAvailabilityState.AVAIL_DESCRIPTION_QWP));

    Mockito.verify(austriaArticleAvailabilityProcessor, Mockito.times(1))
    .process(availability, sendMethod, nextWorkingTour);
  }

  @Test
  public void givenFullArticle_shouldReturnYellowForDdat() {
    final ArticleDocDto article = new ArticleDocDto();
    final Availability availability = new Availability();
    article.setAvailabilities(Arrays.asList(availability));
    PriceWithArticle price = PriceWithArticle.empty();
    price.getPrice().setGrossPrice(10.0d);
    article.setPrice(price);

    final Optional<ArticleDocDto> articleOpt = Optional.of(article);
    final SupportedAffiliate affiliate = SupportedAffiliate.DERENDINGER_AT;
    final ErpSendMethodEnum sendMethod = ErpSendMethodEnum.PICKUP;
    final DateTime nextWorkingTour = DateTime.now();
    final NextWorkingDates nextWorkingDate = buildNextWorkingDates(nextWorkingTour.toString());

    final AustriaArticleAvailabilityState expectedState = AustriaArticleAvailabilityState.YELLOW;
    Mockito.when(austriaArticleAvailabilityProcessor.process(availability, sendMethod, nextWorkingTour))
    .thenReturn(AustriaArticleAvailabilityState.IN_144_HOURS.toResult());

    if (builder == null) {
      log.warn(WARN_NPE_MSG);
      return;
    }

    final AvailableState state =
        builder.buildAvailableState(articleOpt, affiliate, sendMethod, nextWorkingDate);
    Assert.assertThat(state.getAvailState(), Matchers.equalTo(expectedState.getCode()));

    Mockito.verify(austriaArticleAvailabilityProcessor, Mockito.times(1))
    .process(availability, sendMethod, nextWorkingTour);
  }

  private static NextWorkingDates buildNextWorkingDates(String date) {
    return NextWorkingDates.builder()
        .backorderDate(DateTime.parse(date).toDate())
        .noBackorderDate(DateTime.parse(date).toDate())
        .build();
  }

}
