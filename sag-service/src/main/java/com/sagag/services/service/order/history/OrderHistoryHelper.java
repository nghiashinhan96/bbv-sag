package com.sagag.services.service.order.history;

import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.order.OrderHistory;
import com.sagag.eshop.repo.entity.order.VCustomerOrderHistory;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.order.VCustomerOrderHistoryDto;
import com.sagag.services.hazelcast.domain.order.OrderDetailDto.OrderDetailDtoBuilder;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderHistoryHelper {

  @Autowired
  private OrganisationRepository organisationRepo;

  private Map<Long, String> getOrderIdToGoodsReceiverName(
      final List<VCustomerOrderHistory> orderHistory) {
    final List<Integer> goodsReceiverIds =
        orderHistory.stream().map(VCustomerOrderHistory::getGoodsReceiverId)
        .filter(Objects::nonNull).collect(Collectors.toList());

    if (CollectionUtils.isEmpty(goodsReceiverIds)) {
      return Collections.emptyMap();
    }
    Map<Integer, String> goodsReceiverIdTogoodsReceiverName =
        organisationRepo.findByIds(goodsReceiverIds).stream()
            .collect(Collectors.toMap(Organisation::getId, Organisation::getName));

    return orderHistory.stream().filter(o -> Objects.nonNull(o.getGoodsReceiverId()))
        .collect(Collectors.toMap(VCustomerOrderHistory::getId,
            h -> goodsReceiverIdTogoodsReceiverName.get(h.getGoodsReceiverId())));
  }

  public void updateGoodsReceiverInfo(final UserInfo user,
      final List<VCustomerOrderHistoryDto> orderDetailDtos,
      final List<VCustomerOrderHistory> orderHistory) {
    if (!user.hasWholesalerPermission()) {
      return;
    }
    Map<Long, String> orderIdToFinalCustomerName = getOrderIdToGoodsReceiverName(orderHistory);
    orderDetailDtos.stream()
        .forEach(o -> o.setGoodsReceiverName(orderIdToFinalCustomerName.get(o.getId())));
  }

  public void updateFinalCustomerInfo(final UserInfo user,
      final OrderDetailDtoBuilder orderDetailDtoBuilder, final OrderHistory orderHistory) {
    if (!user.hasWholesalerPermission() || Objects.isNull(orderHistory.getGoodsReceiverId())) {
      return;
    }
    Optional<Organisation> goodsReceiver =
        organisationRepo.findOneById(orderHistory.getGoodsReceiverId());
    goodsReceiver.ifPresent(o -> orderDetailDtoBuilder.goodsReceiverName(o.getName()));
  }
}
