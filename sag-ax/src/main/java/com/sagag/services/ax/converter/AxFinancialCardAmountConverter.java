package com.sagag.services.ax.converter;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.sagag.services.ax.domain.financialcard.AxFinancialCardAmount;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.sag.financialcard.FinancialCardAmountDto;

@Component
@AxProfile
public class AxFinancialCardAmountConverter
    implements Function<AxFinancialCardAmount, FinancialCardAmountDto> {

  @Override
  public FinancialCardAmountDto apply(AxFinancialCardAmount axFinancialCardAmount) {
    if (axFinancialCardAmount == null) {
      return null;
    }
    return axFinancialCardAmount.toDto();
  }
}
