package com.sagag.services.ax.translator;

import com.sagag.services.article.api.request.returnorder.ReturnedOrderPosition;
import com.sagag.services.ax.enums.AxPaymentType;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class AxReturnOrderNameTranslator
    implements AxDataTranslator<String, List<ReturnedOrderPosition>> {

  private static final String[] RET_CON_BAR_PAYMENT_TYPES = { AxPaymentType.BAR.getCode(),
    AxPaymentType.KARTE.getCode() };

  @Override
  public List<ReturnedOrderPosition> translateToConnect(String source) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String translateToAx(List<ReturnedOrderPosition> positions) {
    Assert.notEmpty(positions, "The given positions must not be empty");
    if (allMatchRetConBar().test(positions)) {
      return "RetConBar";
    } else if (allMatchRetConSof().test(positions)) {
      return "RetConSof";
    }
    return "RetConRech";
  }

  private static Predicate<List<ReturnedOrderPosition>> allMatchRetConBar() {
    return positions -> positions.stream().allMatch(isRetConBar())
      && is0T0CashDiscount().test(positions);
  }

  private static Predicate<ReturnedOrderPosition> isRetConBar() {
    return position -> Stream.of(RET_CON_BAR_PAYMENT_TYPES)
      .anyMatch(code -> StringUtils.equalsIgnoreCase(code, position.getAxPaymentType()));
  }

  private static Predicate<List<ReturnedOrderPosition>> allMatchRetConSof() {
    return positions -> {
      boolean isBar = positions.stream()
          .allMatch(position -> StringUtils.equalsIgnoreCase(AxPaymentType.SOFORT.getCode(),
              position.getAxPaymentType()));
      return isBar && is0T0CashDiscount().test(positions);
    };
  }

  private static Predicate<List<ReturnedOrderPosition>> is0T0CashDiscount() {
    return positions -> positions.stream()
        .anyMatch(position -> StringUtils.equalsIgnoreCase("0T0", position.getCashDiscount()));
  }
}
