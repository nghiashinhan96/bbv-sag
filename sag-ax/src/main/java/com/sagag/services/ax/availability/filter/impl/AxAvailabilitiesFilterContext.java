package com.sagag.services.ax.availability.filter.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.eshop.repo.api.BranchOpeningTimeRepository;
import com.sagag.eshop.repo.api.SupportedAffiliateRepository;
import com.sagag.eshop.repo.entity.BranchOpeningTime;
import com.sagag.services.article.api.availability.AdditionalArticleAvailabilityCriteria;
import com.sagag.services.article.api.availability.AvailabilitiesFilterContext;
import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.WorkingHours;
import com.sagag.services.ax.availability.stock.ArticlesStockAvailabilitiesFilterContext;
import com.sagag.services.ax.availability.tourtime.NextWorkingDateHelper;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.ax.utils.AxBranchUtils;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.profiles.DynamicAxProfile;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;

import lombok.extern.slf4j.Slf4j;

@Component
@DynamicAxProfile
@Slf4j
public class AxAvailabilitiesFilterContext implements AvailabilitiesFilterContext {

  @Autowired
  private SupportedAffiliateRepository supportedAffRepo;

  @Autowired
  private BranchOpeningTimeRepository branchOpeningTimeRepo;

  @Autowired
  private ArticlesStockAvailabilitiesFilterContext articlesStockAvailabilitiesFilterContext;

  @Autowired
  private NextWorkingDateHelper nextWorkingDateHelper;

  @Override
  @LogExecutionTime
  public List<ArticleDocDto> doFilterAvailabilities(List<ArticleDocDto> originalArticles,
      ArticleSearchCriteria criteria) {
    if (CollectionUtils.isEmpty(originalArticles) || criteria == null) {
      return Lists.newArrayList();
    }

    final List<ArticleDocDto> articles = modifyAndSumOfAmountArticleProcessor()
      .apply(originalArticles);

    final String deliveryType = criteria.getDeliveryType();
    final boolean isFinalCustomerUser = criteria.isFinalCustomerUser();
    final WssDeliveryProfileDto wssDeliveryProfile = criteria.getWssDeliveryProfile();
    final ErpSendMethodEnum sendMethod = defaultErpSendMethodIfFinalCustomer(deliveryType,
      isFinalCustomerUser, wssDeliveryProfile);

    final String branchId = AxBranchUtils.defaultBranchId(sendMethod, criteria.getPickupBranchId(),
        criteria.getDefaultBrandId());
    final List<TourTimeDto> tourTimeList = criteria.getCustomerTourTimes();
    final List<WorkingHours> workingHours = getWorkingHours(branchId);

    final AdditionalArticleAvailabilityCriteria availCriteria =
        new AdditionalArticleAvailabilityCriteria(tourTimeList, workingHours, branchId);
    final String countryName = supportedAffRepo.findCountryShortNameByAffShortName(
      criteria.getAffiliateShortName());
    final List<VendorDto> axVendors =
      ListUtils.defaultIfNull(criteria.getVendors(), Lists.newArrayList());
    final List<ArticleDocDto> filteredArticles = new ArrayList<>();

    final Optional<Date> nextWorkingDateForToday =
        getNextWorkingDate(criteria.getAffiliate().getCompanyName(), branchId);
    criteria.setNextWorkingDateForToday(nextWorkingDateForToday);

    articlesStockAvailabilitiesFilterContext.doFilterStockAvailabilities(filteredArticles, articles,
        countryName, criteria, availCriteria, axVendors);

    return AxArticleUtils.updateArticlesAvailability(originalArticles,
        filteredArticles);
  }

  private static UnaryOperator<List<ArticleDocDto>> modifyAndSumOfAmountArticleProcessor() {
    return originalArticles -> originalArticles.stream().map(SerializationUtils::clone)
      .collect(Collectors.groupingBy(ArticleDocDto::getIdSagsys)).values().stream()
      .map(groupArticlesCombinator()).collect(Collectors.toList());
  }

  private static Function<List<ArticleDocDto>, ArticleDocDto> groupArticlesCombinator() {
    return articles -> {
      final ArticleDocDto article = articles.get(0);
      article.setAmountNumber(
        articles.stream().collect(Collectors.summingInt(ArticleDocDto::getAmountNumber)));
      return article;
    };
  }

  private List<WorkingHours> getWorkingHours(String branchId) {
    final List<BranchOpeningTime> branchOpeningTimeList =
        branchOpeningTimeRepo.findByBranchNr(NumberUtils.createInteger(branchId));
    if (CollectionUtils.isEmpty(branchOpeningTimeList)) {
      throw new IllegalArgumentException("Branch should be present in db");
    }
    return branchOpeningTimeList.stream().map(branchOpeningTimeConverter())
        .collect(Collectors.toList());
  }

  private static Function<? super BranchOpeningTime, ? extends WorkingHours> branchOpeningTimeConverter() {
    return branchOpeningTime -> WorkingHours.builder()
        .openingTime(DateUtils.toLocalTime(branchOpeningTime.getOpeningTime()))
        .closingTime(DateUtils.toLocalTime(branchOpeningTime.getClosingTime()))
        .lunchStartTime(DateUtils.toLocalTime(branchOpeningTime.getLunchStartTime()))
        .lunchEndTime(DateUtils.toLocalTime(branchOpeningTime.getLunchEndTime()))
        .weekDay(branchOpeningTime.getWeekDay()).build();
  }

  private static ErpSendMethodEnum defaultErpSendMethodIfFinalCustomer(String deliveryType,
    boolean isFinalCustomerUser, final WssDeliveryProfileDto deliveryProfile) {
    if (isFinalCustomerUser && deliveryProfile != null) {
      return ErpSendMethodEnum.TOUR;
    }
    return ErpSendMethodEnum.valueOf(deliveryType);
  }

  private Optional<Date> getNextWorkingDate(String companyName, String pickupBranchId) {
    final Calendar calendar = Calendar.getInstance();
    final Date requestDate = calendar.getTime();
    log.debug("User requests after the close Of Business Time");
    return Optional.ofNullable(nextWorkingDateHelper
        .getNextWorkingDate(companyName, pickupBranchId, requestDate));
  }
}
