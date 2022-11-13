package com.sagag.services.domain.sag.erp;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class OrderErrorMessage {
  
  private String code;
  
  private String key;
  
  private String message;
  
  private Map<String, Object> moreInfos;
  
}
