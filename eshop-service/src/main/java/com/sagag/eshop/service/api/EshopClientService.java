package com.sagag.eshop.service.api;

import com.sagag.eshop.service.dto.client.CreatedEshopClientDto;
import com.sagag.eshop.service.dto.client.EshopClientCriteria;
import com.sagag.eshop.service.dto.client.EshopClientDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EshopClientService {

  /**
   * Returns the client details by client name.
   *
   * @param clientName
   * @return the optional of {@link EshopClientDto}
   */
  Optional<EshopClientDto> findActiveClientByClientName(String clientName);

  /***/
  CreatedEshopClientDto createEshopClient(EshopClientCriteria eshopClientCreateDto);

  /***/
  Page<EshopClientDto> findAllActiveEshopClient(Pageable pageable);
}
