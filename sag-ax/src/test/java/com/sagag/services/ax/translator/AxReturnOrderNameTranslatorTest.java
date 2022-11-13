package com.sagag.services.ax.translator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.article.api.request.returnorder.ReturnedOrderPosition;
import com.sagag.services.ax.enums.AxPaymentType;

/**
 * Class to verify {@link AxReturnOrderNameTranslator}.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AxReturnOrderNameTranslatorTest {

  private static final String BAR = AxPaymentType.BAR.getCode();

  private static final String SOFORT = AxPaymentType.SOFORT.getCode();

  @InjectMocks
  private AxReturnOrderNameTranslator translator;

  @Test(expected = UnsupportedOperationException.class)
  public void testTranslateToConnect() {
    translator.translateToConnect(StringUtils.EMPTY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllArgEx_WithEmptyList() {
    translator.translateToAx(Collections.emptyList());
  }

  @Test
  public void shouldReturn_RetConBar() {
    final List<ReturnedOrderPosition> positions = Arrays.asList(
        createReturnOrderPositionRequest("1", 1, BAR, "0T0"),
        createReturnOrderPositionRequest("2", 1, BAR, "0T1"));

    final String name = translator.translateToAx(positions);
    Assert.assertThat(name, Matchers.equalTo("RetConBar"));
  }

  @Test
  public void shouldReturn_RetConSof() {
    final List<ReturnedOrderPosition> positions = Arrays.asList(
        createReturnOrderPositionRequest("3", 1, SOFORT, "0T0"),
        createReturnOrderPositionRequest("4", 1, SOFORT, "0T1"));

    final String name = translator.translateToAx(positions);
    Assert.assertThat(name, Matchers.equalTo("RetConSof"));
  }

  @Test
  public void shouldReturn_RetConRech() {
    final List<ReturnedOrderPosition> positions = Arrays.asList(
        createReturnOrderPositionRequest("5", 1, BAR, "0T0"),
        createReturnOrderPositionRequest("6", 1, SOFORT, "0T1"));

    final String name = translator.translateToAx(positions);
    Assert.assertThat(name, Matchers.equalTo("RetConRech"));
  }

  private static ReturnedOrderPosition createReturnOrderPositionRequest(final String transId,
      final int quantity, final String axPaymentType, final String cashDis) {
    ReturnedOrderPosition position = new ReturnedOrderPosition();
    position.setTransactionId(transId);
    position.setQuantity(quantity);
    position.setAxPaymentType(axPaymentType);
    position.setCashDiscount(cashDis);
    return position;
  }
}
