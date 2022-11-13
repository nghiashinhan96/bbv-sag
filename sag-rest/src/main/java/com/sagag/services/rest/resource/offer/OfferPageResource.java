package com.sagag.services.rest.resource.offer;

import com.sagag.eshop.service.dto.offer.OfferGeneralDto;
import com.sagag.services.rest.resource.DefaultResource;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.domain.Page;

@Data
@EqualsAndHashCode(callSuper = false)
public class OfferPageResource extends DefaultResource {

  private final Page<OfferGeneralDto> offers;

  public static OfferPageResource of(Page<OfferGeneralDto> result) {
    return new OfferPageResource(result);
  }
}
