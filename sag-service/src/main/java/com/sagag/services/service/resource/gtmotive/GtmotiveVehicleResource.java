package com.sagag.services.service.resource.gtmotive;

import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.gtmotive.domain.response.GtmotiveVehicleDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = false)
public class GtmotiveVehicleResource extends ResourceSupport {

  private final GtmotiveVehicleDto gtmotiveResponse;

  private final VehicleDto vehicle;

}
