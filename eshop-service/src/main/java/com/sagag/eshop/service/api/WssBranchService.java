package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.WssBranch;
import com.sagag.eshop.service.exception.WssBranchValidationException;
import com.sagag.services.domain.eshop.branch.dto.WssBranchRequestBody;
import com.sagag.services.domain.eshop.branch.dto.WssBranchSearchRequestCriteria;
import com.sagag.services.domain.eshop.dto.WssBranchDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interface to define services for customer WSS branch.
 */
public interface WssBranchService {

  /**
   * Searches WSS branch by branch number.
   *
   * @param branchNr branch number
   * @param orgId
   *
   * @return {@link WssBranch}
   */
  Optional<WssBranch> searchByBranchNr(Integer branchNr, Integer orgId);

  /**
   * Creates new WSS branch.
   *
   * @param request body request to create new WSS branch
   * @param orgId wholesaler organization id
   *
   * @return {@link WssBranchDto}
   * @throws WssBranchValidationException
   */
  WssBranchDto create(WssBranchRequestBody request, int orgId) throws WssBranchValidationException;

  /**
   * Updates existing WSS branch.
   *
   * @param request body request to update existing WSS branch
   * @param orgId wholesaler organization id
   *
   * @return {@link WssBranchDto}
   * @throws WssBranchValidationException
   */
  WssBranchDto update(WssBranchRequestBody request, int orgId) throws WssBranchValidationException;

  /**
   * Removes existing WSS branch.
   *
   * @param branchNr WSS branch number
   * @param orgId wholesaler organization id
   *
   * @throws WssBranchValidationException
   *
   */
  void remove(Integer branchNr, Integer orgId) throws WssBranchValidationException;

  /**
   * Searching existing WSS branch base on criteria.
   *
   * @param criteria search conditions
   * @param pageable
   * @return {@link Page<WssBranchDto>}
   */
  Page<WssBranchDto> searchBranchByCriteria(WssBranchSearchRequestCriteria criteria, Pageable pageable);

  /**
   * Gets existing branch by WSS branch number.
   *
   * @param branchNr WSS branch number
   * @param orgId WSS branch owner orgId
   *
   * @return the optional of {@link WssBranchDto}
   * @throws WssBranchValidationException
   */
  Optional<WssBranchDto> getBranchDetail(Integer branchNr, Integer orgId)
      throws WssBranchValidationException;

  /**
   * Gets all WSS branches
   * @param orgId
   *
   * @return the list of {@link WssBranchDto}
   */
  List<WssBranchDto> getBranchesByOrganisation(Integer orgId);

}
