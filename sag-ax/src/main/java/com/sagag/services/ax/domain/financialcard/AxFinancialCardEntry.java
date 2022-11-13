package com.sagag.services.ax.domain.financialcard;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.sag.financialcard.FinancialCardEntryDto;

import lombok.Data;

/**
 * Class to receive the financial card detail info from Dynamic AX ERP.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AxFinancialCardEntry implements Serializable {

  private static final long serialVersionUID = 1L;

  private Double sequence;

  private String type;

  private String nr;

  private String description;

  private String uoM;

  private Integer quantity;

  private Double unitPrice;

  private Double amountInclVAT;

  @JsonIgnore
  public FinancialCardEntryDto toDto() {
    return SagBeanUtils.map(this, FinancialCardEntryDto.class);
  }

}
