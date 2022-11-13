package com.sagag.services.service.mail.orderconfirmation;

import com.google.common.collect.Lists;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.api.FinalCustomerService;
import com.sagag.eshop.service.api.UserSettingsService;
import com.sagag.eshop.service.builder.AddressBuilder;
import com.sagag.eshop.service.dto.finalcustomer.FinalCustomerSettingDto;
import com.sagag.eshop.service.formatter.NumberFormatterContext;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.currency.SagCurrencyParser;
import com.sagag.services.common.enums.DeliveryMethodType;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.mail.OrderArticleItem;
import com.sagag.services.domain.eshop.mail.OrderConfirmationMail;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.mail.MailSender;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
public class OrderConfirmationMailSender extends MailSender<OrderConfirmationCriteria> {

  @Autowired
  private UserSettingsService userSettingsService;

  @Autowired
  private FinalCustomerService finalCustomerService;

  @Autowired
  private NumberFormatterContext numberFormatterContext;

  @Autowired
  private OrderConfirmationMailSenderCustomByAffDataBuilderFactory orderConfirmMailBuilderFactory;

  @Autowired
  private SagCurrencyParser currencyParser;

  @Autowired
  private LocaleContextHelper localeContextHelper;

  @Override
  public void send(OrderConfirmationCriteria criteria) {
    final UserSettings userSettings = userSettingsService.getSettingsByUserId(criteria.getUserId());
    // If setting is false return now
    if (!userSettings.isEmailNotificationOrder()) {
      return;
    }
    final Locale locale = localeContextHelper.toLocale(criteria.getLangiso());

    String finalCustomerNr = StringUtils.EMPTY;
    String finalShopName = StringUtils.EMPTY;
    String street = StringUtils.EMPTY;
    String addr1 = StringUtils.EMPTY;
    String addr2 = StringUtils.EMPTY;
    String poBox = StringUtils.EMPTY;
    String postCode = StringUtils.EMPTY;
    String place = StringUtils.EMPTY;
    String finalCustomerDeliveryAddress = StringUtils.EMPTY;
    if (criteria.isFinalUser()) {
      final FinalCustomerSettingDto finalCustomerSettingDto = finalCustomerService
          .getFinalCustomerSettings(Long.valueOf(criteria.getFinalCustomer().getId()));
      street = finalCustomerSettingDto.getStreet();
      addr1 = finalCustomerSettingDto.getAddress1();
      addr2 = finalCustomerSettingDto.getAddress2();
      poBox = finalCustomerSettingDto.getPoBox();
      postCode = finalCustomerSettingDto.getPostCode();
      place = finalCustomerSettingDto.getPlace();
      finalCustomerNr = finalCustomerSettingDto.getCustomerNumber();
      finalShopName = messageSource.getMessage("mail.order_confirm.automotive_shop", null, locale);
      // #3204 handle Final customer with availability

      ArrayList<String> finalCustomerAddresses = Lists.newArrayList(
          criteria.getFinalCustomer().getName(), addr1, addr2, postCode, place);

      finalCustomerAddresses.removeIf(StringUtils::isBlank);
      finalCustomerDeliveryAddress = String.join(", ", finalCustomerAddresses);

      if (criteria.isShowAvailability()) {
        criteria.setDeliveryMethodType(DeliveryMethodType.NORMAL);
      }
    }

    final String customerNr = criteria.getCustomerNr();
    final String affiliateEmail = criteria.getAffiliateEmail();

    SupportedAffiliate affiliate = SupportedAffiliate.fromDesc(criteria.getAffiliateInUrl());

    final TimeZone userTimeZone = criteria.getTimezone();
    final ExternalOrderRequest orderRequest = criteria.getOrderRequest();
    final ShoppingCart shoppingCart = criteria.getShoppingCart();

    final NumberFormat numberFormat = numberFormatterContext
        .getFormatterByAffiliateShortName(criteria.getCollectionShortName());
    final Map<String, List<OrderArticleItem>> orderArticleItems =
        getOrderArticleItems(shoppingCart.getItems(), userTimeZone, locale, affiliate, numberFormat,
            criteria);

    final String orderNr = criteria.getOrderNr();

    final OrderConfirmationMail orderConfirmMail = OrderConfirmationMail.builder()
        .lastName(joinWithSpace(criteria.getLastName(), criteria.getFirstName()))
        .isNetPriceView(userSettings.isNetPriceConfirm() && !criteria.isFinalUser())
        .isShowAvailability(criteria.isShowAvailability())
        .shippingText(
            messageSource.getMessage(criteria.getSendMethodType().getMsgCode(), null, locale))
        .deliveryText(
            messageSource.getMessage(criteria.getDeliveryMethodType().getMsgCode(), null, locale))
        .paymentMethodText(
            messageSource.getMessage(criteria.getPaymentMethodType().getMsgCode(), null, locale))
        .affiliateText(StringUtils.capitalize(affiliate.getGeneralAffiliateName()))
        .orderFrom(DateUtils.toStringDate(DateUtils.getUTCDate(Calendar.getInstance().getTime()),
            DateUtils.SWISS_DATE_PATTERN_3))
        .customerRefText(orderRequest.getCustomerRefText()).branchRemark(orderRequest.getMessage())
        .orderNr(orderNr).deliveryAddr(buildAddress(criteria.getDeliveryAddress()))
        .invoiceAddr(buildAddress(criteria.getBillingAddress())).affiliateEmail(affiliateEmail)
        .customerNr(customerNr)
        .isFinalUser(criteria.isFinalUser())
        .finalCustomerNr(finalCustomerNr)
        .finalCustomerName(criteria.isFinalUser() ? criteria.getFinalCustomer().getName()
            : StringUtils.EMPTY)
        .street(street)
        .address1(addr1)
        .address2(addr2)
        .poBox(poBox)
        .postCode(postCode)
        .place(place)
        .isChAffiliate(affiliate.isChAffiliate() || criteria.isFinalUser())
        .isShowFinalCustomerNetPrice(criteria.isShowFinalCustomerNetPrice())
        .finalCustomerDeliveryAddress(finalCustomerDeliveryAddress)
        .isReferenceTextShow(showReferenceTextByAff(affiliate))
        .isCzAffiliate(affiliate.isCzAffiliate())
        .build();

    // Set data context
    final Context context = new Context(locale);
    context.setVariable("orderConfirmMail", orderConfirmMail);
    context.setVariable("orderArticleItems", orderArticleItems);

    // Create subject and body text
    final String subject =
        Stream.of(messageSource.getMessage("mail.order_confirm.subject", null, locale),
            finalShopName)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.joining(StringUtils.SPACE));

