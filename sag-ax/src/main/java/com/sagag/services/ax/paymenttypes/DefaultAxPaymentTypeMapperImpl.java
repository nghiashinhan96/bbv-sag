package com.sagag.services.ax.paymenttypes;

import java.util.EnumMap;
import java.util.Map;

import com.sagag.services.ax.enums.AxPaymentType;
import com.sagag.services.common.enums.PaymentMethodType;

public class DefaultAxPaymentTypeMapperImpl implements AxPaymentTypeMapper {

  @Override
  public Map<AxPaymentType, PaymentMethodType> getDefaultAxPaymentTypeMapper() {
    final Map<AxPaymentType, PaymentMethodType> axPaymentTypes = new EnumMap<>(AxPaymentType.class);
      axPaymentTypes.put(AxPaymentType.FIN, PaymentMethodType.CREDIT);
      axPaymentTypes.put(AxPaymentType.KEINE, PaymentMethodType.CREDIT);
      axPaymentTypes.put(AxPaymentType.SELBSTZAHL, PaymentMethodType.CREDIT);
      axPaymentTypes.put(AxPaymentType.SEPA_B_FIN, PaymentMethodType.CREDIT);
      axPaymentTypes.put(AxPaymentType.SEPA_B2B, PaymentMethodType.CREDIT);
      axPaymentTypes.put(AxPaymentType.SEPA_C_FIN, PaymentMethodType.CREDIT);
      axPaymentTypes.put(AxPaymentType.SEPA_CORE, PaymentMethodType.CREDIT);
      axPaymentTypes.put(AxPaymentType.SELF_PAY_P, PaymentMethodType.CREDIT);
      axPaymentTypes.put(AxPaymentType.SELF_PAY_CZK, PaymentMethodType.CREDIT);
      axPaymentTypes.put(AxPaymentType.SELF_PAY_EUR, PaymentMethodType.CREDIT);
      axPaymentTypes.put(AxPaymentType.IMMEDIATE, PaymentMethodType.CREDIT);
      axPaymentTypes.put(AxPaymentType.SELF_PAY_O, PaymentMethodType.CREDIT);
      axPaymentTypes.put(AxPaymentType.SELF_PAY_Z, PaymentMethodType.CREDIT);
      axPaymentTypes.put(AxPaymentType.SELF_PAY_C, PaymentMethodType.CREDIT);

      axPaymentTypes.put(AxPaymentType.BAR, PaymentMethodType.CASH);
      axPaymentTypes.put(AxPaymentType.BETREIBUNG, PaymentMethodType.CASH);
      axPaymentTypes.put(AxPaymentType.KARTE, PaymentMethodType.CASH);
      axPaymentTypes.put(AxPaymentType.SAK, PaymentMethodType.CASH);
      axPaymentTypes.put(AxPaymentType.SEPA_B_SAK, PaymentMethodType.CASH);
      axPaymentTypes.put(AxPaymentType.SEPA_C_SAK, PaymentMethodType.CASH);
      axPaymentTypes.put(AxPaymentType.PETTY_CASH, PaymentMethodType.CASH);
      axPaymentTypes.put(AxPaymentType.SOFORT, PaymentMethodType.CASH);
      return axPaymentTypes;
  }

