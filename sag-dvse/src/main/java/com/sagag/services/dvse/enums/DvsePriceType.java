package com.sagag.services.dvse.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p> Get article price, availability and details from sagsys. </p>
 *
 * <p> Sagsys prices types map to DVSE price codes as follows: </p>
 *
 * <pre>
 *    'brutto' => 5
 *    'netto' => 6
 *    'discount' => 9
 *    'UNKNOWN' => 0
 *    'UVP' => 1
 *    'PFAND_1' => 2
 *    'depot' => 3
 *    'EK_SAG' => 4
 *    'WERKSTATT' => 8
 *    'KALKULIERT_TAX' => 10
 * </pre>
 *
 *
 */
@Getter
@AllArgsConstructor
public enum DvsePriceType {

  BRUTTO(5), NETTO(6), DISCOUNT(9), UNKNOWN(0), UVP(1),
  PFAND_1(2), DEPOT(3), EK_SAG(4), WERKSTATT(8), KALKULIERT_TAX(10),
  OEP_PRICE(11);

  private int code;

}
