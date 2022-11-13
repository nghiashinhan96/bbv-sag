package com.sagag.eshop.service.api;


import com.sagag.services.domain.eshop.dto.VinErrorLogDto;

/**
 * Interface to define VIN Error Log service APIs.
 */
public interface VinErrorLogService {

  void addVinErrorLog(VinErrorLogDto vinErrorLogDto);

}
