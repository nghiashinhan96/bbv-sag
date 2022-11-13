package com.sagag.services.rest.resource;

import com.sagag.services.domain.sag.returnorder.ReturnOrderDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReturnOrderResource extends DefaultResource {

  private final ReturnOrderDto returnOrder;

  public static ReturnOrderResource of(ReturnOrderDto result) {
    return new ReturnOrderResource(result);
  }

}
