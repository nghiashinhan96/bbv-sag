package com.sagag.services.ivds.promotion.reorder;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.ivds.promotion.ArticleComparator;
import com.sagag.services.ivds.promotion.IArticlesReorder;
import com.sagag.services.ivds.promotion.impl.BrandPromotionArticleComparator;
import com.sagag.services.ivds.promotion.impl.StockComparator;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The default tyres sorter.
 */
@Component
public class TyreArticleReoder implements IArticlesReorder, InitializingBean {

  private ArticleComparator stockComparator;

  @Override
  public void afterPropertiesSet() throws Exception {
    stockComparator = StockComparator.getInstance();
  }

  @Override
  public List<ArticleDocDto> reorderFirstTime(List<ArticleDocDto> articles, List<String> brands) {
    if (CollectionUtils.isEmpty(articles)) {
      return Collections.emptyList();
    }
    articles = new ArrayList<>(articles);
    Collections.sort(articles, BrandPromotionArticleComparator.getInstance(brands)
        .thenComparing(stockComparator));
    return articles;
  }

  @Override
  public List<ArticleDocDto> reorderInBatch(List<ArticleDocDto> articles, List<String> brands) {
    if (CollectionUtils.isEmpty(articles)) {
      return Collections.emptyList();
    }
    final ArticleComparator promotionBrandComparator =
        BrandPromotionArticleComparator.getInstance(brands);
    articles = new ArrayList<>(articles);
    Map<SortedMode, List<ArticleDocDto>> articlesMap =
      articles.stream().collect(Collectors.groupingBy(SortedMode::isValidFor));
    for (Entry<SortedMode, List<ArticleDocDto>> entry : articlesMap.entrySet()) {
      Collections.sort(ListUtils.emptyIfNull(entry.getValue()),
          arrivalTimeAndProductGroupComparator(promotionBrandComparator));
    }

    return SortedMode.sortedValues()
        .map(mode -> articlesMap.getOrDefault(mode, Collections.emptyList()))
        .flatMap(Collection::stream).collect(Collectors.toList());
  }

  private ArticleComparator arrivalTimeAndProductGroupComparator(
      final ArticleComparator altComparator) {
    return (art1, art2) -> {
      int resultProductGroup = altComparator.compare(art1, art2);
      Availability lastestAvail1 = art1.getLastestAvailability();
      Availability lastestAvail2 = art2.getLastestAvailability();
      if (lastestAvail1 == null || lastestAvail2 == null) {
        return resultProductGroup;
      }
      final DateTime arrivalTime1 = lastestAvail1.getCETArrivalTime();
      final DateTime arrivalTime2 = lastestAvail2.getCETArrivalTime();
      if (arrivalTime1 == null || arrivalTime2 == null) {
        return resultProductGroup;
      }
      int resultArrivalTime = arrivalTime1.compareTo(arrivalTime2);
      if (resultArrivalTime == 0) {
        return resultProductGroup;
      }
      return resultArrivalTime;
    };
  }

  enum SortedMode {
    SORTED, BACK_ODER_TRUE, UNSORTED;

    static Stream<SortedMode> sortedValues() {
      return Stream.of(SORTED, BACK_ODER_TRUE, UNSORTED);
    }

    static SortedMode isValidFor(ArticleDocDto article) {
      if (article == null || CollectionUtils.isEmpty(article.getAvailabilities())) {
        return UNSORTED;
      }
      Availability availability = article.getLastestAvailability();
      if (availability == null || availability.getBackOrder() == null || !article.hasGrossPrice()) {
        return UNSORTED;
      }
      if (BooleanUtils.isTrue(availability.getBackOrder())) {
        return BACK_ODER_TRUE;
      }
      return SORTED;
    }
  }

}
