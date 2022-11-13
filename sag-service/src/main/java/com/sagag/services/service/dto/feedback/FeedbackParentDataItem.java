package com.sagag.services.service.dto.feedback;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.common.utils.SagJSONUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.tomcat.util.buf.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackParentDataItem implements Serializable {

  private static final long serialVersionUID = -4607126217955820848L;

  private List<FeedbackDataItem> items;
  private String title;

  @JsonIgnore
  public String getContent() {
    return StringUtils
        .join(items.stream().map(FeedbackDataItem::getContent).collect(Collectors.toList()), ' ');
  }

  @JsonIgnore
  public FeedbackParentDataItem getShortTechnicalData() {
    List<FeedbackDataItem> dataItems = CollectionUtils.emptyIfNull(items).stream()
        .filter(FeedbackDataItem::isShortTechnicalData).collect(Collectors.toList());
    return FeedbackParentDataItem.builder().title(title).items(dataItems).build();
  }

  @JsonIgnore
  public String toJson() {
    return SagJSONUtil.convertObjectToJson(this);
  }
}
