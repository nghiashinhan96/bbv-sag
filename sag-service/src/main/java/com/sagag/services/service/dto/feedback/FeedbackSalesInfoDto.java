package com.sagag.services.service.dto.feedback;

import lombok.Data;

import java.io.Serializable;

@Data
public class FeedbackSalesInfoDto implements Serializable {

  private static final long serialVersionUID = 9111216878352889739L;
  private String title;
  private String info;
}
