package com.sagag.services.service.order.model;

import com.sagag.eshop.repo.entity.CouponUseLog;
import com.sagag.services.domain.sag.erp.OrderWarningMessageCode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CouponUseLogModel {

  private CouponUseLog couponUseLog;

  private OrderWarningMessageCode warningMessageCode;

}
