package com.sagag.services.article.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalOrderHistoryRequest {

  private String status;
  private String from;
  private String to;
  private String username;
  private String articleNumber;
  private String orderNumber;

}
