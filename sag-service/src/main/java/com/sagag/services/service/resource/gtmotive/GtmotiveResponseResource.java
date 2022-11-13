package com.sagag.services.service.resource.gtmotive;

import com.sagag.services.ivds.response.GtmotiveResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.hateoas.ResourceSupport;

/**
 * REST GTMotive response resource class.
 */
@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = false)
public class GtmotiveResponseResource extends ResourceSupport {

  private final GtmotiveResponse data;

}
