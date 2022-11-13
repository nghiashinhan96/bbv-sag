package com.sagag.services.service.order.steps.afterorder;

import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.order.FinalCustomerOrder;
import com.sagag.eshop.repo.entity.order.FinalCustomerOrderItem;
import com.sagag.eshop.service.api.FinalCustomerOrderService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.FinalCustomerOrderStatus;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.order.model.AfterOrderCriteria;
import com.sagag.services.service.order.processor.AbstractOrderProcessor;
import com.sagag.services.service.order.steps.ShoppingCartConverters;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FinalUserAfterOrderHandler extends AbstractAfterOrderHandler {

  @Autowired
  protected CartBusinessService cartBusinessService;

  @Autowired
  private FinalCustomerOrderService finalCustomerOrderService;

  @Autowired
  private OrganisationService organisationService;

  @Override
  public void handle(final UserInfo user, final AbstractOrderProcessor processor,
      final AfterOrderCriteria afterOrderCriteria) {
    Long userId = user.getId();
    Organisation org =
        organisationService.getFirstByUserId(userId).orElseThrow(() -> new IllegalArgumentException(
            String.format("No organisation available for %d", userId)));
    FinalCustomerOrder finalCustomerOrder =
        buildFinalCustomerOrder(user.getId(), Long.valueOf(org.getId()), afterOrderCriteria,
            user.isFinalUserRole());

    finalCustomerOrderService.save(finalCustomerOrder);

    handleVinSearchCount(user, afterOrderCriteria.getShoppingCart());

    cartBusinessService.clear(user, afterOrderCriteria.getShopType());
  }

  private static FinalCustomerOrder buildFinalCustomerOrder(Long userId, Long orgId,
      AfterOrderCriteria afterOrderCriteria, boolean isFinalCustomer) {

    final ShoppingCart shoppingCart = afterOrderCriteria.getShoppingCart();
    final List<FinalCustomerOrderItem> items =
        ShoppingCartConverters.converter(shoppingCart, isFinalCustomer);
    return FinalCustomerOrder.builder()
        .totalGrossPrice(shoppingCart.getSubTotal())
        .userId(userId).orgId(orgId).date(Calendar.getInstance().getTime())
        .status(FinalCustomerOrderStatus.NEW.toString())
        .items(items)
        .branchRemark(afterOrderCriteria.getBranchRemark())
        .reference(afterOrderCriteria.getReference())
        .totalFinalCustomerNetPriceWithVat(shoppingCart.getSubTotalWithFinalCustomerNetAndVat())
        .totalGrossPriceWithVat(shoppingCart.getSubTotalWithVat())
        .totalFinalCustomerNetPrice(shoppingCart.getFinalCustomerNetTotalExclVat())
        .build();
  }

}
