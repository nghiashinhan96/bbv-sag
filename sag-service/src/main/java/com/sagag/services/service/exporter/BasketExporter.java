package com.sagag.services.service.exporter;

import com.sagag.eshop.repo.api.BranchRepository;
import com.sagag.eshop.repo.entity.Branch;
import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.formatter.NumberFormatterContext;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.exporter.ExportConstants;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.common.exporter.StreamedRtfExporter;
import com.sagag.services.common.exporter.StreamedWordExporter;
import com.sagag.services.common.exporter.SupportedExportType;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.hazelcast.domain.user.EshopBasketContext;
import com.sagag.services.hazelcast.domain.user.EshopContext;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.exception.ExportException;
import com.sagag.services.service.utils.JasperReportGenerators;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Execution class for shopping basket content report.
 */
@Component
@Slf4j
public class BasketExporter implements StreamedRtfExporter<BasketExportCriteria>,
        StreamedWordExporter<BasketExportCriteria> {

  private static final String[] BASKET_TEMPLATES =
          new String[] { "basket.jrxml", "basket_items.jrxml", "price_items.jrxml" };

  private static final int BASKET_TEMPLATE_INDEX = 0;

  private static final int BASKET_ITEM_TEMPLATE_INDEX = 1;

  private static final int PRICE_ITEM_TEMPLATE_INDEX = 2;

  private static final String BASKET_FILE_NAME = "Angebot";

  @Autowired
  private MessageSource messageSource;

  @Autowired
  private BranchRepository branchRepo;

  @Autowired
  private CartBusinessService cartBusinessService;

  @Autowired
  private LocaleContextHelper localeContextHelper;

  @Autowired
  private NumberFormatterContext numberFormatter;

  public ExportStreamedResult export(final UserInfo user, final EshopContext eshopContext,
                                     final Boolean curentStateNetPriceView, SupportedExportType exportType)
          throws ExportException {

    ShoppingCart shoppingCart =
            cartBusinessService.checkoutShopCart(user, ShopType.DEFAULT_SHOPPING_CART, false, true);

    Assert.notNull(shoppingCart, "The given shopping basket must not be null");
    Assert.notNull(user, "The given user must not be null");

    switch (exportType) {
      case WORD:
        return exportWord(
                BasketExportCriteria.of(user, shoppingCart, eshopContext, curentStateNetPriceView));
      case RTF:
        return exportRtf(
                BasketExportCriteria.of(user, shoppingCart, eshopContext, curentStateNetPriceView));
      default:
        throw new UnsupportedOperationException("Not support this type");
    }
  }

  @Override
  public ExportStreamedResult exportWord(BasketExportCriteria criteria) throws ExportException {
    final UserInfo user = criteria.getUser();
    final JasperReport[] jasperReports = getJasperReports();
    final Locale locale = localeContextHelper.toLocale(user.getLanguage());
    NumberFormat numberFormat =
            numberFormatter.getFormatterByAffiliateShortName(user.getCollectionShortname());
    final Map<String, Object> parameters =
            bindJasperReportParameters(jasperReports, criteria, locale, numberFormat);


    // Build basket items data source
    final JRDataSource dataSource =
            getCartItemListDataSource(criteria.getShoppingCart(), locale, numberFormat);

    byte[] content;
    try {
      content = JasperReportGenerators.generateWord(jasperReports[BASKET_TEMPLATE_INDEX],
              parameters, dataSource);
    } catch (JRException ex) {
      final String msg = "Export word shopping basket has error";
      log.error(msg, ex);
      throw new ExportException(msg, ex);
    }

    return toWordResult(content, BASKET_FILE_NAME);
  }

  @Override
  public ExportStreamedResult exportRtf(final BasketExportCriteria criteria)
          throws ExportException {
    final UserInfo user = criteria.getUser();
    final JasperReport[] jasperReports = getJasperReports();
    final Locale locale = localeContextHelper.toLocale(user.getLanguage());
    NumberFormat numberFormat =
            numberFormatter.getFormatterByAffiliateShortName(user.getCollectionShortname());
    final Map<String, Object> parameters =
            bindJasperReportParameters(jasperReports, criteria, locale, numberFormat);

    // Build basket items data source
    final JRDataSource dataSource =
            getCartItemListDataSource(criteria.getShoppingCart(), locale, numberFormat);

    byte[] content;
    try {
      content = JasperReportGenerators.generateRtf(jasperReports[BASKET_TEMPLATE_INDEX], parameters,
              dataSource);
    } catch (JRException ex) {
      final String msg = "Export rtf shopping basket has error";
      log.error(msg, ex);
      throw new ExportException(msg, ex);
    }

    return toRtfResult(content, BASKET_FILE_NAME);
  }

  private Map<String, Object> bindJasperReportParameters(final JasperReport[] jasperReports,
                                                         final BasketExportCriteria criteria, final Locale locale, NumberFormat numberFormat) {

    final Map<String, Object> parameters = new HashMap<>();
    boolean isCh = criteria.getUser().getSupportedAffiliate().isChAffiliate();
    boolean isCzAx = criteria.getUser().getSupportedAffiliate().isSagCzAffiliate();
    final UserInfo user = criteria.getUser();
    final ShoppingCart shoppingCart = criteria.getShoppingCart();

    parameters.put(JRParameter.REPORT_LOCALE, locale);
    parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE,
            ResourceBundle.getBundle(ExportConstants.MESSAGES_BUNDLE, locale));

    final String currency = user.getCustomer().getCurrency();
    final String translateCurrency = messageSource.getMessage(currency, null, currency, locale);

    parameters.put("customerNumber", messageSource.getMessage("report.basket.label.customer_number",
            Arrays.array(user.getCustNrStr()), locale));

    // Build sender info from customer info
    final Map<String, String> senderInfo = buildSenderInfo(user.getDefaultBranchId());
    senderInfo.forEach(parameters::put);

    parameters.put("userNameRef", messageSource.getMessage("report.basket.label.ref",
            Arrays.array(user.getSalesUsername()), locale));

    parameters.put("currency", translateCurrency);

    parameters.put("currentDate",
            messageSource.getMessage(
                    "report.basket.label.price_request_from", Arrays.array(DateUtils
                            .toStringDate(Calendar.getInstance().getTime(), DateUtils.SWISS_DATE_PATTERN_3)),
                    locale));

    String deliveryType = StringUtils.EMPTY;
    String paymentType = StringUtils.EMPTY;
    String receiverInfo = StringUtils.EMPTY;
    String userEmail = StringUtils.EMPTY;
    final EshopBasketContext basketContext = criteria.getEshopContext().getEshopBasketContext();
    if (!Objects.isNull(basketContext)) {
      // Build delivery type
      if (basketContext.hasDeliveryTypeCode()) {
        deliveryType = messageSource.getMessage(
                "delivery." + basketContext.getDeliveryTypeDescCode().toLowerCase(), null, locale);
      }
      if (basketContext.hasPaymentMethodCode()) {
        // Build payment type
        paymentType = messageSource.getMessage(
                "payment." + basketContext.getPaymentMethodDescCode().toLowerCase(), null, locale);
      }
      if (basketContext.hasBillingAddressId()) {
        receiverInfo = buildReceiverInfo(user.getCustomer(), basketContext.getBillingAddress());
      }
    }
    user.setEmail("nghia2nguyen@bbv.vn");
    if(!Objects.isNull(user.getEmail())) {
      userEmail = user.getEmail();
    }
    parameters.put("userEmailRef", userEmail) ;
    parameters.put("deliveryType", deliveryType);
    parameters.put("paymentType", paymentType);
    parameters.put("receiverInfo", receiverInfo);

    final String vatValue = String.format("% ,.1f", user.getSettings().getVatRate())
            .replace(SagConstants.DOT, SagConstants.COMMA_NO_SPACE);
    parameters.put("vat",
            messageSource.getMessage("report.basket.label.vat", Arrays.array(vatValue.trim()), locale));

    final StringBuilder netPricePerUnitLabel = new StringBuilder();
    final StringBuilder totalPriceLabel = new StringBuilder();

    if (StringUtils.isNotBlank(translateCurrency)) {
      netPricePerUnitLabel
              .append(messageSource.getMessage("report.basket.label.netto", null, locale))
              .append(ExportConstants.NEW_LINE_HTML).append(messageSource
              .getMessage("report.basket.label.per_unit", Arrays.array(translateCurrency), locale));
      totalPriceLabel
              .append(messageSource.getMessage("report.basket.label.total_price", null, locale))
              .append(ExportConstants.NEW_LINE_HTML).append(translateCurrency);
    }

    parameters.put("netPricePerUnitLabel", netPricePerUnitLabel.toString());
    parameters.put("totalPriceLabel", totalPriceLabel.toString());

    final boolean currentStateNetPriceView =
            BooleanUtils.isTrue(criteria.getCurentStateNetPriceView());

    if (!Objects.isNull(shoppingCart)) {
      parameters.put("isCurrentStateNetPriceView", currentStateNetPriceView);
      parameters.put("totalExclVAT",
              numberFormat.format(
                      BigDecimal.valueOf(currentStateNetPriceView ? shoppingCart.getNetTotalExclVat()
                              : shoppingCart.getGrossTotalExclVat())));
      parameters.put("totalVAT",
              numberFormat.format(
                      BigDecimal.valueOf(currentStateNetPriceView ? shoppingCart.getVatTotalWithNet()
                              : shoppingCart.getVatTotal())));
      parameters.put("totalInclVAT",
              numberFormat.format(
                      BigDecimal.valueOf(currentStateNetPriceView ? shoppingCart.getNewTotalWithNetAndVat()
                              : shoppingCart.getNewTotalWithVat())));
    }

    parameters.put("basketItemReport", jasperReports[BASKET_ITEM_TEMPLATE_INDEX]);
    parameters.put("priceItemReport", jasperReports[PRICE_ITEM_TEMPLATE_INDEX]);

    if (!Objects.isNull(criteria.getEshopContext().getUserPriceContext())) {
      parameters.put("isCurrentStateNetPriceView", currentStateNetPriceView);
    }

    parameters.put(ExportConstants.UVPE,
            messageSource.getMessage("report.basket.label.uvpe", null, locale));
    parameters.put(ExportConstants.OEP,
            messageSource.getMessage("report.basket.label.oep", null, locale));
    parameters.put("isCh", isCh);
    parameters.put("isCzAx", isCzAx);

    return parameters;
  }

  private Map<String, String> buildSenderInfo(final String branchId) {

    if (StringUtils.isBlank(branchId)) {
      return Collections.emptyMap();
    }

    final Integer branchDefault = Integer.valueOf(branchId);
    final Optional<Branch> branchOpt = branchRepo.findOneByBranchNr(branchDefault);

    if (!branchOpt.isPresent()) {
      return Collections.emptyMap();
    }

    final Map<String, String> senderInfo = new HashMap<>();
    final Branch branch = branchOpt.get();

    final String senderInfoTxt =
            new StringBuilder().append(branch.getAddressDesc()).append(ExportConstants.NEW_LINE_HTML)
                    .append(branch.getAddressStreet()).append(ExportConstants.NEW_LINE_HTML)
                    .append(branch.getZip()).append(StringUtils.SPACE).append(branch.getAddressCity())
                    .append(ExportConstants.NEW_LINE_HTML).append(branch.getAddressCountry()).toString();
    senderInfo.put("senderInfo", senderInfoTxt);

    return senderInfo;
  }

  private String buildReceiverInfo(final Customer customer, final Address billingAddress) {
    final StringBuilder receiverInfo = new StringBuilder();

    append(receiverInfo, customer.getLetterSalutation());
    append(receiverInfo, customer.getName());
    append(receiverInfo, billingAddress.getStreet());
    final String city = StringUtils.isEmpty(billingAddress.getPostCode()) ? billingAddress.getCity()
            : StringUtils.SPACE + billingAddress.getCity();
    append(receiverInfo, billingAddress.getPostCode() + city);
    append(receiverInfo, billingAddress.getCountry());

    return receiverInfo.toString();
  }

  private void append(StringBuilder receiverInfo, String string) {
    if (StringUtils.isBlank(string)) {
      return;
    }
    receiverInfo.append(StringUtils.defaultIfBlank(string, StringUtils.EMPTY))
            .append(ExportConstants.NEW_LINE_HTML);
  }

  private JRDataSource getCartItemListDataSource(final ShoppingCart shoppingCart,
                                                 final Locale locale, final NumberFormat numberFormat) {
    final List<ReportGroupedBasketItemDto> reportBasketItems =
            getBasketItemsParameter(shoppingCart, locale, numberFormat);
    if (CollectionUtils.isEmpty(reportBasketItems)) {
      return new JREmptyDataSource();
    }
    return new JRBeanCollectionDataSource(reportBasketItems);
  }

  private List<ReportGroupedBasketItemDto> getBasketItemsParameter(final ShoppingCart shoppingCart,
                                                                   final Locale locale, final NumberFormat numberFormat) {
    final List<ReportGroupedBasketItemDto> reportBasketItemList = new ArrayList<>();

    final List<ShoppingCartItem> cartItems = shoppingCart.getItems();
    if (CollectionUtils.isEmpty(cartItems)) {
      return Collections.emptyList();
    }

    // For articles in context
    final List<String> vehicles = cartItems.stream().map(ShoppingCartItem::getVehicleInfo)
            .distinct().filter(item -> !item.isEmpty()).sorted().collect(Collectors.toList());

    vehicles.stream().forEach(vehicleInfo -> {
      final List<ShoppingCartItem> cartItemsWithVeh = cartItems.stream()
              .filter(item -> vehicleInfo.equals(item.getVehicleInfo())).collect(Collectors.toList());
      reportBasketItemList.add(new ReportGroupedBasketItemDto(vehicleInfo,
              getAllItemsInCart(cartItemsWithVeh, locale), numberFormat));
    });

    // For articles with non-vehicle
    final List<ShoppingCartItem> cartItemsWithoutVeh =
            cartItems.stream().filter(item -> StringUtils.EMPTY.equals(item.getVehicleInfo()))
                    .collect(Collectors.toList());
    if (!cartItemsWithoutVeh.isEmpty()) {
      reportBasketItemList.add(new ReportGroupedBasketItemDto(
              messageSource.getMessage("report.basket.label.empty_vehicle_title", null, locale),
              getAllItemsInCart(cartItemsWithoutVeh, locale), numberFormat));
    }

    return reportBasketItemList;
  }

  private List<ShoppingCartItem> getAllItemsInCart(final List<ShoppingCartItem> cartItems,
                                                   final Locale locale) {
    final List<ShoppingCartItem> items = new ArrayList<>();

    cartItems.stream().forEach(item -> {
      items.add(item);
      if (item.hasAttachedCartItems()) {
        item.getAttachedCartItems().forEach(attachedCartItem -> {
          String attachedCartItemLabel = getAttachedCartItemLabel(attachedCartItem, locale);
          attachedCartItem.getArticleItem().setFreetextDisplayDesc(attachedCartItemLabel);
          items.add(attachedCartItem);
        });
      }
    });
    return items;
  }

  private String getAttachedCartItemLabel(ShoppingCartItem attachedCartItem, Locale locale) {
    if (attachedCartItem.isPfand()) {
      return messageSource.getMessage("shopping.pfand", null, locale);
    }
    if (attachedCartItem.isDepot()) {
      return messageSource.getMessage("shopping.depot", null, locale);
    }
    if (attachedCartItem.isRecycle()) {
      return messageSource.getMessage("shopping.recycle", null, locale);
    }
    if (attachedCartItem.isVoc()) {
      return messageSource.getMessage("shopping.voc", null, locale);
    }
    if (attachedCartItem.isVrg()) {
      return messageSource.getMessage("shopping.vrg", null, locale);
    }
    return StringUtils.EMPTY;
  }

  private static JasperReport[] getJasperReports() throws ExportException {
    try {
      // Build jasper report from templates
      return JasperReportGenerators.generateJasperReports(BASKET_TEMPLATES);
    } catch (JRException ex) {
      final String msg = "Generate the jasper reports has error";
      log.error(msg, ex);
      throw new ExportException(msg, ex);
    }
  }

}
