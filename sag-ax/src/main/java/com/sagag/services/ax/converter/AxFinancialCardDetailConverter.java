package com.sagag.services.ax.converter;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.sagag.services.ax.domain.financialcard.AxFinancialCardDetail;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.sag.financialcard.FinancialCardDetailDto;

@Component
@AxProfile
public class AxFinancialCardDetailConverter
    implements Function<AxFinancialCardDetail, FinancialCardDetailDto> {

  @Override
  public FinancialCardDetailDto apply(AxFinancialCardDetail axFinancialCardDetail) {
    if (axFinancialCardDetail == null) {
      return null;
    }
    return axFinancialCardDetail.toDto();
  }
}