  @Override
  public Map<AxPaymentType, PaymentMethodType> getAxPaymentTypeMapperForSales() {
    final Map<AxPaymentType, PaymentMethodType> axPaymentTypeMapForSales = new EnumMap<>(AxPaymentType.class);
    axPaymentTypeMapForSales.put(AxPaymentType.FIN, PaymentMethodType.CREDIT);
    axPaymentTypeMapForSales.put(AxPaymentType.KEINE, PaymentMethodType.CREDIT);
    axPaymentTypeMapForSales.put(AxPaymentType.SELBSTZAHL, PaymentMethodType.CREDIT);
    axPaymentTypeMapForSales.put(AxPaymentType.SEPA_B_FIN, PaymentMethodType.CREDIT);
    axPaymentTypeMapForSales.put(AxPaymentType.SEPA_B2B, PaymentMethodType.CREDIT);
    axPaymentTypeMapForSales.put(AxPaymentType.SEPA_C_FIN, PaymentMethodType.CREDIT);
    axPaymentTypeMapForSales.put(AxPaymentType.SEPA_CORE, PaymentMethodType.CREDIT);
    axPaymentTypeMapForSales.put(AxPaymentType.SELF_PAY_P, PaymentMethodType.CREDIT);
    axPaymentTypeMapForSales.put(AxPaymentType.SELF_PAY_CZK, PaymentMethodType.CREDIT);
    axPaymentTypeMapForSales.put(AxPaymentType.SELF_PAY_EUR, PaymentMethodType.CREDIT);
    axPaymentTypeMapForSales.put(AxPaymentType.IMMEDIATE, PaymentMethodType.CREDIT);
    axPaymentTypeMapForSales.put(AxPaymentType.SELF_PAY_O, PaymentMethodType.CREDIT);
    axPaymentTypeMapForSales.put(AxPaymentType.SELF_PAY_Z, PaymentMethodType.CREDIT);
    axPaymentTypeMapForSales.put(AxPaymentType.SELF_PAY_C, PaymentMethodType.CREDIT);
    axPaymentTypeMapForSales.put(AxPaymentType.IMMEDIATE, PaymentMethodType.CREDIT);

    axPaymentTypeMapForSales.put(AxPaymentType.BAR, PaymentMethodType.CASH);
    axPaymentTypeMapForSales.put(AxPaymentType.BETREIBUNG, PaymentMethodType.CASH);
    axPaymentTypeMapForSales.put(AxPaymentType.KARTE, PaymentMethodType.CASH);
    axPaymentTypeMapForSales.put(AxPaymentType.SAK, PaymentMethodType.CASH);
    axPaymentTypeMapForSales.put(AxPaymentType.SEPA_B_SAK, PaymentMethodType.CASH);
    axPaymentTypeMapForSales.put(AxPaymentType.SEPA_C_SAK, PaymentMethodType.CASH);
    axPaymentTypeMapForSales.put(AxPaymentType.PETTY_CASH, PaymentMethodType.CASH);

    axPaymentTypeMapForSales.put(AxPaymentType.SOFORT, PaymentMethodType.DIRECT_INVOICE);
    return axPaymentTypeMapForSales;
  }

  @Override
  public Map<AxPaymentType, PaymentMethodType> getAxPaymentTypeMapperForSalesWithKSLTranslator() {
    final Map<AxPaymentType, PaymentMethodType> axPaymentTypeMapForSalesWithKSL = new EnumMap<>(AxPaymentType.class);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.FIN, PaymentMethodType.CREDIT);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.KEINE, PaymentMethodType.CREDIT);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.SELBSTZAHL, PaymentMethodType.CREDIT);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.SEPA_B_FIN, PaymentMethodType.CREDIT);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.SEPA_B2B, PaymentMethodType.CREDIT);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.SEPA_C_FIN, PaymentMethodType.CREDIT);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.SEPA_CORE, PaymentMethodType.CREDIT);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.SELF_PAY_P, PaymentMethodType.CREDIT);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.SELF_PAY_CZK, PaymentMethodType.CREDIT);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.SELF_PAY_EUR, PaymentMethodType.CREDIT);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.SELF_PAY_O, PaymentMethodType.CREDIT);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.SELF_PAY_Z, PaymentMethodType.CREDIT);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.SELF_PAY_C, PaymentMethodType.CREDIT);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.IMMEDIATE, PaymentMethodType.CREDIT);

      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.BAR, PaymentMethodType.DIRECT_INVOICE);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.BETREIBUNG, PaymentMethodType.DIRECT_INVOICE);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.KARTE, PaymentMethodType.DIRECT_INVOICE);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.SAK, PaymentMethodType.DIRECT_INVOICE);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.SEPA_B_SAK, PaymentMethodType.DIRECT_INVOICE);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.SEPA_C_SAK, PaymentMethodType.DIRECT_INVOICE);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.PETTY_CASH, PaymentMethodType.DIRECT_INVOICE);
      axPaymentTypeMapForSalesWithKSL.put(AxPaymentType.SOFORT, PaymentMethodType.DIRECT_INVOICE);
      return axPaymentTypeMapForSalesWithKSL;
  }

}
