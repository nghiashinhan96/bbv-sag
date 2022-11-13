package com.sagag.services.rest.resource.gtmotive;

import com.sagag.services.service.response.gtmotive.GtmotiveReferenceSearchResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.hateoas.ResourceSupport;

/**
 * REST GTMotive response resource class.
 */
@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = false)
public class GtmotiveReferenceSearchResponseResource extends ResourceSupport {

  private final GtmotiveReferenceSearchResponse data;
}
