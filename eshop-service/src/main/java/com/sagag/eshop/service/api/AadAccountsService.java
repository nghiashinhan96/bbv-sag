package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.AadAccounts;
import com.sagag.eshop.service.exception.DuplicatedEmailException;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.domain.eshop.criteria.AadAccountsSearchRequestCriteria;
import com.sagag.services.domain.eshop.dto.AadAccountsDto;
import com.sagag.services.domain.eshop.dto.AadAccountsSearchResultDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AadAccountsService {

  /**
   * Creates new aad account.
   *
   * @param dto
   * @throws DuplicatedEmailException throw this exception if email is duplicated
   * @throws ValidationException
   */
  void create(AadAccountsDto dto) throws DuplicatedEmailException, ValidationException;

  /**
   * Updates existing aad account.
   *
   * @param dto
   * @param id
   * @throws DuplicatedEmailException throw this exception if email is duplicated
   * @throws ValidationException
   */
  void update(AadAccountsDto dto, int id) throws DuplicatedEmailException, ValidationException;

  /**
   * Returns found add accounts.
   *
   * @param requestCriteria
   * @param pageable
   * @return found add accounts.
   */
  Page<AadAccountsSearchResultDto> search(AadAccountsSearchRequestCriteria requestCriteria,
      Pageable pageable);

  /**
   * Returns found aad account.
   *
   * @param id
   * @return found aad account.
   */
  AadAccountsSearchResultDto findById(Integer id);

  /**
   * Updates email.
   * @param aadAccounts
   * @param email
   */
  void updateEmail(AadAccounts aadAccounts, String email);

  /**
   * Returns found aad account.
   *
   * @param userId
   * @return found aad account.
   */
  Optional<AadAccountsSearchResultDto> findByUserId(Long userId);
}
