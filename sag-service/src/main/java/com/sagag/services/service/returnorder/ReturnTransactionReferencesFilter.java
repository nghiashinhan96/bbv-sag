package com.sagag.services.service.returnorder;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.sag.returnorder.TransactionReferenceDto;

import java.util.List;

@FunctionalInterface
public interface ReturnTransactionReferencesFilter {

  /**
   * Returns the transaction references in return orders.
   *
   * @param affiliate the affiliate name
   * @param reference the reference number.
   * @param userType the user type.
   * @return the list of result of {@link TransactionReferenceDto}
   */
  List<TransactionReferenceDto> filterTransactionReferences(SupportedAffiliate affiliate,
      String reference, String userType);
}
