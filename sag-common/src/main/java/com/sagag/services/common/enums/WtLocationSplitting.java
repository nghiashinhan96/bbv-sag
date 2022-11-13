package com.sagag.services.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WtLocationSplitting {

  MULTI_SHOP,
  FULLY_EXTERNAL,
  PARTLY_EXTERNAL,
  PARTLY_LOCAL,
  NOT_AVAILABLE,
  NONE;

}
