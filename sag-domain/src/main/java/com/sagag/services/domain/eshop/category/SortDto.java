package com.sagag.services.domain.eshop.category;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SortDto implements Serializable {

  private static final long serialVersionUID = 4025827256424923860L;

  private List<String> affiliates;

  private List<BrandDto> brands;

}
