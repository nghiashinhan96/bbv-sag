package com.sagag.services.service.customer;

import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.elasticsearch.criteria.CustomerSearchCriteria;
import com.sagag.services.elasticsearch.domain.customer.CustomerAddress;
import com.sagag.services.elasticsearch.domain.customer.CustomerDoc;
import com.sagag.services.service.dto.CustomerDataResultDto;
import com.sagag.services.service.exception.ErpCustomerNotFoundException;
import com.sagag.services.service.exception.TelephoneFormatException;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Component
@Slf4j
public class CustomerFreetextSearch extends AbstractCustomerSearch {

  @Override
  public CustomerDataResultDto search(final SupportedAffiliate affiliate, final String freetext,
      final Pageable pageable) throws TelephoneFormatException, UserValidationException,
  ErpCustomerNotFoundException {

    final List<String> supportedAffiliates =
        getAffiliateCompanyNamesWithinCountry(affiliate.getAffiliate());

    final Page<Customer> customers = executeCustomerSearchByFreetext(freetext,
        supportedAffiliates, pageable);
    return CustomerDataResultDto.of(customers);
  }

  private Page<Customer> executeCustomerSearchByFreetext(String freetext, List<String> affiliates,
      Pageable pageable) {

    final CustomerSearchCriteria criteria = new CustomerSearchCriteria();
    criteria.setAffiliates(affiliates);
    criteria.setText(freetext);
    if (StringUtils.isBlank(criteria.getText())) {
      log.error("Invalid search criteria, at least the freetext exists");
      throw new UnsupportedOperationException("Invalid customer search arguments");
    }
    return customerESService.searchCustomerByFreetext(criteria, pageable)
        .map(customerConverter(freetext));
  }

  private static Function<CustomerDoc, Customer> customerConverter(final String freetext) {
    return doc -> {
      final Customer customer = Customer.builder()
          .nr(NumberUtils.createLong(doc.getCustomerAccountNr())).shortName(doc.getNameAlias())
          .name(doc.getCustomerName()).affiliateName(doc.getMandant()).build();
      final List<CustomerAddress> addresses = doc.getAddresses();
      if (CollectionUtils.isEmpty(addresses)) {
        return customer;
      }
      final Optional<CustomerAddress> selectedAddressOpt;
      if (CollectionUtils.size(addresses) == NumberUtils.INTEGER_ONE) {
        selectedAddressOpt = addresses.stream().findFirst();
      } else {
        selectedAddressOpt =
            addresses.stream().filter(findMatchedAddressByFreetext(freetext)).findFirst();
      }
      selectedAddressOpt.ifPresent(item -> {
        customer.setZipCode(item.getZip());
        customer.setCity(item.getCity());
        customer.setAddressStreet(item.getAddressStreet());
      });
      return customer;
    };
  }

  private static Predicate<CustomerAddress> findMatchedAddressByFreetext(final String freetext) {
    return address -> address.isPrimary()
        || anyMatchWithFreetext(freetext, address.getCity(), address.getZip());
  }

  private static boolean anyMatchWithFreetext(final String freetext, final String... addressProps) {
    return !ArrayUtils.isEmpty(addressProps)
        && Stream.of(addressProps).anyMatch(prop -> StringUtils.containsIgnoreCase(freetext, prop));
  }
}
