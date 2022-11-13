package com.sagag.services.service.order.steps.orderrequest;

import com.sagag.services.common.profiles.LocalStockPreferringProfile;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@LocalStockPreferringProfile(false)
public class OrderingOptimizationExectutor implements OrderingOptimizer {

  private static final int SHOULD_OPTIMIZE_NUMBER = 1;

  @Override
  public List<ShoppingCartItem> optimize(List<ShoppingCartItem> originalCartItems) {

    List<ShoppingCartItem> cartItems =
        originalCartItems.stream().map(SerializationUtils::clone).collect(Collectors.toList());

    List<ArticleDocDto> articles =
        cartItems.stream().map(ShoppingCartItem::getArticle).collect(Collectors.toList());

    List<Availability> avais = articles.stream()
        .flatMap(article -> CollectionUtils.emptyIfNull(article.getAvailabilities()).stream())
        .collect(Collectors.toList());

    long numberOfVenAvais =
        articles.stream().filter(article -> Objects.nonNull(article.getVenExternalAvailability()))
            .collect(Collectors.counting());

    if (numberOfVenAvais < SHOULD_OPTIMIZE_NUMBER) {
      return cartItems;
    }

    findLastVenExternalDeliveryTime(avais).ifPresent(lastDeliveryTime -> {
      switchToVenExternalIfPossible(articles, lastDeliveryTime);
      optimizeDeliveryTimes(articles, lastDeliveryTime);
    });

    return cartItems;
  }

  private void switchToVenExternalIfPossible(List<ArticleDocDto> articles,
      DateTime lastExternalDeliveryTime) {
    List<ArticleDocDto> potentialArticles =
        articles.stream().filter(isPotentialArticleForSwitching(lastExternalDeliveryTime))
            .collect(Collectors.toList());
    CollectionUtils.emptyIfNull(potentialArticles).forEach(this::switchToVenExternal);
  }

  private void switchToVenExternal(ArticleDocDto article) {
    Availability externalAvai = article.getVenExternalAvailability();
    externalAvai.setQuantity(article.getAmountNumber());
    article.setAvailabilities(Arrays.asList(externalAvai));
  }

  private Predicate<ArticleDocDto> isPotentialArticleForSwitching(
      DateTime lastExternalDeliveryTime) {
    return article -> {
      List<Availability> avais = article.getAvailabilities();
      boolean containsAxAvai =
          CollectionUtils.emptyIfNull(avais).stream().anyMatch(Availability::isNonVenExternal);
      Availability venExternalAvai = article.getVenExternalAvailability();
      return containsAxAvai && Objects.nonNull(venExternalAvai)
          && !lastExternalDeliveryTime.isBefore(venExternalAvai.getUTCArrivalTimeAtBranch())
          && venExternalAvai.getQuantity() >= article.getAmountNumber();
    };
  }

  private Optional<DateTime> findLastVenExternalDeliveryTime(List<Availability> avais) {
    return avais.stream().filter(Availability::isVenExternalSource)
        .collect(Collectors.maxBy(longerDeliveryTime()))
        .map(Availability::getUTCArrivalTimeAtBranch);
  }

  private Comparator<Availability> longerDeliveryTime() {
    return (a, b) -> a.getUTCArrivalTimeAtBranch().compareTo(b.getUTCArrivalTimeAtBranch());
  }

  private void optimizeDeliveryTimes(List<ArticleDocDto> articles,
      DateTime lastExternalDeliveryTime) {
    List<Availability> externalAvais =
        articles.stream().filter(article -> CollectionUtils.isNotEmpty(article.getAvailabilities()))
            .flatMap(article -> article.getAvailabilities().stream())
            .filter(Availability::isVenExternalSource).collect(Collectors.toList());
    externalAvais.stream().forEach(avai -> avai.setArrivalTime(
        DateUtils.getUTCDateString(lastExternalDeliveryTime.toDate(), DateUtils.UTC_DATE_PATTERN)));
  }
}
