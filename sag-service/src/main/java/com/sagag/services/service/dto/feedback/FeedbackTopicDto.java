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
public class FeedbackTopicDto implements Serializable {

  private static final long serialVersionUID = 9031933935215158870L;

  private String title;
  private String topicCode;
  private String topic;
}
