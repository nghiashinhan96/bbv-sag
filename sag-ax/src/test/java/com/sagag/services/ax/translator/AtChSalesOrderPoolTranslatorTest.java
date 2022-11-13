package com.sagag.services.ax.translator;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.ax.exception.AxCustomerException;
import com.sagag.services.common.enums.SupportedAffiliate;

/**
 * Class to verify {@link AxSalesOrderPoolTranslator}.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AtChSalesOrderPoolTranslatorTest {

  @InjectMocks
  private AxDefaultSalesOrderPoolTranslator translator;

  @Test
  public void shouldReturnDDAT() {
    final SupportedAffiliate affiliate = translator.translateToConnect("01");
    Assert.assertThat(affiliate, Matchers.equalTo(SupportedAffiliate.DERENDINGER_AT));
  }

  @Test
  public void shouldReturnMatikAT() {
    final SupportedAffiliate affiliate = translator.translateToConnect("02");
    Assert.assertThat(affiliate, Matchers.equalTo(SupportedAffiliate.MATIK_AT));
  }

  @Test
  public void shouldReturnDDCH() {
    final SupportedAffiliate affiliate = translator.translateToConnect("03");
    Assert.assertThat(affiliate, Matchers.equalTo(SupportedAffiliate.DERENDINGER_CH));
  }

  @Test
  public void shouldReturnTechnomag() {
    final SupportedAffiliate affiliate = translator.translateToConnect("04");
    Assert.assertThat(affiliate, Matchers.equalTo(SupportedAffiliate.TECHNOMAG));
  }

  @Test
  public void shouldReturnMatikCH() {
    final SupportedAffiliate affiliate = translator.translateToConnect("05");
    Assert.assertThat(affiliate, Matchers.equalTo(SupportedAffiliate.MATIK_CH));
  }

  @Test
  public void shouldReturnWbb() {
    final SupportedAffiliate affiliate = translator.translateToConnect("06");
    Assert.assertThat(affiliate, Matchers.equalTo(SupportedAffiliate.WBB));
  }

  @Test(expected = AxCustomerException.class)
  public void shouldThrowIllegalException() {
    translator.translateToConnect(StringUtils.EMPTY);
  }

  @Test(expected = AxCustomerException.class)
  public void shouldThrowUnSupportedOperationException() {
    translator.translateToConnect("99");
  }

}
