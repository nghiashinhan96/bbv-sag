package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.criteria.finalcustomer.FinalCustomerOrderCriteria;
import com.sagag.eshop.service.api.FinalCustomerOrderService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.order.FinalCustomerOrderDto;
import com.sagag.eshop.service.dto.order.FinalCustomerOrderItemDto;
import com.sagag.eshop.service.dto.order.OrderDashboardDto;
import com.sagag.eshop.service.dto.order.VFinalCustomerOrderDto;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.service.api.FinalOrderBusinessService;
import com.sagag.services.service.exporter.wss.WssFinalCustomerOrderCsvExporter;
import com.sagag.services.service.exporter.wss.WssFinalCustomerOrderExcelExporter;
import com.sagag.services.service.exporter.wss.WssFinalCustomerOrderExportItemDto;
import com.sagag.services.service.request.FinalCustomerOrderSearchRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FinalOrderBusinessServiceImpl implements FinalOrderBusinessService {

  @Autowired
  private FinalCustomerOrderService finalCustomerOrderService;

  @Autowired
  private OrganisationRepository organisationRepo;

  @Autowired
  private WssFinalCustomerOrderCsvExporter wssFinalCustomerOrderCsvExporter;

  @Autowired
  private WssFinalCustomerOrderExcelExporter wssFinalCustomerOrderExcelExporter;

  @Override
  public OrderDashboardDto getOrderDashBoardOverview(final UserInfo user) {
    return finalCustomerOrderService.getOrderDashboardByOrgCode(user.getCustNrStr());
  }

  @Override
  public Page<VFinalCustomerOrderDto> searchFinalCustomerOrder(
      final FinalCustomerOrderSearchRequest body, final UserInfo user) {
    if (StringUtils.isNotBlank(body.getId()) && !NumberUtils.isNumber(body.getId())) {
      final String message = String.format("Invalid order id", body.getId());
      throw new IllegalArgumentException(message);
    }
    final FinalCustomerOrderCriteria criteria = body.toFinalCustomerOrderyCriteria();

    final List<Long> finalOrgIds = getFinalOrgIdsCriteriaByRole(user);
    criteria.setFinalOrgIds(finalOrgIds);

    return finalCustomerOrderService.searchVFinalCustomerOrderByCriteria(criteria);
  }

  private List<Long> getFinalOrgIdsCriteriaByRole(final UserInfo user) {
    final List<Long> finalOrgIds;
    if (user.hasWholesalerPermission()) {
      finalOrgIds = organisationRepo.findFinalOrgIdByOrgCode(user.getCustNrStr());
    } else if (user.isFinalUserRole()) {
      finalOrgIds = Arrays.asList(Long.valueOf(user.getFinalCustomerOrgId()));
    } else {
      throw new AccessDeniedException("The function supports wholesaler and final user only");
    }
    return finalOrgIds;
  }

  @Override
  public ExportStreamedResult exportFinalCustomerOrderCsv(UserInfo user, Long finalCustomerOrderId)
      throws ServiceException {
    List<WssFinalCustomerOrderExportItemDto> content =
        prepareFinalCustomerOrderExportData(user, finalCustomerOrderId);
    return wssFinalCustomerOrderCsvExporter.exportCsv(content);
  }

  @Override
  public ExportStreamedResult exportFinalCustomerOrderExcel(UserInfo user,
      Long finalCustomerOrderId) throws ServiceException {
    List<WssFinalCustomerOrderExportItemDto> content =
        prepareFinalCustomerOrderExportData(user, finalCustomerOrderId);
    return wssFinalCustomerOrderExcelExporter.exportExcel(content);
  }

  private List<WssFinalCustomerOrderExportItemDto> prepareFinalCustomerOrderExportData(
      UserInfo user, Long finalCustomerOrderId) {
    Assert.notNull(finalCustomerOrderId, "The given final customer order Id must not be null");
    Optional<FinalCustomerOrderDto> finalCustomerOrderDetailOpt =
        finalCustomerOrderService.getFinalCustomerOrderDetail(finalCustomerOrderId);

    List<FinalCustomerOrderItemDto> finalCustomerOrderItems = new ArrayList<>();
    finalCustomerOrderDetailOpt.ifPresent(
        finalCustomerOrder -> finalCustomerOrderItems.addAll(finalCustomerOrder.getItems()));

    String vatTypeDisplay = user.getSettings().getVatTypeDisplay();
    final boolean isDisplayPriceWithVat = StringUtils.startsWithIgnoreCase(vatTypeDisplay, "1");
    List<WssFinalCustomerOrderExportItemDto> content = finalCustomerOrderItems.stream()
        .map(orderItem -> new WssFinalCustomerOrderExportItemDto(orderItem, isDisplayPriceWithVat,
            user.getUserLocale()))
        .collect(Collectors.toList());
    return content;
  }

}
