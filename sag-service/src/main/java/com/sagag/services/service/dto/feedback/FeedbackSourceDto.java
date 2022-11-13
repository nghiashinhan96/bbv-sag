package com.sagag.services.service.dto.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackSourceDto implements Serializable {

  private static final long serialVersionUID = -3249357553180068778L;
  private String title;
  private String source;
  private String code;
}
