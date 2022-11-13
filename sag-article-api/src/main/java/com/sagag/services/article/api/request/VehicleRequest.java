package com.sagag.services.article.api.request;

import com.sagag.services.common.utils.VehicleUtils;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Class to contain vehicle request to send to ERP.
 *
 */
@Data
public class VehicleRequest implements Serializable {

  private static final long serialVersionUID = 6261707435470909981L;

  private final Long brandId;
  private final String brand;
  private final String model;
  private final String type;

  /**
   * Creates the vehicle request to ERP.
   *
   * @param vehicle the vehicle
   * @return the vehicle document {@link VehicleRequest}
   */
  public static Optional<VehicleRequest> createErpVehicleRequest(final VehicleDto vehicle) {
    if (Objects.isNull(vehicle)) {
      return Optional.empty();
    }
    final Long brandId = defaultLongNumberIfNull(vehicle.getIdMake());
    final String brand = StringUtils.defaultString(vehicle.getVehicleBrand());
    final String model = StringUtils.defaultString(vehicle.getVehicleModel());
    final String type = getTypeInfo(vehicle);
    return Optional.of(new VehicleRequest(brandId, brand, model, type));
  }

  private static Long defaultLongNumberIfNull(Integer idInt) {
    return Objects.isNull(idInt) ? null : idInt.longValue();
  }

  private static String getTypeInfo(final VehicleDto vehicle) {
    if (StringUtils.isBlank(vehicle.getVehicleName())
        || StringUtils.isBlank(vehicle.getVehiclePowerKw())
        || StringUtils.isBlank(vehicle.getVehicleEngineCode())) {
      return null;
    }
    return VehicleUtils.buildVehicleTypeDesc(
      vehicle.getVehicleName(),
      vehicle.getVehiclePowerKw(),
      vehicle.getVehicleEngineCode());
  }

}
