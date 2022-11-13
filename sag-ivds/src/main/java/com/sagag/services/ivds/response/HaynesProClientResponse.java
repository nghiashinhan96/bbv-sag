package com.sagag.services.ivds.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.domain.article.oil.OilTypeIdsDto;
import com.sagag.services.hazelcast.domain.haynespro.HaynesProCacheSmartCartDto;

import lombok.Data;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * HaynesPro client response DTO class.
 */
@Data
public class HaynesProClientResponse implements Serializable {

  private static final long serialVersionUID = 6045975070245997357L;

  @JsonProperty("vehicle_id")
  private String vehicleId;

  @JsonProperty("gen_arts")
  private Set<String> genArts;

  @JsonProperty("isOil")
  private boolean isOil;

  @JsonProperty("oilTypeIdsDtos")
  private List<OilTypeIdsDto> oilTypeIdsDtos;

  private boolean isOates;

  public static HaynesProClientResponse empty() {
    return new HaynesProClientResponse();
  }

  public boolean isEmpty() {
    return CollectionUtils.isEmpty(genArts);
  }

  public static HaynesProClientResponse of(String vehId, HaynesProCacheSmartCartDto hpSmartCartDto) {
    final HaynesProClientResponse response = new HaynesProClientResponse();
    final String selectedVehId = StringUtils.defaultIfBlank(vehId, hpSmartCartDto.getVehicleId());
    response.setVehicleId(selectedVehId);
    response.setGenArts(hpSmartCartDto.getGenArtNumbers());
    return response;
  }
}
