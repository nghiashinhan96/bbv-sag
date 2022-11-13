package com.sagag.services.service.dto.feedback;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDataItem implements Serializable {

  private static final long serialVersionUID = -6390357219969260084L;

  private String key;
  private String value;
  private String title;
  private List<FeedbackDataItem> childs;
  private boolean isShortTechnicalData;

  @JsonIgnore
  public String getContent() {
    return FeedbackItemBuilder.buildHtml(this);
  }
}
