package com.sagag.services.elasticsearch.api;

import com.sagag.services.elasticsearch.domain.vehicle.VehicleGenArtArtDoc;
import java.util.List;

public interface VehicleGenArtArtSearchService {

  /**
   * Returns the list of fitment by list of fitment id
   *
   * @param fitmentIds the list of fitment id
   * @return list of {@link VehicleGenArtArtDoc}
   */
  List<VehicleGenArtArtDoc> searchFitmentsByIds(List<String> fitmentIds);

  /**
   * Returns the fitment by vehicle id and list of ga id
   *
   * @param vehId vehicle id
   * @param gaIds the list of gaId
   * @return list of {@link VehicleGenArtArtDoc}
   */
  List<VehicleGenArtArtDoc> searchFitments(String vehId, List<String> gaIds);

  /**
   * Returns the list of fitment by vehicle and article ids.
   *
   * @param vehId vehicle id
   * @param articleIds the list of article id.
   * @return list of {@link VehicleGenArtArtDoc}
   */
  List<VehicleGenArtArtDoc> searchFitmentsByVehIdAndArticleIds(String vehId, List<String> articleIds);
}
