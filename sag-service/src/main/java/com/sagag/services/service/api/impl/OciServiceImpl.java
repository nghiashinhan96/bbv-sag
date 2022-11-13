package com.sagag.services.service.api.impl;


import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.api.CustomerSettingsService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.formatter.NumberFormatterContext;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.GenArtTxtDto;
import com.sagag.services.domain.eshop.dto.OciItemDto;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.hazelcast.domain.user.EshopBasketContext;
import com.sagag.services.hazelcast.domain.user.EshopContext;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.api.OciService;
import com.sagag.services.service.utils.OciConstants;
import com.sagag.services.service.utils.OciHtmlBuilder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
public class OciServiceImpl implements OciService {

  private static final String UNIT_PCE = "PCE"; // This will be very rare. Suggest to make this a “phase 2” item.
                                              // The UOM will depend upon the article
                                              // and we will need to also convert (ex: ml <-> l)
                                              // Hardcode "PCE" for now Request From PO

  @Autowired
  private CartBusinessService cartBusinessService;

  @Autowired
  private CustomerSettingsService customerSettingsService;

  @Autowired
  private ContextService contextService;

  @Autowired
  private OciHtmlBuilder ociHtmlBuilder;

  @Autowired
  private NumberFormatterContext numberFormatter;

  @Override
  public String exportOrder(final UserInfo user, final String hookUrl, final ShopType shopType) {
    log.debug("exportOrder for user {}", user.getUsername());
    final ShoppingCart shoppingCart = cartBusinessService.checkoutShopCart(user, shopType, false);

    List<ShoppingCartItem> cartItems = shoppingCart.getItems();
    if (CollectionUtils.isEmpty(cartItems)) {
      throw new ValidationException("Shopping cart is empty!");
    }

    final EshopContext eshopContext = contextService.getEshopContext(user.key());
    final EshopBasketContext shoppingCartContext = eshopContext.getEshopBasketContext();

    String deliveryType = !Objects.isNull(shoppingCartContext.getDeliveryType()) &&
        ErpSendMethodEnum.TOUR.name().equals(shoppingCartContext.getDeliveryType().getDescCode())
            ? OciConstants.DELIVERY_TYPE_01
            : OciConstants.DELIVERY_TYPE_02;

    CustomerSettings customerSettings =
        customerSettingsService.findSettingsByOrgId(user.getOrganisationId());
    Assert.notNull(customerSettings,
        String.format("Not found customer setting for %d ", user.getOrganisationId()));

    final String homeBranch =
        StringUtils.defaultString(customerSettings.getHomeBranch(), StringUtils.EMPTY);
    List<OciItemDto> items = new ArrayList<>();
    cartItems.forEach(cartItem -> {
      items.add(buildOciItem(cartItem, user, deliveryType, homeBranch));
      if (cartItem.hasAttachedCartItems()) {
        cartItem.getAttachedCartItems().forEach(attachedCartItem -> items
            .add(buildOciItem(attachedCartItem, user, deliveryType, homeBranch)));
      }
    });

    return ociHtmlBuilder.buildOciHtmlContent(items, hookUrl, user.getLanguage());
  }

  private int getLeadTimeDays(Availability availability) {
    if (Objects.isNull(availability) || StringUtils.isBlank(availability.getArrivalTime())) {
      return -1; // #2155
    }

    DateTime deliveryDate = new DateTime(availability.getArrivalTime());
    DateTime requestTime = new DateTime();
    long difference = requestTime.getMillis() - deliveryDate.getMillis();
    int days = (int) (difference / (1000 * 60 * 60 * 24));

    return (days < 1 ? 1 : days);
  }

  private OciItemDto buildOciItem(ShoppingCartItem cartItem, UserInfo user, String deliveryType,
      String homeBranch) {
    ArticleDocDto article = cartItem.getArticle();
    log.debug("\narticle: {}", SagJSONUtil.convertObjectToJson(article));
    int leadTime = getLeadTimeDays(article.findAvailWithLatestTime());

    String articleNumber;
    if (cartItem.isAttachedCartItem()) {
      articleNumber = Objects.isNull(article.getArticle()) ? StringUtils.EMPTY
          : article.getArticle().getNumber();
    } else {
      articleNumber = article.getArtnr();
    }

    final double price =
        user.getSettings().isShowOciVat() ? cartItem.getNetPriceWithVat() : cartItem.getNetPrice();
    NumberFormat numberFormat =
        numberFormatter.getFormatterByAffiliateShortName(user.getCollectionShortname());
    
    String currencyCode = user.getSupportedAffiliate().isPdpAffiliate() ? user.getCustomer().getCurrency()
        : numberFormat.getCurrency().getCurrencyCode();
    
    //@formatter:off
    return OciItemDto.builder()
        .description(buildOciArticleDescription(article, articleNumber))
        .schemaType(user.getSettings().getOciEwbSchemaType())
        .categoryId(articleNumber)
        .quantity(cartItem.getQuantity())
        .unit(UNIT_PCE)
        .price(price)
        .priceUnit(1) // #4865 always set to 1
        .currency(currencyCode)
        .leadTime(leadTime) // refer to ErpHelper.filterAvailabilityResponse
        .vendor(user.getSettings().getOciVendorId())
        .vendorMat(article.getIdSagsys())
        .custField1(homeBranch)
        .custField2(deliveryType)
        .build();
    //@formatter:on
  }

  private static String buildOciArticleDescription(ArticleDocDto article, String articleNumber) {
    final List<GenArtTxtDto> genArts = article.getGenArtTxts();
    final String productText =
      CollectionUtils.isEmpty(genArts) ? StringUtils.EMPTY : genArts.get(0).getGatxtdech();

    final List<String> artDescriptionItems = new ArrayList<>();

    artDescriptionItems.add(StringUtils.defaultString(productText));
    artDescriptionItems.add(StringUtils.defaultString(articleNumber));
    artDescriptionItems.add(StringUtils.defaultString(article.getProductAddon()));

    return artDescriptionItems.stream().collect(Collectors.joining(SagConstants.COMMA));
  }

}
