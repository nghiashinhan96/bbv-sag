package com.sagag.services.service.utils;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.service.request.dms.DmsExportRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Slf4j
public class DmsRequestBuilder {

  private DmsExportRequest exportRequest;

  public DmsRequestBuilder exportRequest(DmsExportRequest exportRequest) {
    this.exportRequest = exportRequest;
    return this;
  }

  /**
   * Builds Order contents.
   *
   */
  public String buildContent() {
    final StringBuilder dmsContent = new StringBuilder();
    dmsContent.append(exportRequest.getDmsCommand());
    dmsContent.append(StringUtils.LF);
    dmsContent.append("PARAM:");
    dmsContent.append(exportRequest.getBasePath());
    dmsContent.append(exportRequest.getFileName());
    dmsContent.append(StringUtils.LF);
    dmsContent.append(buildGeneralInfo(exportRequest));
    dmsContent.append(buildDetailOrders(exportRequest));
    return dmsContent.toString();
  }

  private String buildGeneralInfo(DmsExportRequest data) {
    final StringBuilder generalInfo = new StringBuilder();
    generalInfo.append(data.getCustomerNr()).append(SagConstants.SEMICOLON);
    generalInfo.append(buildFieldWithQuotes(data.getRequestType())).append(SagConstants.SEMICOLON);
    generalInfo.append(buildFieldWithQuotes(data.getOrderNumber())).append(SagConstants.SEMICOLON);
    generalInfo.append(buildFieldWithQuotes(data.getOrderDate())).append(SagConstants.SEMICOLON);
    generalInfo.append(data.getTotalPriceInclVat()).append(SagConstants.SEMICOLON);
    generalInfo.append(data.getTotalPrice()).append(SagConstants.SEMICOLON);
    generalInfo.append(buildFieldWithQuotes(data.getDeliveryType())).append(SagConstants.SEMICOLON);
    generalInfo.append(buildFieldWithQuotes(data.getPaymentMethod()))
        .append(SagConstants.SEMICOLON);
    generalInfo.append(buildFieldWithQuotes(data.getCompanyName())).append(SagConstants.SEMICOLON);
    generalInfo.append(buildFieldWithQuotes(data.getStreet())).append(SagConstants.SEMICOLON);
    generalInfo.append(buildFieldWithQuotes(data.getPostCode())).append(SagConstants.SEMICOLON);
    generalInfo.append(buildFieldWithQuotes(data.getCity())).append(SagConstants.SEMICOLON);
    generalInfo.append(buildFieldWithQuotes(data.getNote()));
    if (DmsConstants.VERSION_3.equals(data.getVersion())) {
      generalInfo.append(SagConstants.SEMICOLON).append(DmsConstants.VERSION_3);
    }
    generalInfo.append(StringUtils.LF);
    log.debug("general info: " + generalInfo.toString());
    return generalInfo.toString();
  }

  private static String buildFieldWithQuotes(String value) {
    final StringBuilder stringField = new StringBuilder();
    stringField.append(SagConstants.DOUBLE_QUOTE).append(value).append(SagConstants.DOUBLE_QUOTE);
    return stringField.toString();
  }

  private String buildDetailOrders(DmsExportRequest data) {
    if (Objects.isNull(data.getOrders())) {
      return StringUtils.EMPTY;
    }
    final StringBuilder detailOrders = new StringBuilder();
    data.getOrders().forEach(order -> {
      detailOrders.append(buildFieldWithQuotes(order.getArticleNumber()))
          .append(SagConstants.SEMICOLON);
      detailOrders.append(order.getTotalGrossPriceIncl()).append(SagConstants.SEMICOLON);
      detailOrders.append(order.getTotalGrossPrice()).append(SagConstants.SEMICOLON);
      detailOrders.append(order.getGrossPrice()).append(SagConstants.SEMICOLON);
      detailOrders.append(buildFieldWithQuotes(order.getDescription()))
          .append(SagConstants.SEMICOLON);
      detailOrders.append(order.getQuantity());
      if (DmsConstants.VERSION_3.equals(data.getVersion())) {
        detailOrders.append(SagConstants.SEMICOLON).append(order.getTotalNetPriceInclVat());
        detailOrders.append(SagConstants.SEMICOLON).append(order.getTotalNetPrice());
        detailOrders.append(SagConstants.SEMICOLON).append(order.getNetPrice());
        detailOrders.append(SagConstants.SEMICOLON).append(order.getTotalUvpeIncl());
        detailOrders.append(SagConstants.SEMICOLON).append(order.getTotalUvpe());
        detailOrders.append(SagConstants.SEMICOLON).append(order.getUvpe());
      }
      detailOrders.append(StringUtils.LF);
    });
    return detailOrders.toString();
  }
}
