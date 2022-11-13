package com.sagag.services.ax.paymenttypes;

import java.util.Map;

import com.sagag.services.ax.enums.AxPaymentType;
import com.sagag.services.common.enums.PaymentMethodType;

public interface AxPaymentTypeMapper {

  Map<AxPaymentType, PaymentMethodType> getDefaultAxPaymentTypeMapper();

  Map<AxPaymentType, PaymentMethodType> getAxPaymentTypeMapperForSales();

  Map<AxPaymentType, PaymentMethodType> getAxPaymentTypeMapperForSalesWithKSLTranslator();
}
