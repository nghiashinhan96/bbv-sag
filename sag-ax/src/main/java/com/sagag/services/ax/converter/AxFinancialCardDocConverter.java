package com.sagag.services.ax.converter;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.sagag.services.ax.domain.financialcard.AxFinancialCarDoc;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.sag.financialcard.FinancialCardDocDto;

@Component
@AxProfile
public class AxFinancialCardDocConverter
    implements Function<AxFinancialCarDoc, FinancialCardDocDto> {

  @Override
  public FinancialCardDocDto apply(AxFinancialCarDoc axFinancialCarDoc) {
    if (axFinancialCarDoc == null) {
      return null;
    }
    return axFinancialCarDoc.toDto();
  }
}
