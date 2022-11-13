package com.sagag.services.elasticsearch.api;

import com.sagag.services.elasticsearch.domain.vehicle.VehicleGenArtDoc;

import java.util.List;

public interface VehicleGenArtService {

  /**
   * Returns a list of generic articles belongs to the selected Vehicle by the vehicle ID.
   *
   * @param vehicleId the vehicle ID.
   * @return a list of {@link VehicleGenArtDoc}.
   */
  List<VehicleGenArtDoc> getVehicleGenArts(final String vehicleId);

  /**
   * Returns all generic articles from a selected vehicle id.
   *
   * @param vehicleId the selected vehicle id.
   * @return a list of generic article ids.
   */
  List<String> findGenericArticlesByVehicle(final String vehicleId);
}
