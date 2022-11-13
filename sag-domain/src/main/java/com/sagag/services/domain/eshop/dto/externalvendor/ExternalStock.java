package com.sagag.services.domain.eshop.dto.externalvendor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalStock implements Serializable {

  private static final long serialVersionUID = 761813573207708671L;

  private String articleId;
  private String vendorId;
  private Double stock;

  public Double getStock() {
    return Optional.ofNullable(stock).map(item -> item).orElse(NumberUtils.DOUBLE_ZERO);
  }
}
