package com.sagag.services.service.order.steps;

import com.sagag.eshop.repo.entity.SupportedAffiliate;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GlassBodyOrWorkMessageOrderRequestStepHandler {

  private static final String LOG_INFO =
      "Update glass body or work into ordering message with request date = {}";

  private static final String MESSAGE_CHECK_VIN = "BITTE MIT VIN PRUEFEN";

  private static final String MESSAGE_NO_VIN = "OHNE VIN";

  public void handle(final SupportedAffiliate affiliate, final ExternalOrderRequest axOrderRequest,
      final List<ShoppingCartItem> cartItems, final String requestDateTime) {
    log.info(LOG_INFO, requestDateTime);
    if (StringUtils.isBlank(requestDateTime)) {
      log.warn("No update glass body or work message with empty request date time");
      return;
    }

    final boolean isGlassArticleFound = cartItems.stream()
        .anyMatch(isGlassOrBodyArticleSearchItemPredicate());
    if (!isGlassArticleFound) {
      log.warn("Not found any glass or body article");
      return;
    }

    final String affiliateStartWorkingTime =
        StringUtils.defaultString(affiliate.getStartWorkingTime());
    final String affiliateEndWorkingTime =
        StringUtils.defaultString(affiliate.getEndWorkingTime());

    Optional<String> vinMessageOpt = Optional.empty();
    Optional<String> vinRefTextOpt = Optional.empty();
    if (isInWorkingTime(requestDateTime, affiliateStartWorkingTime, affiliateEndWorkingTime)) {
      vinMessageOpt = Optional.of(StringUtils.defaultIfBlank(
          axOrderRequest.getMessage(), StringUtils.EMPTY));
    } else {
      vinRefTextOpt = Optional.of(StringUtils.defaultIfBlank(
          axOrderRequest.getCustomerRefText(), StringUtils.EMPTY));
    }

    final Set<String> vinCodes = cartItems.stream()
        .filter(isGlassOrBodyArticleSearchItemPredicate())
        .map(cartItem -> cartItem.getVehicle().getVin())
        .filter(StringUtils::isNotBlank)
        .collect(Collectors.toSet());
    final StringBuilder vinInfoBuilder = createVinInfoTextBuilder(vinCodes);
    vinMessageOpt.map(vinMessage -> new StringBuilder(vinMessage).append(StringUtils.SPACE)
        .append(vinInfoBuilder).toString())
    .map(String::trim).ifPresent(axOrderRequest::setMessage);
    vinRefTextOpt.map(vinRefText -> new StringBuilder(vinRefText).append(StringUtils.SPACE)
        .append(vinInfoBuilder).toString())
    .map(String::trim).ifPresent(axOrderRequest::setCustomerRefText);

  }

  private static Predicate<ShoppingCartItem> isGlassOrBodyArticleSearchItemPredicate() {
    return isGlassOrBodyArticleItemPredicate().and(isVehicleSearchPredicate());
  }

  private static Predicate<ShoppingCartItem> isGlassOrBodyArticleItemPredicate() {
    return cartItem -> cartItem.getArticle().isGlassOrBody();
  }

  private static Predicate<ShoppingCartItem> isVehicleSearchPredicate() {
    return cartItem -> !StringUtils.isBlank(cartItem.getVehicleId());
  }

  private static StringBuilder createVinInfoTextBuilder(final Set<String> vinCodes) {
    StringBuilder vinInfoBuilder = new StringBuilder();
    if (CollectionUtils.isEmpty(vinCodes)) {
      vinInfoBuilder.append(MESSAGE_NO_VIN);
      return vinInfoBuilder;
    }
    vinInfoBuilder.append(StringUtils.join(vinCodes, StringUtils.SPACE))
    .append(StringUtils.SPACE).append(MESSAGE_CHECK_VIN);
    return vinInfoBuilder;
  }

  private static boolean isInWorkingTime(final String clientDateTime,
      final String affStartWorkingTime, final String affEndWorkingTime) {

    if (StringUtils.isAnyBlank(clientDateTime, affStartWorkingTime, affEndWorkingTime)) {
      return false;
    }

    final LocalDateTime requestTime = LocalDateTime.parse(clientDateTime,
        DateTimeFormatter.ofPattern(DateUtils.SWISS_DATE_PATTERN_1));
    final LocalDateTime startWorkingTime = parseLocalDateTime(LocalDateTime.now(), affStartWorkingTime);
    final LocalDateTime endWorkingTime = parseLocalDateTime(LocalDateTime.now(), affEndWorkingTime);

    return !requestTime.isBefore(startWorkingTime) && !requestTime.isAfter(endWorkingTime);
  }

  private static LocalDateTime parseLocalDateTime(LocalDateTime currentDateTime, String workingTimeStr) {
    final String[] strs = ArrayUtils.nullToEmpty(
        StringUtils.split(workingTimeStr, SagConstants.COLON));
    return currentDateTime.withHour(Integer.valueOf(strs[0])).withMinute(Integer.valueOf(strs[1]));
  }

}
