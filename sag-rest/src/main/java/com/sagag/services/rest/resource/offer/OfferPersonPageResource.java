package com.sagag.services.rest.resource.offer;

import com.sagag.eshop.service.dto.offer.ViewOfferPersonDto;
import com.sagag.services.rest.resource.DefaultResource;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.domain.Page;

@Data
@EqualsAndHashCode(callSuper = false)
public class OfferPersonPageResource extends DefaultResource {

  private final Page<ViewOfferPersonDto> offerPersons;

  public static OfferPersonPageResource of(Page<ViewOfferPersonDto> offerPersons) {
    return new OfferPersonPageResource(offerPersons);
  }

}
