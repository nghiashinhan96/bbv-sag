package com.sagag.services.tools.service;

import com.sagag.services.tools.domain.elasticsearch.VehicleDoc;

import java.util.Optional;

public interface VehicleSearchService {

  Optional<VehicleDoc> searchVehicleByIdSag(String idSag);

}
