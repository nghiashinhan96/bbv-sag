package com.sagag.services.service.exporter.wss;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class WssFinalCustomerOrderFileExporter {

  protected static String[] FINAL_CUSTOMER_ORDER_CSV_EXPORT_HEADER = {
      "report.wss.finalcustomer.order.articlenumber", "report.wss.finalcustomer.order.article_desc",
      "report.wss.finalcustomer.order.quantity", "report.wss.finalcustomer.order.gross_price",
      "report.wss.finalcustomer.order.net_price", "report.wss.finalcustomer.order.total_price" };
  protected static final int HEADER_ROW_INDEX = 0;
  protected static final String FILE_NAME = "FinalCustomerOrder";
  protected static final String DEFAULT_SHEET_NAME = "FinalCustomerOrder";

  @Autowired
  protected MessageSource messageSource;

  protected List<String[]> getFinalCustomerOrderItemsInfo(
      final List<WssFinalCustomerOrderExportItemDto> exportItems) {
    List<String[]> dataForExport = new ArrayList<>();
    exportItems.stream().forEach(item -> {
      dataForExport.add(getWssMarginArticleGroupItemInfo(item));
      CollectionUtils.emptyIfNull(item.getAttachedItems())
              .forEach(attachedItem -> dataForExport.add(getWssMarginArticleGroupItemInfo(attachedItem)));
    });
    return dataForExport;
  }

  protected String[] getFinalCustomerOrderCsvExportHeader(
      List<WssFinalCustomerOrderExportItemDto> orderExportItemDtos) {
    Locale locale = orderExportItemDtos.get(0).getLocale();
    List<String> exportHeaders = Arrays.stream(FINAL_CUSTOMER_ORDER_CSV_EXPORT_HEADER)
        .map(key -> messageSource.getMessage(key, null, locale)).collect(Collectors.toList());
    return exportHeaders.toArray(new String[FINAL_CUSTOMER_ORDER_CSV_EXPORT_HEADER.length]);
  }

  private String[] getWssMarginArticleGroupItemInfo(WssFinalCustomerOrderExportItemDto exportItem) {
    final List<String> finalCustomerOrderItem = new ArrayList<>();
    finalCustomerOrderItem.add(exportItem.getArticleDesc());
    finalCustomerOrderItem.add(exportItem.getItemDesc());
    finalCustomerOrderItem.add(Objects.toString(exportItem.getQuantity(), StringUtils.EMPTY));
    finalCustomerOrderItem.add(exportItem.getDisplayGrossPrice());
    finalCustomerOrderItem.add(exportItem.getDisplayFinalCustomerNetPrice());
    finalCustomerOrderItem.add(exportItem.getDisplayTotalPrice());

    return finalCustomerOrderItem.toArray(new String[finalCustomerOrderItem.size()]);
  }

}
