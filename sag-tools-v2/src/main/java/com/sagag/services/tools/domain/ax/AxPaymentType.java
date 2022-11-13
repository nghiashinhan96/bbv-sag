package com.sagag.services.tools.domain.ax;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.sagag.services.tools.exception.AxCustomerException;

import lombok.Getter;

/**
 * Enumeration class to define payment type from AX.
 *
 */
@Getter
public enum AxPaymentType {
  BAR("Bar"),
  BETREIBUNG("Betreibung"),
  FIN("Fin"),
  KARTE("Karte"),
  KEINE("Keine"),
  SAK("Sak"),
  SELBSTZAHL("Selbstzahl"),
  SEPA_B_FIN("Sepa B Fin"),
  SEPA_B_SAK("Sepa B Sak"),
  SEPA_B2B("Sepa B2B"),
  SEPA_C_FIN("Sepa C Fin"),
  SEPA_C_SAK("Sepa C Sak"),
  SEPA_CORE("Sepa Core"),
  SOFORT("Sofort");

  private String code;

  private static Map<String, AxPaymentType> map = new HashMap<>();

  static {
    for (AxPaymentType type : AxPaymentType.values()) {
      map.put(type.code, type);
    }
  }

  private AxPaymentType(final String code) {
    this.code = code;
  }

  public static AxPaymentType findByCode(final String code) {
    return Optional.ofNullable(map.get(code)).orElseThrow(
        () -> new AxCustomerException(AxCustomerException.AxCustomerErrorCase.AC_PAYMENT_TYPE_002, String.format("No support this payment type = %s", code)));
  }
}