    final String body = templateEngine.process("email/order-confirmation-template", context);

    // Send order confirmation mail
    mailService.sendEmail(affiliateEmail, criteria.getEmail(), subject, body, true);
  }

  private boolean showReferenceTextByAff(SupportedAffiliate affiliate) {
    return !affiliate.isCzAffiliate()
        && !affiliate.isSbAffiliate();
  }

  private Map<String, List<OrderArticleItem>> getOrderArticleItems(
      final List<ShoppingCartItem> cartItems,
      final TimeZone timeZone, final Locale locale, final SupportedAffiliate affiliate,
      final NumberFormat numberFormat, final OrderConfirmationCriteria criteria) {
    // Get unique vehicle list from the list of cart items
    Set<String> vehicleSet =
        cartItems.stream().map(ShoppingCartItem::getVehicleInfo).collect(Collectors.toSet());

    // Create empty map value
    Map<String, List<OrderArticleItem>> orderArticleItems = new HashMap<>();
    vehicleSet.stream()
        .forEach(vehicleInfo -> orderArticleItems.put(vehicleInfo, Lists.newArrayList()));

    cartItems.stream().filter(item -> !item.isNonReference())
        .forEach(cartItem -> addOrderArticleItemToVehicle(orderArticleItems, cartItem,
            timeZone, locale, affiliate, numberFormat,criteria));

    // Sort result map
    Map<String, List<OrderArticleItem>> sortedMap = new TreeMap<>((key1, key2) -> {
      if (StringUtils.isBlank(key1) || StringUtils.isBlank(key2)) {
        return -1;
      }
      return key1.compareToIgnoreCase(key2);
    });
    sortedMap.putAll(orderArticleItems);
    return Collections.unmodifiableMap(sortedMap);
  }

  private void addOrderArticleItemToVehicle(
      final Map<String, List<OrderArticleItem>> orderArticleItems, final ShoppingCartItem cartItem,
      final TimeZone timeZone, final Locale locale,
      final SupportedAffiliate affiliate, NumberFormat numberFormat,
      final OrderConfirmationCriteria criteria) {
    final ArticleDocDto artDoc = cartItem.getArticle();
    if (!orderArticleItems.containsKey(cartItem.getVehicleInfo())) {
      return;
    }

    final Map<String, String> additionalTextDocMap = criteria.getAdditionalTextDocMap();
    final boolean isShowPriceType = criteria.isShowPriceType();
    final boolean isFinalUserRole = criteria.isFinalUser();
    final String arrivalTime =
        orderConfirmMailBuilderFactory.getDisplayedArrivalTime(cartItem, timeZone,
            locale, isFinalUserRole);
    final String priceType =
        orderConfirmMailBuilderFactory.getDisplayedPriceType(cartItem, isShowPriceType, affiliate);

    Double grossPrice = getGrossPrice(cartItem);

    final String customerCurrency = criteria.getCustomerCurrency();
    final String translateCurrency = messageSource.getMessage(customerCurrency, null,
        customerCurrency, locale);

    final String grossPriceWithCurrency =
        toPriceStringWithCurrency(grossPrice, affiliate, numberFormat, locale, translateCurrency);
    final String netPriceWithCurrency =
        toPriceStringWithCurrency(cartItem.getNetPrice(), affiliate, numberFormat, locale,
            translateCurrency);
    final String finalCustomerNetPriceWithCurrency = toPriceStringWithCurrency(
        cartItem.getFinalCustomerNetPrice(), affiliate, numberFormat, locale, translateCurrency);
    // #1277: ArtID/SAGsysID
    List<OrderArticleItem> articleItems = orderArticleItems.get(cartItem.getVehicleInfo());
    articleItems.add(
        OrderArticleItem.builder().amount(cartItem.getQuantity())
        .artnrDisplay(artDoc.getArtnrDisplay())
        .articleText(buildArticleDescription(cartItem, artDoc, locale))
        .grossPrice(grossPriceWithCurrency).netPrice(netPriceWithCurrency)
        .remark(additionalTextDocMap.get(cartItem.getCartKey()))
        .arrivalDate(arrivalTime)
        .priceType(priceType)
        .finalCustomerNetPrice(finalCustomerNetPriceWithCurrency).build());
    if (cartItem.hasAttachedCartItems()) {
      cartItem.getAttachedCartItems().forEach(
          attachedCartItem -> articleItems.add(buildAttachedCartItemInfo(attachedCartItem, locale,
              affiliate, cartItem.getQuantity(), numberFormat, false, translateCurrency)));
    }
  }

  private static Double getGrossPrice(final ShoppingCartItem cartItem) {
    Double grossPrice = cartItem.getGrossPrice();
    if (NumberUtils.DOUBLE_ZERO.equals(grossPrice)) {
      return cartItem.getOriginalBrandPrice();
    }
    return grossPrice;
  }

  private OrderArticleItem buildAttachedCartItemInfo(ShoppingCartItem attachedCartItem,
      final Locale locale, final SupportedAffiliate affiliate, int quantity,NumberFormat numberFormat,
      boolean isShowPriceType, String customerCurrency) {
    if (Objects.isNull(attachedCartItem.getArticle())) {
      return null;
    }
    ArticleDocDto artDoc = attachedCartItem.getArticle();

    String priceType =
        orderConfirmMailBuilderFactory.getDisplayedPriceType(attachedCartItem, isShowPriceType, affiliate);

    Double grossPrice = getGrossPrice(attachedCartItem);

    return OrderArticleItem.builder().amount(quantity)
        .artnrDisplay(Objects.isNull(artDoc.getArticle()) ? StringUtils.EMPTY
            : artDoc.getArtnrDisplay())
        .articleText(buildArticleDescription(attachedCartItem, artDoc, locale))
        .grossPrice(toPriceStringWithCurrency(grossPrice, affiliate, numberFormat, locale,
            customerCurrency))
        .netPrice(toPriceStringWithCurrency(artDoc.getPrice().getPrice().getNetPrice(), affiliate,
            numberFormat, locale, customerCurrency))
        .priceType(priceType)
        .finalCustomerNetPrice(toPriceStringWithCurrency(
            attachedCartItem.getFinalCustomerNetPrice(), affiliate, numberFormat, locale,
            customerCurrency))
        .build();
  }


  private String toPriceStringWithCurrency(Double price, SupportedAffiliate affiliate,
      NumberFormat format, Locale locale, String currencyCustomer) {
    return (price == null || NumberUtils.DOUBLE_ZERO.equals(price))
        ? messageSource.getMessage("mail.order_confirm.article_without_price", null, locale)
            : currencyParser.parse(price, format, affiliate, currencyCustomer);
  }

  private String buildArticleDescription(final ShoppingCartItem cartItem,
      final ArticleDocDto artDoc, final Locale locale) {
    if (cartItem.isVin()) {
      return StringUtils.defaultIfBlank(cartItem.getItemDesc(), StringUtils.EMPTY);
    }
    if (cartItem.isPfand()) {
      return StringUtils.defaultIfBlank(messageSource.getMessage("shopping.pfand", null, locale),
          StringUtils.EMPTY);
    }
    if (cartItem.isDepot()) {
      return StringUtils.defaultIfBlank(messageSource.getMessage("shopping.depot", null, locale),
          StringUtils.EMPTY);
    }
    if (cartItem.isRecycle()) {
      return StringUtils.defaultIfBlank(messageSource.getMessage("shopping.recycle", null, locale),
          StringUtils.EMPTY);
    }
    if (cartItem.isVoc()) {
      return StringUtils.defaultIfBlank(messageSource.getMessage("shopping.voc", null, locale),
          StringUtils.EMPTY);
    }
    if (cartItem.isVrg()) {
      return StringUtils.defaultIfBlank(messageSource.getMessage("shopping.vrg", null, locale),
          StringUtils.EMPTY);
    }
    final StringBuilder sb = new StringBuilder();
    if (!CollectionUtils.isEmpty(artDoc.getGenArtTxts())) {
      sb.append(StringUtils.defaultIfBlank(
          cartItem.getArticleItem().getGenArtTxts().get(0).getGatxtdech(), StringUtils.EMPTY))
          .append(SagConstants.SPACE);
    }
    
    sb.append(StringUtils.defaultIfBlank(artDoc.getSupplier(), StringUtils.EMPTY))
        .append(SagConstants.SPACE);
    sb.append(StringUtils.defaultIfBlank(artDoc.getProductAddon(), StringUtils.EMPTY));
    
    return sb.toString();
  }

  private static String buildAddress(final Address address) {
    return new AddressBuilder().companyName(address.getCompanyName()).street(address.getStreet())
        .postCode(address.getPostCode()).city(address.getCity()).country(address.getCountry())
        .build();
  }

  private static String joinWithSpace(String... strings) {
    final List<String> strs =
        Stream.of(strings).map(str -> StringUtils.defaultIfBlank(str, StringUtils.EMPTY))
            .collect(Collectors.toList());
    return StringUtils.join(strs, StringUtils.SPACE);
  }

}
