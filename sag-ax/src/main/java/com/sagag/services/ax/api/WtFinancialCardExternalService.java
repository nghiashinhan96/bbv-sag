package com.sagag.services.ax.api;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.sagag.services.article.api.request.FinancialCardDetailRequest;
import com.sagag.services.article.api.request.FinancialCardHistoryRequest;
import com.sagag.services.domain.sag.financialcard.FinancialCardAmountDto;
import com.sagag.services.domain.sag.financialcard.FinancialCardDetailDto;
import com.sagag.services.domain.sag.financialcard.FinancialCardDocDto;


public interface WtFinancialCardExternalService {

  /**
   * Returns the financial card detail by document number.
   *
   * @param compName the affiliate company name
   * @param custNr the customer number
   * @param documentNr the document number
   * @param request the query parameter {@link FinancialCardDetailRequest}
   * @return the financial card detail {@link FinancialCardDetailDto}
   */
  Optional<FinancialCardDetailDto> getFinancialCardDetail(String compName, String custNr,
      String documentNr, FinancialCardDetailRequest request);

  /**
   * Returns a history financial card Payment by customer number.
   *
   * @param companyName the affiliate company name
   * @param custNr the customer number
   * @param request the query parameter {@link FinancialCardHistoryRequest}
   * @return the page financial card {@link FinancialCardDocDto}
   */
  Page<FinancialCardDocDto> getFinancialCardHistory(String companyName, String custNr,
      FinancialCardHistoryRequest request);

  /**
   * Returns a financial card amount by customer number.
   *
   * @param companyName the affiliate company name
   * @param custNr the customer number
   * @param paymentMethod the payment method
   * @return the financial card amount {@link FinancialCardAmountDto}
   */
  Optional<FinancialCardAmountDto> getFinancialCardAmount(String companyName, String custNr,
      String paymentMethod);
}
