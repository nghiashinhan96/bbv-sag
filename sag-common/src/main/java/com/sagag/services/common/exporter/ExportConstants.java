package com.sagag.services.common.exporter;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Export Constants class.
 */
@UtilityClass
public final class ExportConstants {

  public static final String NEW_LINE_HTML = "<br />";

  public static final String MESSAGES_BUNDLE = "i8n/messages";

  public static final BigDecimal PERCENT_UNIT = BigDecimal.valueOf(100L);

  public static final DecimalFormat DEFAULT_DECIMAL_FORMAT = new DecimalFormat("0.00");

  public static final String UVPE = "uvpe";

  public static final String OEP = "oep";

  public static final String GROSS = "GROSS";

}
