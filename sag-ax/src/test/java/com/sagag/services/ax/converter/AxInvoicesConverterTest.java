package com.sagag.services.ax.converter;

import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.sag.invoice.InvoiceDto;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AxInvoicesConverterTest {

  @InjectMocks
  private AxInvoicesConverter converter;

  @Test
  public void shouldConvertAxInvoicesResponse() throws IOException {
    final File file = new File(FilenameUtils.separatorsToSystem(
      "src/test/resources/invoices/1100005.json"));
    final List<InvoiceDto> invoices = SagJSONUtil.convertArrayJsonToList(
      FileUtils.readFileToString(file, "UTF-8"), InvoiceDto.class);
    final List<InvoiceDto> result = converter.apply(invoices);
    Assert.assertThat(result.size(), Matchers.is(3));
  }

  @Test
  public void shouldConvertAxInvoicesResponse_EmptyInvoices() {
    Assert.assertThat(converter.apply(Collections.emptyList()).isEmpty(), Matchers.is(true));
  }

  @Test
  public void testConvertAxInvoicesWithBlankDeliveryNoteNr() throws IOException {
    final File file = new File(FilenameUtils.separatorsToSystem(
      "src/test/resources/invoices/1111792.json"));
    final List<InvoiceDto> invoices = SagJSONUtil.convertArrayJsonToList(
      FileUtils.readFileToString(file, "UTF-8"), InvoiceDto.class);
    final List<InvoiceDto> result = converter.apply(invoices);
    Assert.assertThat(result.size(), Matchers.is(3));
  }

}
