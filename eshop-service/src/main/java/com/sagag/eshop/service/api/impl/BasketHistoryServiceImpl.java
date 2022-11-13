package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.BasketHistoryRepository;
import com.sagag.eshop.repo.api.VBasketHistoryRepository;
import com.sagag.eshop.repo.api.VUserDetailRepository;
import com.sagag.eshop.repo.criteria.BasketHistoryCriteria;
import com.sagag.eshop.repo.entity.BasketHistory;
import com.sagag.eshop.repo.entity.VBasketHistory;
import com.sagag.eshop.repo.specification.BasketHistorySpecification;
import com.sagag.eshop.service.api.BasketHistoryService;
import com.sagag.eshop.service.converter.BasketHistoryConverters;
import com.sagag.eshop.service.dto.BasketHistoryDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Implementation class of basket history.
 */
@Service
@Slf4j
@Transactional
public class BasketHistoryServiceImpl implements BasketHistoryService {

  @Autowired
  private BasketHistoryRepository basketHistoryRepo;

  @Autowired
  private VUserDetailRepository vUserDetailRepo;

  @Autowired
  private VBasketHistoryRepository vBasketHistoryRepo;

  @Override
  public void createBasketHistory(final BasketHistoryDto basketHistory) {

    Assert.notNull(basketHistory, "The given basket history object must not be null");
    Assert.notNull(basketHistory.getCreatedUserId(), "The given created user id must not be null");
    Assert.notEmpty(basketHistory.getItems(),
        "Can not create basket history with the empty list of items");
    Assert.notNull(basketHistory.getGrandTotalExcludeVat(),
        "The given gross price exclude VAT must not be null");

    final Integer orgId =
        vUserDetailRepo.findOrgIdByUserId(basketHistory.getCreatedUserId()).orElseThrow(
            () -> new NoSuchElementException("Not found customer info"));

    basketHistoryRepo.save(basketHistory.toEntity(orgId, true));
  }

  @Override
  public Page<BasketHistoryDto> searchBasketHistoriesByCriteria(final BasketHistoryCriteria criteria,
      final boolean salesMode) {
    Assert.notNull(criteria, "The given criteria must not be null");
    Pageable pageRequest = PageRequest.of(criteria.getOffset(), criteria.getPageSize());
    Page<VBasketHistory> page =
        vBasketHistoryRepo.findAll(BasketHistorySpecification.of(criteria, salesMode), pageRequest);
    return page.map(BasketHistoryConverters.ignoreItemBasketHistoryConverter());
  }

  @Override
  public long countBasketHistoriesByCustomer(String customerNr) {
    if (StringUtils.isBlank(customerNr)) {
      return NumberUtils.LONG_ZERO;
    }
    return vBasketHistoryRepo.countByOrganisationOrgCode(customerNr);
  }

  @Override
  public long countBasketHistoriesByUser(final Long userId) {
    Assert.notNull(userId, "User id must not be empty!");
    return vBasketHistoryRepo.countByCreatedUserId(userId);
  }

  @Override
  public long countSalesBasketHistories(Long salesUserId) {
    return vBasketHistoryRepo.countBySalesUserId(salesUserId);
  }

  @Override
  public Optional<BasketHistoryDto> getBasketHistoryDetails(final Long basketId) {
    return vBasketHistoryRepo.findByBasketId(basketId)
        .map(BasketHistoryConverters.optionalBasketHistoryConverter());
  }

  @Override
  public void delete(final Long basketId, final Long saleId, final Integer organisationId,
      final boolean isAdmin, final Long userId) {
    log.debug("Delete basket history by basket id {}", basketId);
    final BasketHistory basketHistory = basketHistoryRepo.findById(basketId)
        .orElseThrow(() -> new IllegalArgumentException("There is no such a basket to delete!"));

    if (!validateBasketDelete(basketHistory, saleId, organisationId, isAdmin, userId)) {
      throw new IllegalArgumentException(
          "Please check if you have permission to delete the basket");
    }

    basketHistory.setActive(false);
    basketHistoryRepo.save(basketHistory);
  }

  private boolean validateBasketDelete(final BasketHistory basketHistory, final Long saleId,
      final Integer organisationId, final boolean isAdmin, final Long userId) {

    if (basketHistory.getSalesUserId() != null) {
      return basketHistory.getSalesUserId().equals(saleId);
    }

    if (saleId != null) {
      return true;
    }

    if (isAdmin) {
      return organisationId.equals(basketHistory.getOrganisationId());
    }

    return userId.equals(basketHistory.getCreatedUserId());
  }

}
