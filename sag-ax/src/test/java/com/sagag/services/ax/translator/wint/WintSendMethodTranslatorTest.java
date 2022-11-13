package com.sagag.services.ax.translator.wint;

import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.ax.enums.wint.WtSendMethod;
import com.sagag.services.ax.translator.AxSendMethodTranslator;
import com.sagag.services.common.enums.SendMethodType;

/**
 * Class to verify {@link AxSendMethodTranslator}.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class WintSendMethodTranslatorTest {

  @InjectMocks
  private WintSendMethodTranslator axSendMethodTranslator;

  @Test
  public void givenEmptyShouldReturnPickup() {
    assertThat(axSendMethodTranslator.translateToConnect(StringUtils.EMPTY),
        Matchers.is(SendMethodType.PICKUP.name()));
  }

  @Test
  public void givenTourShouldReturnTour() {
    assertThat(axSendMethodTranslator.translateToConnect(WtSendMethod.TOUR.getCode()),
        Matchers.is(SendMethodType.TOUR.name()));
  }

  @Test
  public void givenPickupShouldReturnPickup() {
    assertThat(axSendMethodTranslator.translateToConnect(WtSendMethod.PICKUP.getCode()),
        Matchers.is(SendMethodType.PICKUP.name()));
  }
}
