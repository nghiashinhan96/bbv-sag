package com.sagag.services.hazelcast.domain.haynespro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.common.utils.VehicleUtils;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;

@Data
public class HaynesProCacheSmartCartDto implements Serializable {

  private static final long serialVersionUID = 7652378509691515158L;

  private List<HaynesProCacheJobDto> jobs;

  private HaynesProCacheVehicleDto vehicle;

  private List<HaynesProCachePartDto> parts;

  private HaynesProCacheCustomerDto customer;

  @JsonIgnore
  public boolean hasValidParts() {
    return getVehicle() != null && !CollectionUtils.isEmpty(getParts());
  }

  @JsonIgnore
  public Set<String> getGenArtNumbers() {
    return ListUtils.emptyIfNull(getParts()).stream()
      .map(HaynesProCachePartDto::getGenartNumber)
      .collect(Collectors.toSet());
  }

  public String getVehicleId() {
    return VehicleUtils.buildVehicleId(getVehicle().getKtypnr(), getVehicle().getMotnrs());
  }
}
