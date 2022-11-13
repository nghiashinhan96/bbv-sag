package com.sagag.services.ax.translator.wint;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.ax.enums.wint.WtInvoiceType;
import com.sagag.services.ax.translator.AxInvoiceTypeTranslator;
import com.sagag.services.common.enums.ErpInvoiceTypeCode;

/**
 * Class to verify {@link AxInvoiceTypeTranslator}.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class WintInvoiceTypeTranslatorTest {

  @InjectMocks
  private WintInvoiceTypeTranslator axInvoiceTypeTranslator;

  @Test
  public void givenEmptyInvoiceTypeShouldReturnSingleInvoice() {
    Assert.assertThat(axInvoiceTypeTranslator.translateToConnect(StringUtils.EMPTY),
        Matchers.is(ErpInvoiceTypeCode.SINGLE_INVOICE));
  }

  @Test
  public void givenWeeklyInvoiceTypeShouldReturnWeeklyInvoice() {
    Assert.assertThat(axInvoiceTypeTranslator.translateToConnect(
        WtInvoiceType.WEEKLY.getCode()),
        Matchers.is(ErpInvoiceTypeCode.WEEKLY_INVOICE));
  }

  @Test
  public void givenSingleInvoiceTypeShouldReturnSingleInvoice() {
    Assert.assertThat(axInvoiceTypeTranslator.translateToConnect(
        WtInvoiceType.SINGLE.getCode()),
        Matchers.is(ErpInvoiceTypeCode.SINGLE_INVOICE));
  }

}
