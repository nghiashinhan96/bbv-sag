package com.sagag.services.elasticsearch.criteria;

import lombok.Data;

import java.util.List;
import java.util.Optional;

/**
 * Customer search criteria class.
 */
@Data
public class CustomerSearchCriteria {

  private Optional<Telephone> telephone = Optional.empty();
  private String text; // can be zip, city, short name, last name or company name
  private List<String> affiliates;
  private String customerNumber;

}
