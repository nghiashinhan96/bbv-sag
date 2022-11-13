package com.sagag.services.ax.enums;

import lombok.experimental.UtilityClass;

/**
 * The constant to define AX invoice type code.
 *
 */
@UtilityClass
public final class AxInvoiceTypes {

  /** 2WFAKT - 2 Wochen Faktura. */
  public static final String AX_2WFAKT = "2WFAKT";

  /** 2WFAKTGT - 2 Wochen Faktura mit Gutschriftstrennung. */
  public static final String AX_2WFAKTGT = "2WFAKTGT";

  /** EINZELFAGT - Fakturierung pro Auftrag mit Gutschriftstennung. */
  public static final String AX_EINZELFAGT = "EINZELFAGT";

  /** EINZELFAKT - Fakturierung pro Auftrag. */
  public static final String AX_EINZELFAKT = "EINZELFAKT";

  /** MONATSFAGT - Monatsfaktura mit Gutschriftstrennung. */
  public static final String AX_MONATSFAGT = "MONATSFAGT";

  /** MONATSFAKT - Monatsfaktura. */
  public static final String AX_MONATSFAKT = "MONATSFAKT";

  /** TAGESFAGT - Tagesfaktura mit Gutschriftstrennung. */
  public static final String AX_TAGESFAGT = "TAGESFAGT";

  /** TAGESFAKT - Tagesfaktura. */
  public static final String AX_TAGESFAKT = "TAGESFAKT";

  /** WOCHENFAGT - Wochenfaktura mit Gutschriftstrennung. */
  public static final String AX_WOCHENFAGT = "WOCHENFAGT";

  /** WOCHENFAKT - Wochenfaktura. */
  public static final String AX_WOCHENFAKT = "WOCHENFAKT";

  public static final String AX_2WFAKTGX = "2WFAKTGX";

  public static final String AX_MONATSFAGX = "MONATSFAGX";

  public static final String AX_MONATSFAGW = "MONATSFAGW";

  public static final String AX_WOCHENFAGX = "WOCHENFAGX";
}
