package com.sagag.services.service.feedback;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.service.dto.feedback.FeedBackCustomerUserDataDto;

import java.util.List;
import java.util.Optional;

public interface CustomerUserDataHandler {

  //@formatter:off
  default FeedBackCustomerUserDataDto getCustomerUserData(UserInfo user) {
    List<String> customerEmails = Optional.ofNullable(user.getCustomer())
        .map(Customer::getEmails)
        .orElse(null);
    List<String> customerPhones = Optional.ofNullable(user.getCustomer())
        .map(Customer::getPhones)
        .orElse(null);
    String customerName = Optional.ofNullable(user.getCustomer())
        .map(Customer::getCompanyName)
        .orElse(null);
    return FeedBackCustomerUserDataDto.builder()
        .customerNr(user.getCustNrStr())
        .customerName(customerName)
        .customerTown(user.getCity())
        .customerEmails(customerEmails)
        .customerPhones(customerPhones)
        .defaultBranch(user.getDefaultBranch())
        .userEmail(user.getEmail())
        .build();
  }
  //@formatter:on
}
