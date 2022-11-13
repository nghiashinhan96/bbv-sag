package com.sagag.services.ivds.tyres;

import com.sagag.services.ax.enums.AustriaArticleAvailabilityState;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.ArticleStock;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;
import com.sagag.services.ivds.promotion.impl.ProductGroupTyreArticleComparator;
import com.sagag.services.ivds.promotion.reorder.TyreArticleReoder;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * UT for {@link TyreArticleReoder}.
 */
@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class DefaultTyresSorterTest {

  private static final String ARRIVAL_TIME = "2018-06-04T09:40:00Z";

  @InjectMocks
  private TyreArticleReoder sorter;

  @Mock
  private ProductGroupTyreArticleComparator productGroupTyreArticleComparator;

  @Test
  @Ignore
  public void givenArticlesShouldSortFirstTime() {
    final List<String> expected = Arrays.asList("1003", "1001", "1002", "1004");
    final List<ArticleDocDto> articles = articlesWithoutAvailabilities();
    final List<ArticleDocDto> result = sorter.reorderFirstTime(articles, Collections.emptyList());

    final List<String> actual = getIdPimFromArticles(result);
    log.debug(SagJSONUtil.convertObjectToPrettyJson(actual));
    Assert.assertThat(actual, Matchers.equalTo(expected));
  }

  private static List<ArticleDocDto> articlesWithoutAvailabilities() {
    return Stream.of(article("1001", 11.0, null), article("1002", null, 20.1),
        article("1003", 101.0, 100.0), article("1004", null, null)).collect(Collectors.toList());
  }

  @Test
  @Ignore
  public void givenArticlesShouldSortInBatch() {
    final List<String> expected = Arrays.asList("1003", "1001", "1005", "1002", "1004");
    final List<ArticleDocDto> articles =
        sorter.reorderFirstTime(articlesWithAvailabilities(), Collections.emptyList());
    final List<ArticleDocDto> result = sorter.reorderInBatch(articles, Collections.emptyList());

    final List<String> actual = getIdPimFromArticles(result);
    log.debug(SagJSONUtil.convertObjectToPrettyJson(actual));
    Assert.assertThat(actual, Matchers.equalTo(expected));
  }

  private static List<ArticleDocDto> articlesWithAvailabilities() {
    return Stream.of(
        articleWithAvailability("1001", 14.0, 10.1, DateTime.parse(ARRIVAL_TIME), "R"),
        articleWithAvailability("1002", null, 20.1, DateTime.parse(ARRIVAL_TIME), "R"),
        articleWithAvailability("1003", 101.0, 100.0, DateTime.parse(ARRIVAL_TIME), "I"),
        articleWithAvailability("1004", null, null, null, "P"),
        articleWithAvailability("1005", 11.0, 10.01, DateTime.parse(ARRIVAL_TIME), "R"))
        .collect(Collectors.toList());
  }

  @Test
  @Ignore
  public void givenArticlesShouldSortTyres3323() {
    final List<String> expected = Arrays.asList("1002", "1001", "1003", "1005", "1004");
    final List<ArticleDocDto> articles =
        sorter.reorderFirstTime(articlesWithBackOrderAvailabilities(), Collections.emptyList());
    final List<ArticleDocDto> result = sorter.reorderInBatch(articles, Collections.emptyList());

    final List<String> actual = getIdPimFromArticles(result);
    log.debug(SagJSONUtil.convertObjectToPrettyJson(actual));
    Assert.assertThat(actual, Matchers.equalTo(expected));
  }

  private static List<ArticleDocDto> articlesWithBackOrderAvailabilities() {
    final int delivery24Hours = AustriaArticleAvailabilityState.IN_144_HOURS.getCode();
    final int green = AustriaArticleAvailabilityState.GREEN.getCode();
    final boolean backOrderFalse = false;
    final boolean backOrderTrue = true;

    return Stream.of(
        articleWithAvailability("1001", 14.0, 101.0, DateTime.parse("2018-06-04T12:40:00Z"), "R",
            delivery24Hours, backOrderFalse),
        articleWithAvailability("1002", null, 20.1, DateTime.parse("2018-06-04T10:40:00Z"), "R",
            green, backOrderFalse),
        articleWithAvailability("1003", 101.0, 100.0, DateTime.parse("2018-06-04T08:40:00Z"), "I",
            delivery24Hours, backOrderTrue),
        articleWithAvailability("1004", null, null, null, "P", green, backOrderFalse),
        articleWithAvailability("1005", 11.0, null, DateTime.parse(ARRIVAL_TIME), "R",
            delivery24Hours, backOrderTrue))
        .collect(Collectors.toList());
  }

  @Test
  @Ignore
  public void givenArticlesShouldSortTyres3323DirtyData() {
    final List<String> expected = Arrays.asList("1006", "1002", "1001", "1005", "1003", "1004");
    final List<ArticleDocDto> articles =
        sorter.reorderFirstTime(articlesWithBackOrderAvailabilitiesDirty(), Collections.emptyList());
    final List<ArticleDocDto> result = sorter.reorderInBatch(articles, Collections.emptyList());

    final List<String> actual = getIdPimFromArticles(result);
    log.debug(SagJSONUtil.convertObjectToPrettyJson(actual));
    Assert.assertThat(actual, Matchers.equalTo(expected));
  }

  private static List<ArticleDocDto> articlesWithBackOrderAvailabilitiesDirty() {
    final int delivery24Hours = AustriaArticleAvailabilityState.IN_144_HOURS.getCode();
    final int green = AustriaArticleAvailabilityState.GREEN.getCode();
    final int yellow = AustriaArticleAvailabilityState.YELLOW.getCode();
    final boolean backOrderFalse = false;
    final boolean backOrderTrue = true;

    return Stream.of(
        articleWithAvailability("1001", 14.0, 10.1, DateTime.parse("2018-06-04T12:40:00Z"), "R",
            delivery24Hours, backOrderFalse),
        articleWithAvailability("1002", null, 20.1, DateTime.parse("2018-06-04T10:40:00Z"), "R",
            green, backOrderFalse),
        articleWithAvailability("1003", 101.0, null, null, "I", yellow, null),
        articleWithAvailability("1004", null, null, null, "P", green, backOrderFalse),
        articleWithAvailability("1005", 11.0, 10.1, DateTime.parse("2018-06-04T09:40:00Z"), "R",
            delivery24Hours, backOrderTrue),
        articleWithAvailability("1006", 101.0, 101.0, DateTime.parse("2018-06-04T08:40:00Z"), "I",
            yellow, backOrderFalse))
        .collect(Collectors.toList());
  }

  private static List<String> getIdPimFromArticles(final List<ArticleDocDto> articles) {
    return articles.stream().map(ArticleDocDto::getIdSagsys).collect(Collectors.toList());
  }

  private static ArticleDocDto articleWithAvailability(String id, Double stockVal, Double priceVal,
      DateTime arrivalTime, String sagProductGrp, int availCode, Boolean backOrder) {
    final ArticleDocDto article = article(id, stockVal, priceVal);
    if (arrivalTime != null) {
      Availability avail = new Availability();
      avail.setArrivalTime(arrivalTime.toString());
      avail.setAvailState(availCode);
      avail.setBackOrder(backOrder);
      article.setAvailabilities(Arrays.asList(avail));
    }
    article.setSagProductGroup(sagProductGrp);
    return article;
  }

  private static ArticleDocDto articleWithAvailability(String id, Double stockVal, Double priceVal,
      DateTime arrivalTime, String sagProductGrp) {
    final ArticleDocDto article = article(id, stockVal, priceVal);
    if (arrivalTime != null) {
      Availability avail = new Availability();
      avail.setArrivalTime(arrivalTime.toString());
      avail.setBackOrder(true);
      article.setAvailabilities(Arrays.asList(avail));
    }
    article.setSagProductGroup(sagProductGrp);
    return article;
  }

  private static ArticleDocDto article(String id, Double stockVal, Double priceVal) {
    ArticleDocDto article = new ArticleDocDto();
    article.setIdSagsys(id);
    if (stockVal != null) {
      ArticleStock stock = new ArticleStock();
      stock.setStock(stockVal);
      article.setStock(stock);
    }
    if (priceVal != null) {
      PriceWithArticle price = new PriceWithArticle();
      PriceWithArticlePrice articlePrice = new PriceWithArticlePrice();
      articlePrice.setNetPrice(priceVal);
      articlePrice.setGrossPrice(priceVal);
      price.setPrice(articlePrice);
      article.setPrice(price);
    }
    return article;
  }

}
