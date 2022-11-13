package com.sagag.services.service.order.steps.afterorder;

import com.sagag.eshop.repo.entity.CouponUseLog;
import com.sagag.eshop.repo.entity.order.OrderHistory;
import com.sagag.eshop.service.api.CouponService;
import com.sagag.eshop.service.api.LicenseService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.LicenseType;
import com.sagag.services.common.enums.order.EshopOrderHistoryState;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.LicenseDto;
import com.sagag.services.domain.eshop.dto.LicenseSettingsDto;
import com.sagag.services.hazelcast.api.CouponCacheService;
import com.sagag.services.hazelcast.domain.cart.CartItemType;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.order.model.AfterOrderCriteria;
import com.sagag.services.service.order.processor.AbstractOrderProcessor;
import com.sagag.services.service.request.RemoveItemRequestBody;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class EshopUserAfterOrderHandler extends AbstractAfterOrderHandler {

  private static final String LOG_INFO = "Handling common processes after ordering successful";

  /* endDate: 2099.12.31 23:59:59 */
  @Value("${license.endDate:}")
  private String endDate;

  @Value("${license.vin.minimumOrder:}")
  private String minimumOrder;

  @Autowired
  private LicenseService licenseService;

  @Autowired
  protected CartBusinessService cartBusinessService;

  @Autowired
  private CouponService couponService;

  @Autowired
  private CouponCacheService couponCacheService;

  @Override
  public void handle(final UserInfo user, final AbstractOrderProcessor processor,
      final AfterOrderCriteria afterOrderCriteria) {
    log.info(LOG_INFO);
    ShoppingCart shoppingCart = afterOrderCriteria.getShoppingCart();
    OrderHistory orderHistory = afterOrderCriteria.getOrderHistory();

    handleVinPackOrder(user, findVinArticle(shoppingCart));

    handleVinSearchCount(user, shoppingCart);

    String[] cartKeys =
        shoppingCart.getItems().stream().map(ShoppingCartItem::getCartKey).toArray(String[]::new);
    RemoveItemRequestBody body = RemoveItemRequestBody.builder().cartKeys(cartKeys).build();
    cartBusinessService.remove(user, body, afterOrderCriteria.getShopType());

    handleCoupon(user.key(), orderHistory, afterOrderCriteria.getCouponUseLog());

    orderHistory.setFinalCustomerOrderId(afterOrderCriteria.getFinalCustomerOrderId());
    orderHistory.setGoodsReceiverId(afterOrderCriteria.getGoodsReceiverId());
    processor.updateOrderHistory(orderHistory, afterOrderCriteria.getAxResult(),
        EshopOrderHistoryState.ORDERED, user.getCustNrStr());
  }

  private ArticleDocDto findVinArticle(final ShoppingCart shoppingCart) {
    final Optional<ShoppingCartItem> cartItem = shoppingCart.getItems().stream()
        .filter(item -> item.getItemType() == CartItemType.VIN).findFirst();
    return cartItem.map(ShoppingCartItem::getArticle).orElse(null);
  }

  /**
   * Handles vin package when order successfully.
   * <p>
   * Update license table and clear cache if shopping basket contains vin license package item.
   *
   * @param user the logged in user
   */
  private void handleVinPackOrder(final UserInfo user, final ArticleDocDto vinArticleDoc) {
    // update vin license db
    if (Objects.isNull(vinArticleDoc)) {
      return;
    }

    final long artId = Long.parseLong(vinArticleDoc.getArtid());
    final int numberOfPackage = vinArticleDoc.getAmountNumber();
    Optional<LicenseSettingsDto> licenseSettings =
        licenseService.getLicenseSettingsByArticleId(artId);
    if (!licenseSettings.isPresent()) {
      return;
    }
    final LicenseSettingsDto license = licenseSettings.get();
    final Date now = DateUtils.getUTCDate(Calendar.getInstance().getTime());
    LicenseDto licenseDto = new LicenseDto();
    licenseDto.setTypeOfLicense(LicenseType.VIN.name());
    licenseDto.setPackId(license.getPackId());
    licenseDto.setPackName(license.getPackName());
    licenseDto.setCustomerNr(Long.valueOf(user.getCustNrStr()));
    licenseDto.setUserId(user.getId());
    licenseDto.setBeginDate(now);
    Date licenseEndDate =
        DateUtils.getUTCDate(DateUtils.toDate(endDate, DateUtils.SWISS_DATE_PATTERN));
    licenseDto.setEndDate(licenseEndDate);
    licenseDto.setQuantity(license.getQuantity() * numberOfPackage);
    licenseDto.setQuantityUsed(0);
    // Consider setLastUsed
    licenseDto.setLastUpdate(now);
    licenseDto.setLastUpdateBy(user.getId());
    licenseService.createVinLicense(licenseDto);
  }

  private void handleCoupon(final String key, OrderHistory orderHistory,
      CouponUseLog couponUseLog) {
    if (!Objects.isNull(couponUseLog) && couponUseLog.isValidCoupon()) {
      couponUseLog.setOrderConfirmationId(null);
      couponUseLog.setOrderID(orderHistory.getId());
      couponUseLog.setUsageCountRemainder(couponUseLog.getUsageCountRemainder() - 1);
      couponService.update(couponUseLog);
    }
    // clear coupon cached after order
    couponCacheService.clearCache(key);
  }
}
