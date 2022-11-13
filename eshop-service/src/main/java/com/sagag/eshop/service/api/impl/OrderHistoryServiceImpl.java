package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.order.OrderHistoryRepository;
import com.sagag.eshop.repo.api.order.VCustomerOrderHistoryRepository;
import com.sagag.eshop.repo.api.order.VOrderHistoryRepository;
import com.sagag.eshop.repo.criteria.OrderHistorySearchCriteria;
import com.sagag.eshop.repo.entity.order.OrderHistory;
import com.sagag.eshop.repo.entity.order.VCustomerOrderHistory;
import com.sagag.eshop.repo.entity.order.VOrderHistory;
import com.sagag.eshop.repo.specification.VOrderHistorySpecifications;
import com.sagag.eshop.service.api.OrderHistoryService;
import com.sagag.eshop.service.converter.VOrderHistoryConverters;
import com.sagag.eshop.service.dto.order.SaleOrderHistoryDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Order history service implementation class.
 */
@Service
@Slf4j
public class OrderHistoryServiceImpl implements OrderHistoryService {

  @Autowired
  private OrderHistoryRepository orderHistoryRepo;

  @Autowired
  private VOrderHistoryRepository vOrderHistoryRepo;

  @Autowired
  private VCustomerOrderHistoryRepository vCustomerOrderHistoryRepo;

  @Override
  public Optional<OrderHistory> getOrderDetail(Long orderId) {
    Assert.notNull(orderId, "The given order id must not be null");
    return orderHistoryRepo.findById(orderId);
  }

  private static Map<String, VCustomerOrderHistory> toOrderHistoryMap(
      List<VCustomerOrderHistory> orderHistories) {
    return orderHistories.stream()
        .collect(Collectors.toMap(VCustomerOrderHistory::getOrderNumber, order -> order));
  }

  @Override
  public Page<SaleOrderHistoryDto> getSaleOrders(OrderHistorySearchCriteria criteria,
      Pageable pageable) {
    Assert.notNull(criteria, "The given search criteria must not be null");

    final Specification<VOrderHistory> orderSpecs = VOrderHistorySpecifications.of(criteria);

    final long start = System.currentTimeMillis();
    final Page<VOrderHistory> orders = vOrderHistoryRepo.findAll(orderSpecs, pageable);
    log.debug("Perf:OrderHistoryServiceImpl-> getSaleOrders-> findAll  {} ms",
        System.currentTimeMillis() - start);

    return orders.map(VOrderHistoryConverters::convertToGeneralDto);
  }

  @Override
  public Map<String, VCustomerOrderHistory> getOrdersByCustomerAndDate(String customerNr,
      Date dateFrom, Date dateTo) {
    Assert.hasText(customerNr, "The given customerNr not be null or empty");
    return toOrderHistoryMap(vCustomerOrderHistoryRepo
        .findOrderByCustomerAndDate(Long.valueOf(customerNr), dateFrom, dateTo));
  }

  @Override
  public Map<String, VCustomerOrderHistory> getOrdersByUserAndDate(Long userId, Date dateFrom, Date dateTo) {
    return toOrderHistoryMap(vCustomerOrderHistoryRepo.findOrderByUserAndDate(userId, dateFrom, dateTo));
  }

  @Override
  @Transactional
  public void updateClosedDate(long orderHistoryId, Date closedDate) {
    Assert.notNull(closedDate, "The given closed date must not be null");
    orderHistoryRepo.updateClosedDateByOrderId(orderHistoryId, closedDate);
  }

  @Override
  public String findLatestOrderStateByUserId(final Long userId) {
    Assert.notNull(userId, "The given user id must not be null");
    return orderHistoryRepo.findLatestOrderStateByUserId(userId);
  }

  @Override
  public List<String> searchAvailableOrderNrs(Long userId, List<String> orderNrs) {
    if (userId == null || CollectionUtils.isEmpty(orderNrs)) {
      return Collections.emptyList();
    }
    return orderHistoryRepo.findAvailableOrderNrs(orderNrs, userId);
  }

  @Override
  public Optional<OrderHistory> searchOrderByNr(String orderNr) {
    if (StringUtils.isBlank(orderNr)) {
      return Optional.empty();
    }
    return orderHistoryRepo.findByOrderNumber(orderNr);
  }
}
