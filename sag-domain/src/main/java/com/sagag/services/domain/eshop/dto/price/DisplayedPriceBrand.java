package com.sagag.services.domain.eshop.dto.price;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class DisplayedPriceBrand implements Serializable {

  private static final long serialVersionUID = 8818966672392574606L;

  private Long brandId;
  private String brand;
}
