package com.sagag.services.ax.translator;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.ax.enums.SendMethod;

/**
 * Class to verify {@link AxSendMethodTranslator}.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AtChSendMethodTranslatorTest {

  private static final String EXPECTED_TOUR = "TOUR";

  private static final String EXPECTED_PICKUP = "PICKUP";

  @InjectMocks
  private AxDefaultSendMethodTranslator axSendMethodTranslator;

  @Test
  public void testMapWithEmptyString() {
    String sendMethodCode = axSendMethodTranslator.translateToConnect(StringUtils.EMPTY);

    Assert.assertThat(sendMethodCode, Matchers.equalTo(StringUtils.EMPTY));
  }

  @Test()
  public void testMapWithNoSupportSendMethod() {
    final String noSupportSendMethod = "NO_SUPPORT";
    final String method = axSendMethodTranslator.translateToConnect(noSupportSendMethod);
    Assert.assertThat(method, Matchers.isEmptyString());
  }

  @Test
  public void testMapWithAbhSendMethod() {
    final String axSendMethod = SendMethod.ABH.name();
    final String eConnectSendMethod = axSendMethodTranslator.translateToConnect(axSendMethod);
    Assert.assertEquals(EXPECTED_PICKUP, eConnectSendMethod);
  }

  @Test()
  public void testMapWithGlSendMethod() {
    final String axSendMethod = SendMethod.GL.name();
    final String method = axSendMethodTranslator.translateToConnect(axSendMethod);
    Assert.assertThat(method, Matchers.isEmptyString());
  }

  @Test()
  public void testMapWithMontSendMethod() {
    final String axSendMethod = SendMethod.MONT.name();
    final String method = axSendMethodTranslator.translateToConnect(axSendMethod);
    Assert.assertThat(method, Matchers.isEmptyString());
  }

  @Test()
  public void testMapWithSpedSendMethod() {
    final String axSendMethod = SendMethod.SPED.name();
    final String method = axSendMethodTranslator.translateToConnect(axSendMethod);
    Assert.assertThat(method, Matchers.isEmptyString());
  }

  @Test
  public void testMapWithTourSendMethod() {
    final String axSendMethod = SendMethod.TOUR.name();
    final String eConnectSendMethod = axSendMethodTranslator.translateToConnect(axSendMethod);
    Assert.assertEquals(EXPECTED_TOUR, eConnectSendMethod);
  }

}
