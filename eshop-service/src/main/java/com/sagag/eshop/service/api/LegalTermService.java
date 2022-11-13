package com.sagag.eshop.service.api;

import com.sagag.services.domain.sag.legal_term.LegalTermDto;

import java.util.Date;
import java.util.List;

public interface LegalTermService {

  /**
   * Returns legal terms by customer id, language and user first login date.
   *
   * @param orgId
   * @param language
   * @param firstLoginDate the user first login date
   * @return the all legal terms
   */
  List<LegalTermDto> getLegalTerms(Integer orgId, String language, Date firstLoginDate);

  /**
   * Accepts legal term by id and customer id.
   *
   * @param termId
   * @param orgId
   */
  void acceptLegalTerm(Long termId, Integer orgId);

  /**
   * Checks legal terms is expired or not.
   *
   * @param orgId
   * @param language
   * @param firstLoginDate
   * @return true if expired, otherwise
   */
  boolean hasExpiredTerms(Integer orgId, String language, Date firstLoginDate);

  /**
   * Checks customer is accepted all legal terms or not.
   *
   * @param orgId
   * @param language
   * @return true if expired, otherwise
   */
  boolean isAllTermsAccepted(Integer orgId, String language);

  /**
   * Returns un-accepted legal terms by customer id, language and user first login date.
   *
   * @param orgId
   * @param language
   * @param firstLoginDate the user first login date
   * @return the un-accepted legal terms
   */
  List<LegalTermDto> getUnacceptedTerms(Integer orgId, String language, Date firstLoginDate);

}
