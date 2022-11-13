package com.sagag.services.elasticsearch.api;

import com.sagag.services.elasticsearch.criteria.CustomerSearchCriteria;
import com.sagag.services.elasticsearch.domain.customer.CustomerDoc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


/**
 * Interface to expose customer search service APIs.
 */
public interface CustomerSearchService {

  /**
   * Search the customer on ES with telephone.
   *
   * <p>
   * At least the <code>telephone</code> or <code>text</code> has value and the
   * <code>affiliate</code> is mandatory.
   *
   * @param criteria the customer criteria to search.
   * @return a the page of customers document, null-able
   */
  Optional<CustomerDoc> searchCustomerByTelephone(CustomerSearchCriteria criteria);

  /**
   * Search the customer on ES with freetext.
   *
   * <p>
   * At least the <code>telephone</code> or <code>text</code> has value and the
   * <code>affiliate</code> is mandatory.
   *
   * @param criteria the customer criteria to search.
   * @param pageable the {@link Pageable}
   * @return a the page of customers document, null-able
   */
  Page<CustomerDoc> searchCustomerByFreetext(CustomerSearchCriteria criteria,
      Pageable pageable);

  /**
   * Returns the customer by number.
   *
   * @param criteria the customer criteria to search.
   * @return the optional of <{@link CustomerDoc}, null-able
   */
  Optional<CustomerDoc> searchCustomerByCustomerNumber(CustomerSearchCriteria criteria);
}
