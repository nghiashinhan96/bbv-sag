package com.sagag.services.ax.converter;

import com.sagag.services.ax.domain.invoice.AxInvoice;
import com.sagag.services.ax.translator.AxPaymentTypeTranslator;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.domain.sag.invoice.InvoiceDto;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AxInvoiceConverterTest {

  @InjectMocks
  private AxInvoiceConverter converter;
  @Mock
  private AxPaymentTypeTranslator axPaymentTypeTranslator;

  @Test
  public void shouldConvertAxInvoice_NullObj() {
    Assert.assertThat(converter.apply(null), Matchers.nullValue());
  }

  @Test
  public void shouldConvertAxInvoice() {
    Mockito.when(axPaymentTypeTranslator.translateToConnect(Mockito.any()))
      .thenReturn(PaymentMethodType.CARD);
    Assert.assertThat(converter.apply(new AxInvoice()), Matchers.instanceOf(InvoiceDto.class));
  }
}
