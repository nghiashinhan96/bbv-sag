package com.sagag.services.domain.sag.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ExternalUserSession implements Serializable {

  private static final long serialVersionUID = 3047103641713698538L;

  private String traderId;

  private String user;

  private String uid;

  private String customerId;

  private String customerFirstName;

  private String privileges;

  private String startPage;

  private Integer startPagePosition;

  private String hKat;

  private String dateRequest;

  private String lId;

  private String sid;

}
