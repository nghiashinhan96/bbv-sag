package com.sagag.services.ax.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

/**
 * Class to receive the additional price from Dynamic AX ERP.
 *
 */
@Data
@JsonPropertyOrder(
    {
      "priceType",
      "priceValue"
    })
public class AxAdditionalPrice implements Serializable {

  private static final long serialVersionUID = -1853382501624043213L;

  private String priceType;

  private Double priceValue;

}
