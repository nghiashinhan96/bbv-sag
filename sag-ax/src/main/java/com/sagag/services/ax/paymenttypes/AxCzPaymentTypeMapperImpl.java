package com.sagag.services.ax.paymenttypes;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.sagag.services.ax.enums.AxPaymentType;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.profiles.AxCzProfile;

@Component
@AxCzProfile
public class AxCzPaymentTypeMapperImpl implements AxPaymentTypeMapper {

  @Override
  public Map<AxPaymentType, PaymentMethodType> getDefaultAxPaymentTypeMapper() {
    final Map<AxPaymentType, PaymentMethodType> axPaymentTypes = new EnumMap<>(AxPaymentType.class);
    final AxPaymentType[] axPaymentTypesForCredit = { AxPaymentType.FIN,
        AxPaymentType.KEINE,
        AxPaymentType.SELBSTZAHL,
        AxPaymentType.SEPA_B_FIN,
        AxPaymentType.SEPA_B2B,
        AxPaymentType.SEPA_C_FIN,
        AxPaymentType.SEPA_CORE,
        AxPaymentType.SELF_PAY_P,
        AxPaymentType.SELF_PAY_CZK,
        AxPaymentType.SELF_PAY_EUR,
        AxPaymentType.SELF_PAY_O,
        AxPaymentType.SELF_PAY_C,
        AxPaymentType.SELF_PAY_Z };

    Stream.of(axPaymentTypesForCredit)
    .forEach(paymentType -> axPaymentTypes.putIfAbsent(paymentType, PaymentMethodType.CREDIT));

    final AxPaymentType[] axPaymentTypesForCash = { AxPaymentType.BAR,
        AxPaymentType.BETREIBUNG,
        AxPaymentType.KARTE,
        AxPaymentType.SAK,
        AxPaymentType.SEPA_B_SAK,
        AxPaymentType.SEPA_C_SAK,
        AxPaymentType.PETTY_CASH,
        AxPaymentType.SOFORT,
        AxPaymentType.IMMEDIATE };

    Stream.of(axPaymentTypesForCash)
    .forEach(paymentType -> axPaymentTypes.putIfAbsent(paymentType, PaymentMethodType.CASH));

    return axPaymentTypes;
  }

  @Override
  public Map<AxPaymentType, PaymentMethodType> getAxPaymentTypeMapperForSales() {

    final Map<AxPaymentType, PaymentMethodType> axPaymentTypes = new EnumMap<>(AxPaymentType.class);
    final AxPaymentType[] axPaymentTypesForCredit = { AxPaymentType.FIN,
        AxPaymentType.KEINE,
        AxPaymentType.SELBSTZAHL,
        AxPaymentType.SEPA_B_FIN,
        AxPaymentType.SEPA_B2B,
        AxPaymentType.SEPA_C_FIN,
        AxPaymentType.SEPA_CORE,
        AxPaymentType.SELF_PAY_P,
        AxPaymentType.SELF_PAY_CZK,
        AxPaymentType.SELF_PAY_EUR,
        AxPaymentType.SELF_PAY_O,
        AxPaymentType.SELF_PAY_C,
        AxPaymentType.SELF_PAY_Z };

    Stream.of(axPaymentTypesForCredit)
    .forEach(paymentType -> axPaymentTypes.putIfAbsent(paymentType, PaymentMethodType.CREDIT));

    final AxPaymentType[] axPaymentTypesForCash = { AxPaymentType.BAR,
        AxPaymentType.BETREIBUNG,
        AxPaymentType.KARTE,
        AxPaymentType.SAK,
        AxPaymentType.SEPA_B_SAK,
        AxPaymentType.SEPA_C_SAK,
        AxPaymentType.PETTY_CASH };

    Stream.of(axPaymentTypesForCash)
    .forEach(paymentType -> axPaymentTypes.putIfAbsent(paymentType, PaymentMethodType.CASH));

    final AxPaymentType[] axPaymentTypesForDirectInvoice = {
        AxPaymentType.IMMEDIATE,
        AxPaymentType.SOFORT };

    Stream.of(axPaymentTypesForDirectInvoice)
    .forEach(paymentType -> axPaymentTypes.putIfAbsent(paymentType,
        PaymentMethodType.DIRECT_INVOICE));

    return axPaymentTypes;
  }

  @Override
  public Map<AxPaymentType, PaymentMethodType> getAxPaymentTypeMapperForSalesWithKSLTranslator() {
    final Map<AxPaymentType, PaymentMethodType> axPaymentTypes = new EnumMap<>(AxPaymentType.class);
    final AxPaymentType[] axPaymentTypesForCredit = { AxPaymentType.FIN,
        AxPaymentType.KEINE,
        AxPaymentType.SELBSTZAHL,
        AxPaymentType.SEPA_B_FIN,
        AxPaymentType.SEPA_B2B,
        AxPaymentType.SEPA_C_FIN,
        AxPaymentType.SEPA_CORE,
        AxPaymentType.SELF_PAY_P,
        AxPaymentType.SELF_PAY_CZK,
        AxPaymentType.SELF_PAY_EUR,
        AxPaymentType.SELF_PAY_O,
        AxPaymentType.SELF_PAY_C,
        AxPaymentType.SELF_PAY_Z };

    Stream.of(axPaymentTypesForCredit)
    .forEach(paymentType -> axPaymentTypes.putIfAbsent(paymentType, PaymentMethodType.CREDIT));

    final AxPaymentType[] axPaymentTypesForDirectInvoice = { AxPaymentType.BAR,
        AxPaymentType.BETREIBUNG,
        AxPaymentType.KARTE,
        AxPaymentType.SAK,
        AxPaymentType.SEPA_B_SAK,
        AxPaymentType.SEPA_C_SAK,
        AxPaymentType.PETTY_CASH,
        AxPaymentType.SOFORT,
        AxPaymentType.IMMEDIATE };

    Stream.of(axPaymentTypesForDirectInvoice)
    .forEach(paymentType -> axPaymentTypes.putIfAbsent(paymentType,
        PaymentMethodType.DIRECT_INVOICE));

    return axPaymentTypes;
  }

}
