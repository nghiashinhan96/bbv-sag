package com.sagag.services.ax.domain.financialcard;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.sag.financialcard.FinancialCardAmountDto;

import lombok.Data;

/**
 * Class to receive the financial card amount info from Dynamic AX ERP.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AxFinancialCardAmount implements Serializable {

  private static final long serialVersionUID = -2803147708942073191L;

  private Double postedBalance;

  private Double inProcessAmount;

  @JsonIgnore
  public FinancialCardAmountDto toDto() {
    return SagBeanUtils.map(this, FinancialCardAmountDto.class);
  }
}
