package com.sagag.services.ax.domain.invoice;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * Class to receive the order history info from Dynamic AX ERP.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AxInvoices implements Serializable {

  private static final long serialVersionUID = 6257782038843572987L;

  private List<AxInvoice> invoices;

}
