package com.sagag.services.service.exporter.shoppingcart;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class ShoppingCartFileExporter {

  protected static final String EMPTY_CART_ITEMS_MSG = "The given cart items must not be empty";

  protected static final char CSV_DELIMITER = ',';
  protected static final int HEADER_ROW_INDEX = 0;
  protected static final String DEFAULT_SHEET_NAME = "shoppingbasket";
  protected static final String FILE_NAME = "shoppingbasket";

  protected String[] getHeaderColumns(List<ShoppingCartExportItemDto> rows) {
    final ShoppingCartExportItemDto firstRow = rows.get(NumberUtils.INTEGER_ZERO);
    return StringUtils.split(firstRow.getHeaderTitle(), CSV_DELIMITER);
  }

  protected static List<String[]> getShoppingCartItemsInfo(
      final List<ShoppingCartExportItemDto> data) {
    List<String[]> dataForExport = new ArrayList<>();
    data.stream().forEach(item -> dataForExport.add(getCartItemInfo(item)));
    return dataForExport;
  }

  protected static String[] getCartItemInfo(ShoppingCartExportItemDto item) {
    final List<String> cartItemInfos = new ArrayList<>();
    cartItemInfos.add(item.getArticleNumber());
    cartItemInfos.add(item.getArticleDescription());
    cartItemInfos.add(String.valueOf(item.getQuantity()));
    if (Objects.nonNull((item.getGrossPriceType()))) {
      cartItemInfos.add(item.getGrossPriceType());
    }
    cartItemInfos.add(item.getGrossPrice());
    if (!StringUtils.isEmpty(item.getNetPrice())) {
      cartItemInfos.add(item.getNetPrice());
    }
    cartItemInfos.add(item.getTotal());

    return cartItemInfos.toArray(new String[cartItemInfos.size()]);
  }

  protected static List<String[]> getShortShoppingCartItemsInfo(
      final List<ShortShoppingCartExportItemDto> data) {
    List<String[]> dataForExport = new ArrayList<>();
    data.stream().forEach(item -> dataForExport.add(getShortCartItemInfo(item)));
    return dataForExport;
  }

  protected static String[] getShortCartItemInfo(ShortShoppingCartExportItemDto item) {
    final List<String> cartItemInfos = new ArrayList<>();
    cartItemInfos.add(item.getArticleNumber());
    cartItemInfos.add(String.valueOf(item.getQuantity()));

    return cartItemInfos.toArray(new String[cartItemInfos.size()]);
  }

}
