package com.sagag.services.oates.api;

import java.util.Optional;

import com.sagag.services.oates.dto.OatesEquipmentProductsDto;
import com.sagag.services.oates.dto.OatesVehicleDto;

/**
 *
 * Interface for Oates business.
 *
 */
public interface OatesService {

	/**
	 * Returns the OATES vehicle information by vehicleId.
	 *
	 * @param vehicleId
	 * @return the optional of <code>OatesVehicleDto</code>
	 */
	Optional<OatesVehicleDto> searchOatesVehicle(String vehicleId);

	/**
	 * Returns the list of OATES applications by href string.
	 *
	 * @param href the selected type id
	 * @return the instance of <code>OatesEquipmentProductsDto</code>
	 */
	OatesEquipmentProductsDto searchOatesEquipment(String href);
}
