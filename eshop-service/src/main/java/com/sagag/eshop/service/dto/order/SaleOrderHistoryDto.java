package com.sagag.eshop.service.dto.order;

import com.sagag.eshop.repo.entity.order.VOrderHistory;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.order.OrderType;
import com.sagag.services.common.utils.DateUtils;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Sale order history search Dto class.
 */
@Data
public class SaleOrderHistoryDto implements Serializable {

  private static final long serialVersionUID = 7690931548195470097L;

  private final Long id;
  private final String nr;
  private final String customerNr;
  private final String customerName;
  private final Date createdDate;
  private final float totalPrice;
  private final Date closedDate;
  private final OrderType type;
  private final String axProcessStatus;
  private final List<String> workIds;
  private final String axOrderURL;

  /**
   * Returns the displayed creation date.
   *
   * @return the <code>String</code> date
   */
  public String getCreatedDateDisp() {
    if (Objects.isNull(createdDate)) {
      return StringUtils.EMPTY;
    }
    return DateUtils.toStringDate(createdDate, DateUtils.SWISS_DATE_PATTERN_1);
  }

  /**
   * Returns the displayed creation date.
   *
   * @return the <code>String</code> date
   */
  public String getClosedDateDisp() {
    if (Objects.isNull(closedDate)) {
      return null;
    }
    return DateUtils.toStringDate(closedDate, DateUtils.SWISS_DATE_PATTERN_1);
  }

  public SaleOrderHistoryDto(VOrderHistory vOrderHistory) {
    this.id = vOrderHistory.getId();
    this.nr = vOrderHistory.getOrderNumber();
    this.customerNr = vOrderHistory.getCustomerNumber();
    this.customerName = vOrderHistory.getCustomerName();
    this.createdDate = vOrderHistory.getCreatedDate();
    this.closedDate = vOrderHistory.getClosedDate();
    this.totalPrice = vOrderHistory.getTotalPrice();
    this.type = OrderType.getNullSafeType(vOrderHistory.getType());
    this.axProcessStatus = vOrderHistory.getAxProcessStatus();
    this.workIds = !StringUtils.isBlank(vOrderHistory.getWorkIds())
        ? Stream.of(vOrderHistory.getWorkIds().split(SagConstants.COMMA_NO_SPACE)).collect(
            Collectors.toList())
        : Collections.emptyList();
    this.axOrderURL = vOrderHistory.getAxOrderUrl();
  }

}
