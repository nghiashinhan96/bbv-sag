package com.sagag.eshop.service.api;

import com.sagag.eshop.service.dto.BranchDetailDto;
import com.sagag.eshop.service.dto.BranchDto;
import com.sagag.eshop.service.exception.BranchValidationException;
import com.sagag.services.domain.eshop.branch.dto.BranchRequestBody;
import com.sagag.services.domain.eshop.branch.dto.BranchSearchRequestCriteria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interface to define services for customer branch.
 */
public interface BranchService {

  /**
   * Creates new branch.
   *
   * @param request body request to create new branch
   *
   * @return {@link BranchDetailDto}
   * @throws BranchValidationException
   */
  BranchDetailDto create(BranchRequestBody request) throws BranchValidationException;

  /**
   * Updates existing branch.
   *
   * @param request body request to update existing branch
   *
   * @return {@link BranchDetailDto}
   * @throws BranchValidationException
   */
  BranchDetailDto update(BranchRequestBody request) throws BranchValidationException;

  /**
   * Removes existing branch.
   *
   * @param branchNr branch number
   * @throws BranchValidationException
   *
   */
  void remove(Integer branchNr) throws BranchValidationException;

  /**
   * Searching existing branch base on criteria.
   *
   * @param criteria search conditions
   * @param pageable
   * @return {@link Page<BranchDto>}
   */
  Page<BranchDto> searchBranchByCriteria(BranchSearchRequestCriteria criteria, Pageable pageable);

  /**
   * Gets existing branch by branch number.
   *
   * @param branchNr branch number
   *
   * @return the optional of {@link BranchDetailDto}
   * @throws BranchValidationException
   */
  Optional<BranchDetailDto> getBranchDetail(Integer branchNr) throws BranchValidationException;

  /**
   * Gets all branches
   *
   * @return the list of {@link BranchDto}
   */
  List<BranchDto> getBranches();

  /**
   * Gets all branches by country code
   *
   * @param countryShortCode
   * @return the list of {@link BranchDto}
   * @throws BranchValidationException
   */
  List<BranchDto> getBranchesByCountry(String countryShortCode) throws BranchValidationException;
}
