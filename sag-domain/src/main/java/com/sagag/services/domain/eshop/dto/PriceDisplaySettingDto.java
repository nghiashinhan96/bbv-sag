package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceDisplaySettingDto implements Serializable {

  private static final long serialVersionUID = -5839263792813541713L;

  private Integer id;

  private boolean enable;

  private boolean editable;

  private String descriptionCode;

}
