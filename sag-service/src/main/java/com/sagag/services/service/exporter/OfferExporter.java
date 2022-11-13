package com.sagag.services.service.exporter;

import com.sagag.eshop.service.api.AddressFilterService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.offer.GroupedOfferPosition;
import com.sagag.eshop.service.dto.offer.OfferAddressDto;
import com.sagag.eshop.service.dto.offer.OfferDto;
import com.sagag.eshop.service.dto.offer.OfferPersonDto;
import com.sagag.eshop.service.dto.offer.OfferPositionDto;
import com.sagag.eshop.service.dto.offer.ReportGroupedOfferPositionDto;
import com.sagag.eshop.service.formatter.NumberFormatterContext;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.offer.OfferActionType;
import com.sagag.services.common.enums.offer.OfferFormatAlignmentType;
import com.sagag.services.common.enums.offer.OfferStatus;
import com.sagag.services.common.exporter.ExportConstants;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.common.exporter.StreamedPdfExporter;
import com.sagag.services.common.exporter.StreamedRtfExporter;
import com.sagag.services.common.exporter.StreamedWordExporter;
import com.sagag.services.common.exporter.SupportedExportType;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.dto.OrgPropertyOfferDto;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.ContactInfo;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.service.exception.OfferExportException;
import com.sagag.services.service.utils.JasperReportGenerators;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.stream.Collectors;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Execution class for offer report.
 */
