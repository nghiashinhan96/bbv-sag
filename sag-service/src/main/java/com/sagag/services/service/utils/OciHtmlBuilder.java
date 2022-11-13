package com.sagag.services.service.utils;

import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.common.utils.SagPriceUtils;
import com.sagag.services.domain.eshop.dto.OciItemDto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

/**
 * Builder for Oci Html Form
 */
@Component
public class OciHtmlBuilder {

  @Autowired
  private MessageSource messageSource;

  @Autowired
  private LocaleContextHelper localeContextHelper;

  public String buildOciHtmlContent(List<OciItemDto> items, String hookUrl, String language) {
    final Locale locale = localeContextHelper.toLocale(language);

    final String summitText = messageSource.getMessage("oci.form.export_to_sap", null, locale);
    final String action = StringUtils.defaultString(hookUrl, OciConstants.DEFAULT_ACTION);
    StringBuilder ociHtmlContent = new StringBuilder();
    ociHtmlContent
    .append("<html>").append(StringUtils.LF)
    .append("<body>").append(StringUtils.LF)
    .append("<form action=\"").append(action).append("\" method=post target=\"_top\">")
    .append(StringUtils.LF);
    for (int i = 0; i < items.size(); i++) {
      ociHtmlContent.append(buildOciHtmlItem(items.get(i), i + 1));
    }
    ociHtmlContent.append("<input type=\"submit\" value=\"").append(summitText)
    .append("\" id=submit1 name=submit1><br>").append(StringUtils.LF)
    .append("</form>").append(StringUtils.LF)
    .append("</body>").append(StringUtils.LF)
    .append("</html>").append(StringUtils.LF);
    return ociHtmlContent.toString();

  }

  private static String buildOciHtmlItem(OciItemDto item, int i) {
    StringBuilder itemContent = new StringBuilder();
    itemContent
    .append(buildField(i, "NEW_ITEM-DESCRIPTION", item.getDescription()))
    .append(buildField(i, "NEW_ITEM-SCHEMA_TYPE", item.getSchemaType()))
    .append(buildField(i, "NEW_ITEM-CATEGORY_ID", item.getCategoryId()))
    .append(buildField(i, "NEW_ITEM-QUANTITY", String.valueOf(item.getQuantity())))
    .append(buildField(i, "NEW_ITEM-UNIT", item.getUnit()))
    .append(buildField(i, "NEW_ITEM-PRICE", SagPriceUtils.roundHalfEvenTo2digits(item.getPrice())))
    .append(buildField(i, "NEW_ITEM-PRICEUNIT", String.valueOf(item.getPriceUnit())))
    .append(buildField(i, "NEW_ITEM-CURRENCY", item.getCurrency()))
    .append(buildField(i, "NEW_ITEM-LEADTIME", String.valueOf(item.getLeadTime())))
    .append(buildField(i, "NEW_ITEM-VENDOR", item.getVendor()))
    .append(buildField(i, "NEW_ITEM-VENDORMAT", item.getVendorMat()))
    .append(buildField(i, "NEW_ITEM-CUST_FIELD1", item.getCustField1()))
    .append(buildField(i, "NEW_ITEM-CUST_FIELD2", item.getCustField2()));
    return itemContent.toString();
  }

  private static String buildField(int index, String field, String value) {
    StringBuilder fieldContent = new StringBuilder();
    fieldContent.append("<input type=\"hidden\" name=\"")
    .append(field)
    .append("[")
    .append(index)
    .append("]\" value=\"")
    .append(StringUtils.defaultString(value))
    .append("\" />")
    .append(StringUtils.LF);
    return fieldContent.toString();
  }
}
