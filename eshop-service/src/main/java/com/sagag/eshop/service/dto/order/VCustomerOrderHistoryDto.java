package com.sagag.eshop.service.dto.order;

import com.sagag.eshop.repo.entity.order.VCustomerOrderHistory;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.sag.erp.ExternalOrderDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VCustomerOrderHistoryDto implements Serializable{

  private static final long serialVersionUID = -4900341509476481510L;

  public static final String AGGREGRATION_ALL = "ALL";

  private Long id;

  private String nr;

  private Long customerNr;

  private String customerName;

  private String saleName;

  private String typeCode;

  private String type;

  private String typeDesc;

  private String sendMethodCode;

  private String sendMethod;

  private String sendMethodDesc;

  private String statusCode;

  private String status;

  private String statusDesc;

  private Boolean partialDelivery;

  private Boolean partialInvoice;

  private String date;

  private String username;

  private String invoiceTypeCode;

  private String reference;

  private String branchRemark;

  private Double totalPrice;

  private String goodsReceiverName;

  private List<String> vehicleInfos;

  public static Optional<VCustomerOrderHistoryDto> createFitleringOrderDetailInfo(
      final ExternalOrderDetail externalOrder, final VCustomerOrderHistory orderHistory,
      OrderHistoryFilterDto condition, final String custName,
      final String custNr, final String username) {
    if (Objects.isNull(externalOrder) && Objects.isNull(orderHistory)) {
      return Optional.empty();
    }
    if(isNotMatchedCondition(externalOrder, orderHistory, condition, username)) {
      return Optional.empty();
    }
    VCustomerOrderHistoryDtoBuilder orderDetailDtoBuilder = VCustomerOrderHistoryDto.builder();
    if (!Objects.isNull(externalOrder)) {
      orderDetailDtoBuilder
      .nr(externalOrder.getNr())
      .typeCode(externalOrder.getTypeCode())
      .sendMethodCode(externalOrder.getSendMethodCode())
      .date(externalOrder.getDate())
      .statusCode(externalOrder.getStatus())
      .status(externalOrder.getStatus());
    }

    if (!Objects.isNull(orderHistory)) {
      List<String> vehicleInfos = new ArrayList<>();
      if (StringUtils.isNotBlank(orderHistory.getVehicleInfos())) {
        vehicleInfos = Arrays.asList(orderHistory.getVehicleInfos().split(SagConstants.SEMICOLON))
            .stream().map(String::trim).distinct().collect(Collectors.toList());
      }
      orderDetailDtoBuilder
      .id(orderHistory.getId())
      .nr(orderHistory.getOrderNumber())
      .date(DateUtils.getUTCDateStrWithTimeZone(orderHistory.getCreatedDate()))
      .vehicleInfos(vehicleInfos)
      .username(username)
      .customerNr(Long.valueOf(custNr))
      .customerName(custName);
    }

    return Optional.of(orderDetailDtoBuilder.build());
  }

  private static boolean isNotMatchedCondition(final ExternalOrderDetail externalOrder,
      final VCustomerOrderHistory orderHistory, OrderHistoryFilterDto condition,
      final String username) {
    String statusCode =
        Objects.isNull(externalOrder) ? StringUtils.EMPTY : externalOrder.getStatus();
    boolean isSearchByArticleNumberMatched = StringUtils.isBlank(condition.getArticleNumber())
        || (StringUtils.isNoneBlank(condition.getArticleNumber())
            && !Objects.isNull(externalOrder));
    return (!isFilterMatched(condition.getStatus(), statusCode)
        || !isFilterMatched(condition.getUsername(), username)
        || !isSearchByArticleNumberMatched);

  }

  private static boolean isFilterMatched(String filteredText, String text) {
    if (StringUtils.isBlank(filteredText)) {
      return false;
    }
    if (filteredText.equalsIgnoreCase(AGGREGRATION_ALL)) {
      return true;
    }
    return filteredText.equalsIgnoreCase(text);
  }

}
