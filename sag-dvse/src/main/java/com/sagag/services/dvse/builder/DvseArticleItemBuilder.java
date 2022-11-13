package com.sagag.services.dvse.builder;

import java.util.Optional;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.availability.IArticleAvailabilityProcessor;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.NextWorkingDates;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.dvse.wsdl.dvse.ArrayOfString;
import com.sagag.services.dvse.wsdl.dvse.Item;

/**
 * Builder class to build article item response for DVSE request.
 */
@Component
@AxProfile
public class DvseArticleItemBuilder {

  private static final DateTimeFormatter DATE_FOMATTER = DateTimeFormat.forPattern("dd.MM");

  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("HH.mm");

  private static final String SOFORT = "Sofort";

  private static final String MEMO_PATTERN = "Abfahrtszeit: %s, %s";

  @Autowired
  private IArticleAvailabilityProcessor articleAvailabilityProcessor;

  @Autowired
  private IArrayOfPriceBuilder priceBuilder;

  @Autowired
  private IAvailableStateBuilder availableStateBuilder;

  /**
   * Returns the article item response for GetArticleInformation request.
   *
   * @param requestItem
   * @param articleOpt
   * @param affiliateName
   * @param sendMethodEnum
   * @param nextWorkingDate
   * @return the new object of {@link Item}
   */
  public Item buildArticeItem(final Item requestItem, final Optional<ArticleDocDto> articleOpt,
      final SupportedAffiliate affiliate, final ErpSendMethodEnum sendMethodEnum,
      final NextWorkingDates nextWorkingDate) {
    final Item newItem = new Item();

    // Copy over some basic item data
    newItem.setWholesalerArticleNumber(requestItem.getWholesalerArticleNumber());
    newItem.setSupplierId(requestItem.getSupplierId());
    newItem.setSupplierArticleNumber(requestItem.getSupplierArticleNumber());
    newItem.setGenericArticles(requestItem.getGenericArticles());
    newItem.setTecDocTypes(requestItem.getTecDocTypes());

    newItem.setPrices(priceBuilder.buildArrayOfPrice(articleOpt));

    newItem.setAvailState(availableStateBuilder.buildAvailableState(articleOpt, affiliate,
        sendMethodEnum, nextWorkingDate));

    if (!articleOpt.isPresent()) {
      return newItem;
    }
    final ArrayOfString memo = buildMemo(articleOpt.get(), sendMethodEnum);
    if (memo != null) {
      newItem.setMemo(memo);
    }
    return newItem;
  }

  private ArrayOfString buildMemo(final ArticleDocDto article,
      final ErpSendMethodEnum sendMethod) {
    if (!article.hasAvailabilities()) {
      return null;
    }
    ArrayOfString arrayOfString = new ArrayOfString();
    arrayOfString.getString().add(printMemo(article.findAvailWithLatestTime(), sendMethod));
    return arrayOfString;
  }

  private String printMemo(final Availability firstAvailability,
      final ErpSendMethodEnum sendMethod) {
    final DateTime arrivalTime = firstAvailability.getCETArrivalTime();
    boolean isSofort = articleAvailabilityProcessor.isDeliveryImmediate(sendMethod,
            arrivalTime, firstAvailability.isImmediateDelivery());
    // ex 10.08, 08:15. for now only german language
    return isSofort ? SOFORT : String.format(MEMO_PATTERN,
        DATE_FOMATTER.print(arrivalTime), TIME_FORMATTER.print(arrivalTime));
  }
}
