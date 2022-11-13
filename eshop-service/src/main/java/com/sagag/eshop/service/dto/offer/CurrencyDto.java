package com.sagag.eshop.service.dto.offer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "isoCode", "description" })
public class CurrencyDto implements Serializable {

  private static final long serialVersionUID = 2688778417449558942L;

  private int id;

  private String isoCode;

  private String description;
}
