package com.sagag.services.rest.resource.gtmotive;

import com.sagag.services.service.response.gtmotive.GtmotiveVehicleSearchResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.hateoas.ResourceSupport;

@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = false)
public class GtmotiveVehicleSearchResponseResource extends ResourceSupport {

  private final GtmotiveVehicleSearchResponse data;
}
