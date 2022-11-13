package com.sagag.services.rest.resource.offer;

import com.sagag.eshop.service.dto.offer.OfferDto;
import com.sagag.services.rest.resource.DefaultResource;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OfferResource extends DefaultResource {

  private final OfferDto offer;

  public static OfferResource of(OfferDto result) {
    return new OfferResource(result);
  }

}