@Component
@Slf4j
public class OfferExporter implements StreamedPdfExporter<OfferExportCriteria>,
    StreamedRtfExporter<OfferExportCriteria>, StreamedWordExporter<OfferExportCriteria> {

  private static final int OFFER_TEMPLATE_INDEX = 0;

  private static final int OFFER_ARTICLE_TEMPLATE_INDEX = 1;

  private static final String[] OFFER_TEMPLATES =
      new String[] { "offer.jrxml", "offer_articles.jrxml" };

  private static final String OFFER_FILE_PATTERN = "offer_";

  @Autowired
  private MessageSource messageSource;

  @Autowired
  private OrganisationService organisationService;

  @Autowired
  private NumberFormatterContext numberFormatter;

  @Autowired
  private AddressFilterService addressFilterService;

  @Autowired
  private LocaleContextHelper localeContextHelper;

  public ExportStreamedResult export(final UserInfo user, final OfferDto offer,
      final SupportedExportType type) throws OfferExportException {
    Assert.notNull(offer, "The given offer must not be null");
    Assert.notNull(user, "The given user must not be null");

    switch (type) {
      case PDF:
        return exportPdf(OfferExportCriteria.of(user, offer));
      case RTF:
        return exportRtf(OfferExportCriteria.of(user, offer));
      case WORD:
        return exportWord(OfferExportCriteria.of(user, offer));
      default:
        throw new UnsupportedOperationException("Not support this type");
    }
  }

  @Override
  public ExportStreamedResult exportWord(OfferExportCriteria criteria) throws OfferExportException {
    final UserInfo user = criteria.getUser();
    final OfferDto offer = criteria.getOffer();

    NumberFormat numberFormat =
        numberFormatter.getFormatterByAffiliateShortName(user.getCollectionShortname());

    final JasperReport[] jasperReports = getJasperReports();
    final Locale locale = localeContextHelper.toLocale(user.getLanguage());
    final Map<String, Object> parameters =
        bindJasperReportParameters(jasperReports, user, offer, locale, numberFormat);

    // Build offer positions data source
    final JRDataSource dataSource = getOfferPositionListDataSource(offer, locale, numberFormat);

    byte[] content;
    try {
      content = JasperReportGenerators.generateWord(jasperReports[OFFER_TEMPLATE_INDEX], parameters,
          dataSource);
    } catch (JRException ex) {
      final String msg = "Export rtf offer has error";
      log.error(msg, ex);
      throw new OfferExportException(msg, ex);
    }

    return toWordResult(content, OFFER_FILE_PATTERN + System.currentTimeMillis());
  }

  @Override
  public ExportStreamedResult exportRtf(OfferExportCriteria criteria) throws OfferExportException {
    final UserInfo user = criteria.getUser();
    final OfferDto offer = criteria.getOffer();

    NumberFormat numberFormat =
        numberFormatter.getFormatterByAffiliateShortName(user.getCollectionShortname());

    final JasperReport[] jasperReports = getJasperReports();
    final Locale locale = localeContextHelper.toLocale(user.getLanguage());
    final Map<String, Object> parameters =
        bindJasperReportParameters(jasperReports, user, offer, locale, numberFormat);

    // Build offer positions data source
    final JRDataSource dataSource = getOfferPositionListDataSource(offer, locale, numberFormat);

    byte[] content;
    try {
      content = JasperReportGenerators.generateRtf(jasperReports[OFFER_TEMPLATE_INDEX], parameters,
          dataSource);
    } catch (JRException ex) {
      final String msg = "Export rtf offer has error";
      log.error(msg, ex);
      throw new OfferExportException(msg, ex);
    }

    return toRtfResult(content, OFFER_FILE_PATTERN + System.currentTimeMillis());
  }

  @Override
  public ExportStreamedResult exportPdf(OfferExportCriteria criteria) throws OfferExportException {

    final UserInfo user = criteria.getUser();
    final OfferDto offer = criteria.getOffer();

    NumberFormat numberFormat =
        numberFormatter.getFormatterByAffiliateShortName(user.getCollectionShortname());

    final JasperReport[] jasperReports = getJasperReports();
    final Locale locale = localeContextHelper.toLocale(user.getLanguage());
    final Map<String, Object> parameters =
        bindJasperReportParameters(jasperReports, user, offer, locale, numberFormat);

    // Build offer positions data source
    final JRDataSource dataSource = getOfferPositionListDataSource(offer, locale, numberFormat);

    byte[] content;
    try {
      content = JasperReportGenerators.generatePdf(jasperReports[OFFER_TEMPLATE_INDEX], parameters,
          dataSource);
    } catch (JRException ex) {
      final String msg = "Export pdf offer has error";
      log.error(msg, ex);
      throw new OfferExportException(msg, ex);
    }

    return toPdfResult(content, OFFER_FILE_PATTERN + System.currentTimeMillis());
  }

  private static JasperReport[] getJasperReports() throws OfferExportException {
    try {
      // Build jasper report from templates
      return JasperReportGenerators.generateJasperReports(OFFER_TEMPLATES);
    } catch (JRException ex) {
      final String msg = "Generate the jasper reports has error";
      log.error(msg, ex);
      throw new OfferExportException(msg, ex);
    }
  }

  private String getCurrencyCode(UserInfo user, NumberFormat numberFormat) {
    // Just for CH/AT/CZAX10
    if (user.getSupportedAffiliate().isAtAffiliate()
        || user.getSupportedAffiliate().isChAffiliate()
        || user.getSupportedAffiliate().isSagCzAffiliate()) {
      return user.getCustomer().getCurrency();
    }
    return numberFormat.getCurrency().getCurrencyCode();
  }

  private Map<String, Object> bindJasperReportParameters(final JasperReport[] jasperReports,
      final UserInfo user, final OfferDto offer, final Locale locale, NumberFormat numberFormat) {
    Assert.notNull(offer, "The given offer must not be null");
    Assert.notNull(user.getOrganisationId(), "Organisation id must not be null");

    OrgPropertyOfferDto orgPropertiesDto =
        organisationService.findOrganisationPropertiesById(Long.valueOf(user.getOrganisationId()));
    Map<String, Object> parameters = new HashMap<>();
    parameters.put(JRParameter.REPORT_LOCALE, locale);
    parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE,
        ResourceBundle.getBundle(ExportConstants.MESSAGES_BUNDLE, locale));
    parameters.put("remark", offer.getRemark());
    parameters.put("offerNumber", offer.getOfferNr());
    parameters.put("offerStatus", OfferStatus.valueOf(offer.getStatus()));

    parameters.put("longPriceLabel",
        messageSource.getMessage("report.offer.label.article_displayed_price", null, locale));

    final String currencyCode = getCurrencyCode(user, numberFormat);
    final String translateCurrencyCode = messageSource.getMessage(currencyCode, null, currencyCode, locale);
    parameters.put("totalPriceLabel", messageSource.getMessage("report.offer.label.total",
        new Object[] { translateCurrencyCode }, locale));

    // Print offer person information
    final boolean showVendorAddr = Boolean.parseBoolean(orgPropertiesDto.getPrintVendorAddr());
    parameters.put("printVendorInformation", showVendorAddr);
    final String customerInfo =
        getOfferPersonInformation(offer.getOfferPerson(), offer.getRecipientAddress());
    if (OfferFormatAlignmentType.RIGHT.getValue()
        .equalsIgnoreCase(orgPropertiesDto.getFormatAlign())) {
      parameters.put("customerInformationRight", customerInfo);
    } else {
      parameters.put("customerInformationLeft", customerInfo);
    }

    // Print end customer information
    parameters.put("vendorInformation",
        showVendorAddr ? getCustomerInformation(user, locale) : StringUtils.EMPTY);

    // Print price info
    parameters.put("totalParts",
        numberFormat.format(BigDecimal.valueOf(offer.calcArticleAmount())));
    parameters.put("totalWork", numberFormat.format(BigDecimal.valueOf(offer.calcWorkAmount())));
    parameters.put("totalOffer",
        numberFormat.format(BigDecimal.valueOf(offer.calcTotalExcludeVatAmount())));
    parameters.put("vat", numberFormat
        .format(BigDecimal.valueOf(offer.getVat()).multiply(ExportConstants.PERCENT_UNIT)));
    parameters.put("defaultVat", numberFormat.format(BigDecimal.ZERO));

    parameters.put("totalOfferVat",
        numberFormat.format(BigDecimal.valueOf(offer.updateTotalAmount())));

    // Print offer Date
    // Default date format: dd.MM.yyyy
    parameters.put("offerDate",
        DateUtils.toStringDate(offer.getOfferDate(), DateUtils.SWISS_DATE_PATTERN_3));

    // Print remarks
    final double remarkAmount = offer.calcRemarkAmount();
    parameters.put("printRemarks", NumberUtils.compare(remarkAmount, NumberUtils.DOUBLE_ZERO) != 0);
    parameters.put("totalRemarks", numberFormat.format(BigDecimal.valueOf(remarkAmount)));
    parameters.put("totalVat", numberFormat.format(BigDecimal.valueOf(offer.calcVatAmount())));

    // Need some info to print text base on client organisation settings
    parameters.put("title", StringUtils.EMPTY);
    parameters.put("footer", StringUtils.defaultString(orgPropertiesDto.getFooterText()));

    parameters.put("SubReportParam", jasperReports[OFFER_ARTICLE_TEMPLATE_INDEX]);

    return parameters;
  }

  /**
   * Returns the customer information.
   *
   * @param locale
   * @param user
   * @return the text of <code>end customer</code>
   */
  private String getCustomerInformation(final UserInfo user, Locale locale) {
    if (!user.hasCust()) {
      return StringUtils.EMPTY;
    }

    final Customer customer = user.getCustomer();
    final StringBuilder builder = new StringBuilder();

    // [Company name] or [nothing] from SAGsys
    final String companyName = customer.getCompanyName();
    if (StringUtils.isNotBlank(companyName)) {
      builder.append(companyName).append(ExportConstants.NEW_LINE_HTML);
    }

    final Optional<Address> defaultAddressOpt =
        Optional.ofNullable(addressFilterService.getDefaultAddress(user.getAddresses()).get());
    if (!defaultAddressOpt.isPresent()) {
      builder.append(StringUtils.defaultString(buildExtendInformation(customer, locale)));
      return builder.toString();
    }

    final Address address = defaultAddressOpt.get();
    // [Street] or [nothing] from AX
    final String street = address.getStreet();
    if (StringUtils.isNotBlank(street)) {
      builder.append(street).append(ExportConstants.NEW_LINE_HTML);
    }
    // [Additional 1] or [nothing] from AX: need confirmation
    // [Additional 2] or [nothing] from AX: need confirmation
    // [Postal Code] or [nothing] from AX
    final String postalCode = address.getPostOfficeBox();
    if (StringUtils.isNotBlank(postalCode)) {
      builder.append(postalCode).append(ExportConstants.NEW_LINE_HTML);
    }
    // [ZIP] [Town] from AX
    final String zipTown = StringUtils.defaultString(address.getPostCode()) + StringUtils.SPACE
        + StringUtils.defaultString(address.getCity());
    if (StringUtils.isNotBlank(zipTown)) {
      builder.append(zipTown).append(ExportConstants.NEW_LINE_HTML);
    }

    builder.append(StringUtils.defaultString(buildExtendInformation(customer, locale)));
    return builder.toString();
  }

  private String buildExtendInformation(Customer customer, Locale locale) {
    if (Objects.isNull(customer)) {
      return StringUtils.EMPTY;
    }
    final StringBuilder builder = new StringBuilder();
    final String phone = getContactValue(customer.getPhoneContacts());
    if (StringUtils.isNotBlank(phone)) {
      builder.append(messageSource.getMessage("report.offer.label.phone", null, locale))
          .append(phone).append(ExportConstants.NEW_LINE_HTML);
    }

    final String fax = getContactValue(customer.getFaxContacts());
    if (StringUtils.isNotBlank(fax)) {
      builder.append(messageSource.getMessage("report.offer.label.fax", null, locale)).append(fax)
          .append(ExportConstants.NEW_LINE_HTML);
    }

    final String email = getContactValue(customer.getEmailContacts());
    if (StringUtils.isNotBlank(email)) {
      builder.append(email).append(ExportConstants.NEW_LINE_HTML);
    }

    final String vatNumber = customer.getVatNr();
    if (StringUtils.isNotBlank(vatNumber)) {
      builder.append(messageSource.getMessage("report.offer.label.vatNumber", null, locale))
          .append(vatNumber).append(ExportConstants.NEW_LINE_HTML);
    }

    final String extendInfo = builder.toString();
    if (StringUtils.isBlank(extendInfo)) {
      return StringUtils.EMPTY;
    }

    final StringBuilder extendInfoForm = new StringBuilder();
    extendInfoForm.append("<span style=\"font-size:4px\">").append(ExportConstants.NEW_LINE_HTML)
        .append(StringUtils.SPACE).append("</span>");
    extendInfoForm.append("<span style=\"font-size:5px\">");
    extendInfoForm.append(extendInfo);
    extendInfoForm.append("</span>");

    return extendInfoForm.toString();
  }

  /**
   * Returns the offer person information.
   *
   * @param person
   * @param recipientAddress
   * @return the text of <code>offer person</code>
   */
  private String getOfferPersonInformation(final OfferPersonDto person,
      final OfferAddressDto recipientAddress) {
    final StringBuilder builder = new StringBuilder(ExportConstants.NEW_LINE_HTML);

    final String receiverName = buildReceiverName(person);
    if (StringUtils.isNotBlank(receiverName)) {
      builder.append(receiverName).append(ExportConstants.NEW_LINE_HTML);
    }

    if (recipientAddress == null) {
      return builder.toString();
    }

    final String line1 = recipientAddress.getLine1();
    if (StringUtils.isNotBlank(line1)) {
      builder.append(line1).append(ExportConstants.NEW_LINE_HTML);
    }
    final String line2 = recipientAddress.getLine2();
    if (StringUtils.isNotBlank(line2)) {
      builder.append(line2).append(ExportConstants.NEW_LINE_HTML);
    }
    final String line3 = recipientAddress.getLine3();
    if (StringUtils.isNotBlank(line3)) {
      builder.append(line3).append(ExportConstants.NEW_LINE_HTML);
    }
    final String zipTown = StringUtils.defaultString(recipientAddress.getZipcode())
        + StringUtils.SPACE + StringUtils.defaultString(recipientAddress.getCity());
    if (StringUtils.isNotBlank(zipTown)) {
      builder.append(zipTown).append(ExportConstants.NEW_LINE_HTML);
    }

    return builder.toString();
  }

  /**
   * Builds receiver name.
   *
   * @param person
   * @return [Company name] or [First_Name Last_Name]
   */
  private String buildReceiverName(OfferPersonDto person) {
    if (Objects.isNull(person)) {
      return StringUtils.EMPTY;
    }

    final String offerCompanyName = person.getCompanyName();
    if (StringUtils.isNotBlank(offerCompanyName)) {
      return offerCompanyName;
    }
    return StringUtils.defaultString(person.getFirstName()) + StringUtils.SPACE
        + StringUtils.defaultString(person.getLastName());
  }

  /**
   * Returns the offer positions data source.
   *
   * @param offer
   * @return the result of {@link JRDataSource}
   */
  private JRDataSource getOfferPositionListDataSource(final OfferDto offer, final Locale locale,
      NumberFormat numberFormat) {
    final List<ReportGroupedOfferPositionDto> reportOfferPositions =
        getOfferPositionsParameter(offer, locale, numberFormat);
    if (CollectionUtils.isEmpty(reportOfferPositions)) {
      return new JREmptyDataSource();
    }
    return new JRBeanCollectionDataSource(reportOfferPositions);
  }

  private List<ReportGroupedOfferPositionDto> getOfferPositionsParameter(final OfferDto offer,
      final Locale locale, NumberFormat numberFormat) {
    final List<ReportGroupedOfferPositionDto> reportOfferPositionList = new ArrayList<>();

    // Articles with vehicle
    final Map<String, GroupedOfferPosition> articlesWithVehicle =
        getGroupedOfferPositions(offer.getVendorArticleList());

    // Labour time with vehicle
    final Map<String, List<OfferPositionDto>> labourTimeWithVehicle = offer.getHaynesProList()
        .stream().collect(Collectors.groupingBy(OfferPositionDto::getConnectVehicleId));

    if (!MapUtils.isEmpty(articlesWithVehicle)) {
      articlesWithVehicle.forEach((vehDes, groupedOfferPosition) -> {
        final List<OfferPositionDto> positions = groupedOfferPosition.getOfferPositions().values()
            .stream().flatMap(List::stream).collect(Collectors.toList());
        final List<OfferPositionDto> labourTimeList =
            labourTimeWithVehicle.get(groupedOfferPosition.getVehicleId());
        if (!CollectionUtils.isEmpty(labourTimeList)) {
          positions.addAll(labourTimeList);
        }
        reportOfferPositionList
            .add(new ReportGroupedOfferPositionDto(vehDes, positions, numberFormat));
      });
    }

    // Articles without vehicle
    final List<OfferPositionDto> articlesWithoutVehicle =
        offer.getVendorArticleWithoutVehicleList();
    if (!CollectionUtils.isEmpty(articlesWithoutVehicle)) {
      reportOfferPositionList.add(new ReportGroupedOfferPositionDto(
          messageSource.getMessage("report.offer.label.article_without_vehicle", null, locale),
          articlesWithoutVehicle, numberFormat));
    }

    // Client Articles
    final List<OfferPositionDto> clientArticleList = offer.getClientArticleList();
    if (!CollectionUtils.isEmpty(clientArticleList)) {
      reportOfferPositionList.add(new ReportGroupedOfferPositionDto(
          messageSource.getMessage("report.offer.label.own_article", null, locale),
          clientArticleList, numberFormat));
    }

    // Client Works
    final List<OfferPositionDto> clientWorkList = offer.getClientWorkList();
    if (!CollectionUtils.isEmpty(clientWorkList)) {
      reportOfferPositionList.add(new ReportGroupedOfferPositionDto(
          messageSource.getMessage("report.offer.label.own_work", null, locale), clientWorkList,
          numberFormat));
    }

    // Remarks
    final List<OfferPositionDto> remarkList =
        updateAdditionalArticleDescription(offer.getRemarkList(), locale, numberFormat);
    if (!CollectionUtils.isEmpty(remarkList)) {
      reportOfferPositionList.add(new ReportGroupedOfferPositionDto(
          messageSource.getMessage("report.offer.label.discount", null, locale), remarkList,
          numberFormat));
    }

    return reportOfferPositionList;
  }

  private List<OfferPositionDto> updateAdditionalArticleDescription(
      final List<OfferPositionDto> remarkOfferPositonList, final Locale locale,
      NumberFormat numberFormat) {
    if (CollectionUtils.isEmpty(remarkOfferPositonList)) {
      return Collections.emptyList();
    }

    for (OfferPositionDto position : remarkOfferPositonList) {
      if (!position.getPositionType().isRemark()) {
        continue;
      }
      final String additionalArticleDesc =
          getRemarkAdditionalArticleDescription(position, locale, numberFormat);
      position.setQuantity(NumberUtils.DOUBLE_ONE);
      if (!StringUtils.isBlank(additionalArticleDesc)) {
        position.setArticleDescription(additionalArticleDesc);
      }
    }

    return remarkOfferPositonList;
  }

  private String getRemarkAdditionalArticleDescription(@NonNull final OfferPositionDto position,
      final Locale locale, NumberFormat numberFormat) {
    final OfferActionType actionType = position.getOfferActionType();
    if (!actionType.isPercentType() && !actionType.isAmountType()) {
      return StringUtils.EMPTY;
    }
    final StringBuilder strBuilder = new StringBuilder();
    strBuilder.append(StringUtils.defaultString(position.getRemark()));
    String subDescription = StringUtils.EMPTY;
    if (actionType.isOwnWorkType()) {

      final String subMsg =
          messageSource.getMessage("report.offer.label.own_work_discount", null, locale);
      subDescription =
          appendOwnDescription(actionType, position.getActionValue(), subMsg, numberFormat);
    } else if (actionType.isOwnArticleType()) {

      final String subMsg =
          messageSource.getMessage("report.offer.label.own_articles_discount", null, locale);
      subDescription =
          appendOwnDescription(actionType, position.getActionValue(), subMsg, numberFormat);
    } else if (actionType.isDiscountPercent() || actionType.isAdditionPercent()) {

      subDescription =
          appendPercentMode(position.getActionValue(), StringUtils.EMPTY, numberFormat);
    } else if (actionType.isDiscountAmount() || actionType.isAdditionAmount()) {

      subDescription = appendAmountMode(StringUtils.EMPTY);
    }
    if (StringUtils.isNotBlank(subDescription)) {
      strBuilder.append(StringUtils.SPACE).append('(')
          .append(StringUtils.trimToEmpty(subDescription)).append(')');
    }
    return StringUtils.trimToEmpty(strBuilder.toString());
  }

  private static String appendOwnDescription(final OfferActionType actionType, final Double value,
      final String subMsg, NumberFormat numberFormat) {
    if (actionType.isPercentType()) {
      return appendPercentMode(value, subMsg, numberFormat);
    } else if (actionType.isAmountType()) {
      return appendAmountMode(subMsg);
    }
    return StringUtils.EMPTY;
  }

  private static String appendPercentMode(final Double value, final String subMsg,
      NumberFormat numberFormat) {
    final String percentModeStr =
        numberFormat.format(BigDecimal.valueOf(value).multiply(ExportConstants.PERCENT_UNIT)) + '%'
            + StringUtils.SPACE + StringUtils.trimToEmpty(subMsg);
    return StringUtils.trimToEmpty(percentModeStr);
  }

  /**
   * Appends amount value with empty value.
   *
   * <pre>
   * #2322:
   *  currency amounts must not be displayed in the first column
   *  refLink: https://app.assembla.com/spaces/sag-eshop/tickets/2322/details?comment=1357634883
   * </pre>
   */
  private static String appendAmountMode(final String subMsg) {
    return StringUtils.trimToEmpty(subMsg);
  }

  private static Map<String, GroupedOfferPosition> getGroupedOfferPositions(
      final List<OfferPositionDto> positions) {
    final Map<String, GroupedOfferPosition> groupedOfferPositions =
        new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    if (CollectionUtils.isEmpty(positions)) {
      return Collections.emptyMap();
    }
    positions.stream().forEach(position -> {
      final String vehDesc =
          StringUtils.defaultIfBlank(position.getVehicleDescription(), "zzz_OTHER_ARTICLES");
      if (!groupedOfferPositions.containsKey(vehDesc)) {
        GroupedOfferPosition groupedOfferPosition = new GroupedOfferPosition(vehDesc);
        groupedOfferPosition.setVehicleId(position.getConnectVehicleId());
        groupedOfferPosition.setModelId(position.getModelId());

        groupedOfferPositions.put(vehDesc, groupedOfferPosition);
      }
      groupedOfferPositions.get(vehDesc).addOfferPosition(position);
    });
    return groupedOfferPositions;
  }

  private String getContactValue(List<ContactInfo> contactInfos) {
    if (CollectionUtils.isEmpty(contactInfos)) {
      return StringUtils.EMPTY;
    }

    final List<String> contactValues = contactInfos.stream().filter(ContactInfo::isPrimary)
        .map(ContactInfo::getValue).filter(StringUtils::isNotBlank).collect(Collectors.toList());
    return StringUtils.join(contactValues.toArray(), SagConstants.COMMA);
  }

}
