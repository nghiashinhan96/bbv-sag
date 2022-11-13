package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.AadAccounts;

import java.util.Optional;

/**
 * Interface to define the service APIs for AX account.
 */
public interface AxAccountService {

  Optional<AadAccounts> searchSaleAccount(final String email);
}
