package com.sagag.services.tools.batch.offer_feature.convert_vehicle_id;

import java.util.Optional;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.elasticsearch.VehicleDoc;
import com.sagag.services.tools.domain.target.TargetOfferPosition;
import com.sagag.services.tools.service.VehicleSearchService;

import lombok.extern.slf4j.Slf4j;

@Component
@StepScope
@Slf4j
@OracleProfile
public class ConvertingVehicleIdProcessor implements ItemProcessor<TargetOfferPosition, TargetOfferPosition> {

  @Autowired
  private VehicleSearchService vehicleSearchService;

  @Override
  public TargetOfferPosition process(final TargetOfferPosition source) {
    final String vehicleIdSag = source.getVehicleId();
    Optional<VehicleDoc> vehDocOpt = vehicleSearchService.searchVehicleByIdSag(vehicleIdSag);
    if (!vehDocOpt.isPresent()) {
      log.warn("Not found any vehicle id with id sag = {}", vehicleIdSag);
      return null;
    }
    final String connectVehicleId = vehDocOpt.get().getVehId();
    log.debug("Found vehicle id of id_sag = {} --> vehId = {}", vehicleIdSag, connectVehicleId);
    source.setConnectVehicleId(connectVehicleId);
    return source;
  }
}
