package com.sagag.eshop.service.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class WssImportMarginBrandResponseDto implements Serializable {
  private static final long serialVersionUID = -7051459390727537054L;

  private Integer totalImported;
  private Integer totalItemToImport;
}
