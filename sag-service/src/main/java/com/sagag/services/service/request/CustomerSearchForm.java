package com.sagag.services.service.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

/**
 * Form class to submit the customer search criteria.
 */
@Data
public class CustomerSearchForm implements Serializable {

  private static final long serialVersionUID = 507724704250704574L;

  private String customerNr;

  private String telephone;

  private String text;

  private String affiliate;

  private boolean fetchUserAdmin;

  private int offset;

  private int size;

  @JsonIgnore
  public Pageable getPageable() {
    return PageRequest.of(offset, size);
  }

}
