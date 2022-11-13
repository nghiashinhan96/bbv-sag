package com.sagag.services.thule.extractor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.thule.domain.BuyersGuideData;
import com.sagag.services.thule.domain.BuyersGuideOrder;

@Component
public class BuyersGuideDataExtractor
    implements Function<Map<String, String>, Optional<BuyersGuideData>> {

  private static final String ORDER_LIST_SEPARATOR = "|";

  private static final String DEALER_KEY = "dealer";

  private static final String ORDER_LIST_KEY = "order_list";

  private static final String[] SEARCH_LIST = {"{", "}"};

  private static final String[] REPLACEMENT_LIST = { StringUtils.EMPTY, StringUtils.EMPTY };

  @Override
  public Optional<BuyersGuideData> apply(Map<String, String> formData) {
    if (MapUtils.isEmpty(formData)) {
      return Optional.empty();
    }

    final String dealerId = formData.getOrDefault(DEALER_KEY, StringUtils.EMPTY);
    final BuyersGuideData buyersGuideData = new BuyersGuideData(
        StringUtils.replaceEach(dealerId, SEARCH_LIST, REPLACEMENT_LIST));

    final String orderListStr = formData.get(ORDER_LIST_KEY);
    if (!StringUtils.isBlank(orderListStr)) {
      String[] orderListItems = StringUtils.split(orderListStr, ORDER_LIST_SEPARATOR);
      final List<BuyersGuideOrder> orders = Stream.of(orderListItems)
          .map(BuyersGuideOrder::new).collect(Collectors.toCollection(LinkedList::new));
      buyersGuideData.setOrders(orders);
    }
    return Optional.of(buyersGuideData);
  }

}
