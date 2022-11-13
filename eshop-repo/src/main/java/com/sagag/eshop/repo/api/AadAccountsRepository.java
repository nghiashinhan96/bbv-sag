package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.AadAccounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Repository interfacing for {@link AadAccounts}.
 */
public interface AadAccountsRepository
    extends JpaRepository<AadAccounts, Integer>, JpaSpecificationExecutor<AadAccounts> {

  /**
   * Returns the first account with the email and the belonged group.
   *
   * @param primaryContactEmail the unique email identified the account.
   * @param permitGroup the permit group to which the account belongs
   * @return the {@link AadAccounts}, nullable
   */
  Optional<AadAccounts> findFirstByPrimaryContactEmailAndPermitGroup(
      final String primaryContactEmail, final String permitGroup);

  Optional<AadAccounts> findByUuid(String uuid);

  AadAccounts findByUserId(Long userId);
}
