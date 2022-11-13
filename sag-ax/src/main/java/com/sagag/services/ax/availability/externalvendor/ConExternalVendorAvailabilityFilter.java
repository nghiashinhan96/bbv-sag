package com.sagag.services.ax.availability.externalvendor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.sagag.services.article.api.availability.AdditionalArticleAvailabilityCriteria;
import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.WorkingHours;
import com.sagag.services.ax.availability.filter.AxConArticleSearchCriteria;
import com.sagag.services.ax.availability.filter.AxConAvailabilityFilter;
import com.sagag.services.ax.domain.vendor.ExternalStockInfo;
import com.sagag.services.ax.utils.AxBranchUtils;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.profiles.DynamicAxProfile;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;
import com.sagag.services.domain.sag.erp.Availability;

@Component
@DynamicAxProfile
public class ConExternalVendorAvailabilityFilter extends AbstractExternalVendorAvailabilityFilter {

  @Autowired
  private AxConAvailabilityFilter axConAvailabilityFilter;

  @Override
  public List<AxAvailabilityType> availabilityTypes() {
    return Arrays.asList(AxAvailabilityType.CON);
  }

  @Override
  public List<ArticleDocDto> filter(List<ArticleDocDto> articles, ArticleSearchCriteria artCriteria,
      AdditionalArticleAvailabilityCriteria availCriteria, List<VendorDto> axVendors,
      String countryName) {

    if (!artCriteria.isDropShipment() || CollectionUtils.isEmpty(axVendors)) {
      return Lists.newArrayList();
    }

    List<ArticleDocDto> hasNotEnoughAvaiArticles = CollectionUtils.emptyIfNull(articles).stream()
        .filter(art -> !art.hasEnoughQuantity()).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(hasNotEnoughAvaiArticles)) {
      return Lists.newArrayList();
    }

    final List<ExternalVendorDto> allExternalVendor = artCriteria.getExternalVendors();
    final List<DeliveryProfileDto> allDeliveryProfile = artCriteria.getDeliveryProfiles();
    final String deliveryType = artCriteria.getDeliveryType();
    final ErpSendMethodEnum sendMethod = ErpSendMethodEnum.valueOf(deliveryType);
    final List<String> branchIds = AxBranchUtils.getBranchIdsByDeliveryType(sendMethod,
        artCriteria.getPickupBranchId(), artCriteria.findCustomerBranchIds());

    final List<ExternalVendorDto> conExternalVendors =
        selectExternalVendorsByType(allExternalVendor);

    final List<ExternalStockInfo> stockInfo = getStockInfos(conExternalVendors, axVendors,
        hasNotEnoughAvaiArticles, allDeliveryProfile, branchIds);

    if (CollectionUtils.isEmpty(stockInfo)) {
      return Lists.newArrayList();
    }
    
    List<ExternalStockInfo> sortedStocksInfo = ExternalStockInfoUtil.sortByPriority(stockInfo);

    List<ExternalStockInfo> distinctStockInfo =
        distinctExternalStockInfoIfNecessaryBaseOnCountry(sortedStocksInfo);

    List<ExternalStockInfo> validStockInfos = distinctStockInfo.stream()
        .filter(ExternalStockInfo::isValidConItem).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(validStockInfos)) {
      return Lists.newArrayList();
    }

    final AxConArticleSearchCriteria conArtSearchCriteria = new AxConArticleSearchCriteria();
    SagBeanUtils.copyProperties(artCriteria, conArtSearchCriteria);

    final List<TourTimeDto> tourTimeList = availCriteria.getTourTimeList();
    final List<WorkingHours> openingHours = availCriteria.getOpeningHours();

    validStockInfos.stream().forEach(item -> {
      final ArticleDocDto article = item.getArticle();
      conArtSearchCriteria.setStockInfo(item);

      final List<Availability> avails = axConAvailabilityFilter.filterAvailabilities(article,
          conArtSearchCriteria, tourTimeList, openingHours, countryName);
      final List<Availability> availsWithCon = article.getBackorderFalseAvails();
      if(CollectionUtils.isNotEmpty(avails)) {
        availsWithCon.addAll(avails);
      }
      article.setAvailabilities(availsWithCon);
    });

    return validStockInfos.stream().map(ExternalStockInfo::getArticle).collect(Collectors.toList());
  }

}
