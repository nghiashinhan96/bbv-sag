package com.sagag.services.rest.resource.gtmotive;

import com.sagag.services.service.response.gtmotive.GtmotivePartsListSearchResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.hateoas.ResourceSupport;

@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = false)
public class GtmotivePartListSearchResponseResource extends ResourceSupport {

  private final GtmotivePartsListSearchResponse data;
}
