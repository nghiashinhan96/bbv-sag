package com.sagag.services.domain.sag.financialcard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialCardAmountDto implements Serializable {

  private static final long serialVersionUID = 145942868020939038L;

  private Double postedBalance;

  private Double inProcessAmount;

}
