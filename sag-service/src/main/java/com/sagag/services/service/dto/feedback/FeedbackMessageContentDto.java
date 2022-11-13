package com.sagag.services.service.dto.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackMessageContentDto implements Serializable {

  private static final long serialVersionUID = 795203894173037500L;

  private String title;
  private String content;
}
