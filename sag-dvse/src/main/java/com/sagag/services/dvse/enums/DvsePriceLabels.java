package com.sagag.services.dvse.enums;

import lombok.experimental.UtilityClass;

/**
 * <p> Get article price, availability and details from sagsys. </p>
 *
 * <p> Following price labels/descriptions are defined in current shop (language = de): </p>
 *
 * <pre>
 *    'brutto' => 'Preisempf. je Einheit'
 *    'netto' => 'Nettopreis je Einheit'
 *    'depot' => 'Dosenpfand'
 * </pre>
 *
 *
 */
@UtilityClass
public final class DvsePriceLabels {

  public static final String NETTO_LABEL_DE = "Nettopreis je Einheit";

  public static final String BRUTTO_LABEL_DE = "Preisempf. je Einheit";

  public static final String RABATT_LABEL_DE = "Rabatt";

  public static final String DEPOT_LABEL_DE = "Dosenpfand";

  public static final String OEP_LABEL_DE = "OE Preis je Einheit";

}
