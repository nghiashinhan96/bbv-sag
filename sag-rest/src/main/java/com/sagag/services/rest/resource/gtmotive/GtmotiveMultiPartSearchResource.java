package com.sagag.services.rest.resource.gtmotive;

import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveMultiPartSearchResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.hateoas.ResourceSupport;

@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = false)
public class GtmotiveMultiPartSearchResource extends ResourceSupport {

  private final GtmotiveMultiPartSearchResponse data;
}
