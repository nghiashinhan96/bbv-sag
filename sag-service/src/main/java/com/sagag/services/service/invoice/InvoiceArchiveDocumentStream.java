package com.sagag.services.service.invoice;

import java.io.IOException;

public interface InvoiceArchiveDocumentStream {

  /**
   * Streams invoice archive document.
   *
   * @param invoiceArchiveUrl the file url
   * @return the binary object of streamed file.
   */
  byte[] streamInvoiceArchiveDoc(String invoiceArchiveUrl) throws IOException;

}
