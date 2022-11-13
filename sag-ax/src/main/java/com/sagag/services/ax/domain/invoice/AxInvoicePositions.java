package com.sagag.services.ax.domain.invoice;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * Class to receive the list of invoice positions from Dynamic AX ERP.
 *
 */
@Data
public class AxInvoicePositions implements Serializable {

  private static final long serialVersionUID = 6184149771939942247L;

  private List<AxInvoicePosition> invoicePositions;
}
