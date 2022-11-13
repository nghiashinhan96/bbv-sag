package com.sagag.services.service.invoice.impl;

import com.sagag.services.service.invoice.InvoiceArchiveDocumentStream;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DefaultInvoiceArchiveDocumentStreamImpl implements InvoiceArchiveDocumentStream {

  @Override
  public byte[] streamInvoiceArchiveDoc(String invoiceArchiveUrl) throws IOException {
    Assert.hasText(invoiceArchiveUrl, "The given invoice archive must not be empty");
    byte[] bytes;
    try (InputStream pdfIs = new URL(invoiceArchiveUrl).openStream()) {
      bytes = IOUtils.toByteArray(pdfIs);
    }
    return bytes;
  }
}
