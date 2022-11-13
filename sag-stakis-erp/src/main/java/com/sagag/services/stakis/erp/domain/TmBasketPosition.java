package com.sagag.services.stakis.erp.domain;

import com.sagag.services.article.api.request.BasketPosition;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TmBasketPosition extends BasketPosition {

  private static final long serialVersionUID = 4899375846218749677L;

  private Integer ktType;

}
