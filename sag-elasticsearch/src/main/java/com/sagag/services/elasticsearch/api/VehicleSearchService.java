package com.sagag.services.elasticsearch.api;

import com.sagag.services.elasticsearch.criteria.VehicleFilteringTerms;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchCriteria;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleDoc;
import com.sagag.services.elasticsearch.dto.FreetextVehicleResponse;
import com.sagag.services.elasticsearch.dto.VehicleSearchResponse;
import com.sagag.services.elasticsearch.enums.FreetextSearchOption;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface VehicleSearchService {

  /**
   * Returns the vehicle by <code>vehId</code>.
   *
   * @param vehId the vehicle id
   * @return a vehicle doc
   */
  Optional<VehicleDoc> searchVehicleByVehId(final String vehId);

  /**
   * Returns the types (in vehicles) by make and model.
   *
   * @param idMake the searching make id
   * @param idModel the searching model id
   * @param yearFrom the year from
   * @param fuelType the fuel type of vehicle
   * @return the list of {@link VehicleDoc}
   */
  List<VehicleDoc> searchTypesByMakeModel(Integer idMake, Integer idModel, String yearFrom,
    String fuelType);

  /**
   * Returns the page of vehicle doc by advance vehicle criteria.
   *
   * @param criteria the vehicle description criteria
   * @param pageable the pagination
   * @return the page of {@link VehicleDoc}
   */
  VehicleSearchResponse searchAdvanceTypesByMakeModel(VehicleSearchCriteria criteria, Pageable pageable);

  /**
   * Returns a vehicle from Gtmotive system for its make, mode, engine, transmission code.
   *
   * @param umc the vehicle make code.
   * @param modelCodes the vehicle model code list.
   * @param engineCodes the vehicle engine code list.
   * @param transmisionCodes the vehicle transmission code list.
   * @return a vehicle, can be null.
   */
  Optional<VehicleDoc> searchGtmotiveVehicle(final String umc, final List<String> modelCodes,
      final List<String> engineCodes, final List<String> transmisionCodes);

  /**
   * Returns the vehicle information and its aggregation attributes information.
   *
   * @param text the free text input from user
   * @param filterTerms the filering terms from aggregation
   * @param pageable the paging request
   * @return the {@link FreetextVehicleResponse}, nullable
   */
  Optional<FreetextVehicleResponse> searchVehiclesByFreetext(FreetextSearchOption option,
      final String text, final Optional<VehicleFilteringTerms> filterTerms, final Pageable pageable);

  /**
   * Returns the list of vehicle with kType number.
   *
   * @param kTypeNr the search kTypeNr
   * @return the list of {@link VehicleDoc}
   */
  List<VehicleDoc> searchVehiclesByKTypeNr(Integer kTypeNr);

  /**
   * Returns the first vehicle founded with per kType number.
   *
   * @param kTypeNr the search kTypeNr
   * @return the vehicle {@link VehicleDoc}
   */
  List<VehicleDoc> getTopResultVehicleByKtypes(List<Integer> kTypeNrs);


  /**
   * Returns the page of vehicle doc by vehicle criteria.
   *
   * @param criteria the vehicle description criteria
   * @param pageable the pagination
   * @return the page of {@link VehicleDoc}
   */
  VehicleSearchResponse searchVehiclesByCriteria(VehicleSearchCriteria criteria,
    Pageable pageable);

  /**
   * Returns the found list of vehicles by vehicle id list.
   *
   * @param vehIds the list of vehicle id
   * @return the list of <code>VehicleDoc</code>
   */
  List<VehicleDoc> searchVehiclesByVehIds(String... vehIds);

  List<VehicleDoc> searchVehiclesByMakeModelForSpecificVehicleType(String vehicleType,
      List<String> vehicleSubClass, Integer idMake, Integer model, String cupicCapacity, String year);
}
