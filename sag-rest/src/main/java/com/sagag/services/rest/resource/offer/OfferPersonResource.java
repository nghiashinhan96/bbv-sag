package com.sagag.services.rest.resource.offer;

import com.sagag.eshop.service.dto.offer.OfferPersonDto;
import com.sagag.services.rest.resource.DefaultResource;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OfferPersonResource extends DefaultResource {

  private final OfferPersonDto offerPerson;

  public static OfferPersonResource of(final OfferPersonDto offerPerson) {
    return new OfferPersonResource(offerPerson);
  }
}
