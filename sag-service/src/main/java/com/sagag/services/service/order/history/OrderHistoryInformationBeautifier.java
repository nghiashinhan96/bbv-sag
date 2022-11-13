package com.sagag.services.service.order.history;

import com.google.common.collect.Lists;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.hazelcast.domain.order.OrderInfoDto;
import com.sagag.services.hazelcast.domain.order.OrderItemDetailDto;
import com.sagag.services.ivds.api.IvdsArticleService;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OrderHistoryInformationBeautifier {

  @Autowired
  private IvdsArticleTaskExecutors ivdsArticleTaskExecutors;

  @Autowired
  private IvdsArticleService ivdsArticleService;

  /**
   * Beautifies the order history information.
   *
   * @param user
   * @param orderInfo
   * @param updateErp
   */
  public void beautify(UserInfo user, OrderInfoDto orderInfo, boolean updateErp) {
    if (user.isSalesNotOnBehalf()) {
      return;
    }
    if (updateErp) {
      beautifyErpArticleInfoForOrderHistory(user, orderInfo);
    } else {
      beautifyArticleInfoElasticsearchForOrderHistory(user, orderInfo);
    }
  }

  /**
   * Beautifies ERP article information for order history.
   *
   * @param user
   * @param orderInfo
   */
  private void beautifyErpArticleInfoForOrderHistory(UserInfo user, OrderInfoDto orderInfo) {
    List<ArticleDocDto> articles = orderInfo.getItems().stream().map(OrderItemDetailDto::getArticle)
        .collect(Collectors.toList());
    searchAndBeautifyArticleInfo(user, articles);

    ivdsArticleTaskExecutors.executeTaskWithErpArticleAndIgnoredElasticsearch(user, articles,
        Optional.empty());
  }

  private void searchAndBeautifyArticleInfo(UserInfo user, List<ArticleDocDto> articles) {
    List<String> pimIds =
        articles.stream().map(ArticleDocDto::getId).distinct().collect(Collectors.toList());
    List<ArticleDocDto> esArticles =
        ivdsArticleService.searchArticleByArticleIds(user, pimIds).getContent();

    Map<String, ArticleDocDto> esArticlesMap =
        esArticles.stream().collect(Collectors.toMap(ArticleDocDto::getId, Function.identity()));
    articles.stream()
    .filter(article -> esArticlesMap.containsKey(article.getId()))
    .forEach(article -> {
      ArticleDocDto esArt = esArticlesMap.get(article.getId());
      article.setParts(esArt.getParts());
      article.setInfos(esArt.getInfos());
      article.setCriteria(esArt.getCriteria());
      article.setQtyMultiple(esArt.getQtyMultiple());

      article.setQtyLowestAt(esArt.getQtyLowestAt());
      article.setQtyStandardAt(esArt.getQtyStandardAt());
      article.setQtyMultipleAt(esArt.getQtyMultipleAt());

      article.setQtyLowestBe(esArt.getQtyLowestBe());
      article.setQtyStandardBe(esArt.getQtyStandardBe());
      article.setQtyMultipleBe(esArt.getQtyMultipleBe());

      article.setQtyLowestCh(esArt.getQtyLowestCh());
      article.setQtyStandardCh(esArt.getQtyStandardCh());
      article.setQtyMultipleCh(esArt.getQtyMultipleCh());
    });
  }

  /**
   * Beautifies Elasticsearch article information for order history.
   *
   * @param user
   * @param orderInfo
   */
  private void beautifyArticleInfoElasticsearchForOrderHistory(UserInfo user,
      OrderInfoDto orderInfo) {
    final Map<String, List<OrderItemDetailDto>> orderItemMapByVehId = new HashMap<>();

    orderInfo.getItems().forEach(bindingOrderItemMapByVehIdConsumer(orderItemMapByVehId));

    orderItemMapByVehId.forEach((vehId, orderItems) -> {
      final List<ArticleDocDto> articles = orderItems.stream().map(OrderItemDetailDto::getArticle)
          .collect(Collectors.toList());
      searchAndBeautifyArticleInfo(user, articles);
      ivdsArticleTaskExecutors.executeTaskElasticsearch(articles,
          defaultVehicleOptional(vehId, orderItems), user.getSupportedAffiliate());
    });
  }

  private static Optional<VehicleDto> defaultVehicleOptional(String vehId,
      List<OrderItemDetailDto> orderItems) {
    if (SagConstants.KEY_NO_VEHICLE.equals(vehId)) {
      return Optional.empty();
    }
    return orderItems.stream().findFirst().map(OrderItemDetailDto::getVehicle);
  }

  private static Consumer<OrderItemDetailDto> bindingOrderItemMapByVehIdConsumer(
      Map<String, List<OrderItemDetailDto>> orderItemMapByVehId) {
    return item -> {
      final VehicleDto vehicle = item.getVehicle();
      final String vehIdKey;
      if (vehicle == null
          || StringUtils.equalsIgnoreCase(SagConstants.KEY_NO_VEHICLE, vehicle.getVehId())) {
        vehIdKey = SagConstants.KEY_NO_VEHICLE;
      } else {
        vehIdKey = vehicle.getVehId();
      }

      final BiFunction<String, List<OrderItemDetailDto>, List<OrderItemDetailDto>> remappingFunc =
          (vehId, items) -> {
            if (items == null) {
              items = Lists.newArrayList();
            }
            items.add(item);
            return items;
          };
      orderItemMapByVehId.compute(vehIdKey, remappingFunc);
    };
  }
}
