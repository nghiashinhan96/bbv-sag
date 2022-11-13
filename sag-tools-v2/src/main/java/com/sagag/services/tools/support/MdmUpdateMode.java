package com.sagag.services.tools.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration for define MDM update mode.
 *
 */
@Getter
@AllArgsConstructor
public enum MdmUpdateMode {

  CREATE(false), DELETE(true);

  private Boolean value;

}
