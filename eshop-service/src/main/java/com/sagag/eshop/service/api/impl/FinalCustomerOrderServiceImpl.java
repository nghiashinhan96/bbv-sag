package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.order.FinalCustomerOrderItemRepository;
import com.sagag.eshop.repo.api.order.FinalCustomerOrderRepository;
import com.sagag.eshop.repo.api.order.VFinalCustomerOrderRepository;
import com.sagag.eshop.repo.api.order.VFinalCustomerOrderStatusRepository;
import com.sagag.eshop.repo.criteria.finalcustomer.FinalCustomerOrderCriteria;
import com.sagag.eshop.repo.entity.order.FinalCustomerOrder;
import com.sagag.eshop.repo.entity.order.VFinalCustomerOrder;
import com.sagag.eshop.repo.entity.order.VFinalCustomerOrderStatus;
import com.sagag.eshop.repo.specification.VFinalCustomerOrderSpecification;
import com.sagag.eshop.service.api.FinalCustomerOrderService;
import com.sagag.eshop.service.converter.VFinalCustomerOrderConverters;
import com.sagag.eshop.service.dto.order.FinalCustomerOrderDto;
import com.sagag.eshop.service.dto.order.FinalCustomerOrderItemDto;
import com.sagag.eshop.service.dto.order.FinalCustomerOrderReferenceDto;
import com.sagag.eshop.service.dto.order.OrderDashboardDto;
import com.sagag.eshop.service.dto.order.VFinalCustomerOrderDto;
import com.sagag.services.common.enums.FinalCustomerOrderStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FinalCustomerOrderServiceImpl implements FinalCustomerOrderService {

  @Autowired
  private VFinalCustomerOrderStatusRepository vFinalCustomerOrderStatusRepo;

  @Autowired
  private VFinalCustomerOrderRepository vFinalCustomerOrderRepo;

  @Autowired
  private FinalCustomerOrderRepository finalCustomerOrderRepo;

  @Autowired
  private FinalCustomerOrderItemRepository finalCustomerOrderItemRepo;

  @Autowired
  private FinalCustomerOrderRepository finalCustomerOrderRepository;

  @Override
  public Page<VFinalCustomerOrderDto> searchVFinalCustomerOrderByCriteria(
      final FinalCustomerOrderCriteria criteria) {
    Assert.notNull(criteria, "The given criteria must not be null");
    Pageable pageRequest = PageRequest.of(criteria.getOffset(), criteria.getPageSize());
    Page<VFinalCustomerOrder> page =
        vFinalCustomerOrderRepo.findAll(VFinalCustomerOrderSpecification.of(criteria), pageRequest);

    return page.map(VFinalCustomerOrderConverters.converter());
  }

  private static void assertOrgCodeNotNull(final String orgCode) {
    Assert.hasText(orgCode, "orgCode is required!");
  }

  @Override
  public OrderDashboardDto getOrderDashboardByOrgCode(final String orgCode) {
    assertOrgCodeNotNull(orgCode);
    List<VFinalCustomerOrderStatus> orderStatuses =
        vFinalCustomerOrderStatusRepo.findOrderStatusByOrgCode(Long.valueOf(orgCode));
    return OrderDashboardDto.builder()
        .newOrderQuantity(orderStatuses.stream().mapToInt(VFinalCustomerOrderStatus::getNewOrder)
            .sum())
        .openOrderQuantity(orderStatuses.stream().mapToInt(VFinalCustomerOrderStatus::getOpenOrder)
            .sum())
        .orderedQuantity(orderStatuses.stream().mapToInt(VFinalCustomerOrderStatus::getPlacedOrder)
            .sum()).build();
  }

  @Override
  public Optional<FinalCustomerOrderDto> getFinalCustomerOrderDetail(Long finalCustomerOrderId) {
    if (finalCustomerOrderId == null) {
      return Optional.empty();
    }

    Optional<FinalCustomerOrderDto> finalCustomerOrderOpt =
        finalCustomerOrderRepo.findById(finalCustomerOrderId).map(FinalCustomerOrderDto::new);
    finalCustomerOrderOpt
        .ifPresent(finalCustOrder -> finalCustOrder.setItems(finalCustomerOrderItemRepo
            .findByFinalCustomerOrderIds(Arrays.asList(finalCustOrder.getId()))));
    return finalCustomerOrderOpt;
  }

  @Override
  public List<FinalCustomerOrderItemDto> getFinalCustomerOrderItems(Long finalCustomerOrderId) {
    return finalCustomerOrderItemRepo.findByFinalCustomerOrderId(finalCustomerOrderId).stream()
        .map(FinalCustomerOrderItemDto::new).collect(Collectors.toList());
  }

  @Override
  public void save(final FinalCustomerOrder finalCustomerOrder) {
    FinalCustomerOrder savedOrder = finalCustomerOrderRepo.save(finalCustomerOrder);
    finalCustomerOrder.getItems().forEach(i -> i.setFinalCustomerOrderId(savedOrder.getId()));
    finalCustomerOrderItemRepo.saveAll(finalCustomerOrder.getItems());
  }

  private void manageChangeOrderStatus(Long orderId, String oldStatus, String newStatus) {
    finalCustomerOrderRepository.findById(orderId)
        .filter(order -> oldStatus.equalsIgnoreCase(order.getStatus()))
        .ifPresent(updateOrder -> {
          updateOrder.setStatus(newStatus);
          finalCustomerOrderRepository.save(updateOrder);
        });
  }

  @Override
  public void deleteOrder(Long orderId) {
    manageChangeOrderStatus(orderId, FinalCustomerOrderStatus.OPEN.name(),
        FinalCustomerOrderStatus.DELETED.name());
  }

  @Override
  public void changeOrderStatusToOpen(Long orderId) {
    manageChangeOrderStatus(orderId, FinalCustomerOrderStatus.NEW.name(),
        FinalCustomerOrderStatus.OPEN.name());

  }

  @Override
  public void changeOrderStatusToOrdered(Long orderId) {
    manageChangeOrderStatus(orderId, FinalCustomerOrderStatus.OPEN.name(),
        FinalCustomerOrderStatus.ORDERED.name());
  }

  @Override
  public Optional<FinalCustomerOrderReferenceDto> getFinalCustomerOrderReference(
      Long finalCustomerOrderId) {
    if (finalCustomerOrderId == null) {
      return Optional.empty();
    }

    Optional<FinalCustomerOrderReferenceDto> finalCustomerOrderOpt =
        finalCustomerOrderRepo.findById(finalCustomerOrderId).map(FinalCustomerOrderReferenceDto::new);
    finalCustomerOrderOpt
        .ifPresent(finalCustOrder -> finalCustOrder.setItems(finalCustomerOrderItemRepo
            .findByFinalCustomerOrderIds(Arrays.asList(finalCustOrder.getId()))));
    return finalCustomerOrderOpt;
  }

}
