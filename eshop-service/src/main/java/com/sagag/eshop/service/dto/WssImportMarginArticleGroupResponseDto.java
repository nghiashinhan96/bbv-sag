package com.sagag.eshop.service.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class WssImportMarginArticleGroupResponseDto implements Serializable {
  private static final long serialVersionUID = -2547918975109431964L;
  private Integer totalImported;
  private Integer totalItemToImport;
}
