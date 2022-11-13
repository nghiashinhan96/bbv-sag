package com.sagag.services.service.invoice.impl;

import com.sagag.services.common.profiles.AxCzProfile;
import com.sagag.services.service.invoice.InvoiceArchiveDocumentStream;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;

@Component
@AxCzProfile
@Slf4j
public class SdokInvoiceArchiveDocumentStreamImpl implements InvoiceArchiveDocumentStream {

  @Autowired
  @Qualifier("sdokHttpClient")
  private CloseableHttpClient sdokHttpClient;

  @Override
  public byte[] streamInvoiceArchiveDoc(String invoiceArchiveUrl) {
    Assert.hasText(invoiceArchiveUrl, "The given invoice archive must not be empty");
    try {
      final HttpUriRequest uri = new HttpGet(invoiceArchiveUrl);
      final CloseableHttpResponse response = sdokHttpClient.execute(uri);
      final InputStream pdfIs = response.getEntity().getContent();
      return IOUtils.toByteArray(pdfIs);
    } catch (IOException ex) {
      log.error(ex.getMessage(), ex);
      throw new IllegalArgumentException("Streamming PDF file has problem");
    }
  }

}
