package com.sagag.services.domain.eshop.category;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterBarDto implements Serializable {

  private static final long serialVersionUID = 8672408675133776347L;

  private String filterCaid;

  private String filterSort;

  private String filterDefault;

  private String filterBar;

}
