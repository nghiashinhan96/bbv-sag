package com.sagag.services.service.cart.operation.add;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.domain.sag.invoice.InvoiceDto;
import com.sagag.services.domain.sag.invoice.InvoicePositionDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.api.InvoiceService;
import com.sagag.services.service.utils.order.OrdersUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AddCartItemsFromInvoiceShoppingCartOperation
    extends AbstractAddCartItemShoppingCartOperation<String> {

  @Autowired
  private InvoiceService invoiceService;

  @Autowired
  private AddArticlesByQuantityMapShoppingCartOperation addArticlesByQuantityMapShopCartOperation;

  @Autowired
  private AddCartItemsFromEshopOrderShoppingCartOperation addCartItemsFromEshopOrderShopCartOperation;

  @Override
  @LogExecutionTime
  public ShoppingCart execute(UserInfo user, String invoiceNr, ShopType shopType, Object... additionals) {
    final String orderNr = (String) additionals[0];
    final Long orderId = (Long) additionals[1];
    final String basketItemSourceId =  (String) additionals[2];
    final String basketItemSourceDesc =  (String) additionals[3];
    Optional<InvoiceDto> invoice = invoiceService.getInvoiceDetail(user, invoiceNr, orderNr, false, false);
    if (!invoice.isPresent()) {
      final String msg = String.format(
          "Invovice position is not found for invoiceNr %s and orderNe %s", invoiceNr, orderNr);
      throw new IllegalArgumentException(msg);
    }

    final Map<String, Integer> articleIdQuantityMap = invoice.get().getPositions().stream()
        .collect(Collectors.toMap(InvoicePositionDto::getArticleId,
            inv -> OrdersUtils.getArticleAbsQuantity(inv.getQuantity()),
            (articleId1, articleId2) -> articleId1));

    if (!Objects.isNull(orderId)) {
      return addCartItemsFromEshopOrderShopCartOperation.execute(user, articleIdQuantityMap,
          shopType, orderId, basketItemSourceId, basketItemSourceDesc);
    }
    return addArticlesByQuantityMapShopCartOperation.execute(user, articleIdQuantityMap,
        shopType, basketItemSourceId, basketItemSourceDesc);
  }

}
