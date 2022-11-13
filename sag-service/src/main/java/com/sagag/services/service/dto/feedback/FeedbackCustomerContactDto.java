package com.sagag.services.service.dto.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class FeedbackCustomerContactDto implements Serializable {

  private static final long serialVersionUID = 2355096928523010587L;
  private String title;
  private String contact;
}
