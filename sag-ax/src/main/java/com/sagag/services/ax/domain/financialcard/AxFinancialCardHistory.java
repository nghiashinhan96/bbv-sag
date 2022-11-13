package com.sagag.services.ax.domain.financialcard;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Class to receive the financial card history info from Dynamic AX ERP.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AxFinancialCardHistory implements Serializable {

  private static final long serialVersionUID = 4033761863160126088L;

  private List<AxFinancialCarDoc> documents;

}
