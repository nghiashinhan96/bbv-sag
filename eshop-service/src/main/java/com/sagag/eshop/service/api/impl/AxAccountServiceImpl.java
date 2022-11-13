package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.AadAccountsRepository;
import com.sagag.eshop.repo.entity.AadAccounts;
import com.sagag.eshop.service.api.AxAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Class to define service APIs for AX account.
 */
@Service
public class AxAccountServiceImpl implements AxAccountService {

  enum Group {
    SALES
  }

  @Autowired
  private AadAccountsRepository aadAccountsRepo;

  @Override
  @Transactional
  public Optional<AadAccounts> searchSaleAccount(final String email) {
    return aadAccountsRepo.findFirstByPrimaryContactEmailAndPermitGroup(email, Group.SALES.name())
        .filter(account -> email.equals(account.getPrimaryContactEmail()));
  }
}
