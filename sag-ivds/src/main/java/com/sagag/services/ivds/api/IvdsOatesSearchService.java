package com.sagag.services.ivds.api;

public interface IvdsOatesSearchService extends IvdsOilSearchService {

  default String buildUserKeyForOatesCache(String vehId) {
    return vehId;
  }

}
