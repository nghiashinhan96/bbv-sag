package com.sagag.services.dvse.utils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.NextWorkingDates;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.TourTimeTable;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DvseArticleUtils {

  private static final int QWP_SUPPLIER_ID = 1962;

  private static final String QWP_SUPPLIER_NAME = "QWP";

  public static DateTime getLastTourOfLastAvail(Availability lastAvailability,
      NextWorkingDates nextWorkingDate) {

    TourTimeTable tourTimeTable = lastAvailability.getLastTour();
    DateTime lastTour;
    if (Objects.nonNull(tourTimeTable)) {
      lastTour = tourTimeTable.getCETStartTime();
    } else {
      lastTour = new DateTime(
          Boolean.TRUE.equals(lastAvailability.getBackOrder()) ? nextWorkingDate.getBackorderDate()
              : nextWorkingDate.getNoBackorderDate());
    }
    return lastTour;
  }

  public static boolean equalsQwpSupplier(ArticleDocDto article) {
    return StringUtils.equalsIgnoreCase(QWP_SUPPLIER_NAME, article.getSupplier())
        || article.getSupplierId() == QWP_SUPPLIER_ID;
  }

  public static Map<String, Optional<Integer>> buildArticleAndRequestedQuantityMap(
      List<ArticleDocDto> requestedArticles) {
    Map<String, Optional<Integer>> articleAndRequestQuantity =
        CollectionUtils.emptyIfNull(requestedArticles).stream().collect(Collectors
            .toMap(ArticleDocDto::getArtid, art -> Optional.ofNullable(art.getAmountNumber())));
    return articleAndRequestQuantity;
  }
}
