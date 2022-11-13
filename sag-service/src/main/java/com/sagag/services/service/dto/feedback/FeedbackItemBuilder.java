package com.sagag.services.service.dto.feedback;

import com.sagag.services.common.contants.SagConstants;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@UtilityClass
public class FeedbackItemBuilder {

  private static final String DIV_START = "<div>";
  private static final String DIV_END = "</div>";
  private static final String SPAN_START = "<span>";
  private static final String SPAN_END = "</span>";
  private static final String UL_START = "<ul style='margin:0' >";
  private static final String UL_END = "</ul>";
  private static final String LI_START = "<li style='mso-special-format:bullet'>";
  private static final String LI_END = "</li>";

  private static final String BR = "</br>";


  public static String buildHtml(FeedbackDataItem item) {
    if (!CollectionUtils.isEmpty(item.getChilds())) {
      return FeedbackItemBuilder.getHtmlItems(item.getTitle(), item.getChilds());
    }
    return FeedbackItemBuilder.getHtmlItem(item);
  }

  private static String getHtmlItem(FeedbackDataItem item) {
    if (StringUtils.isEmpty(item.getTitle())) {
      return getItemWithoutValueOrTitle(item.getValue());
    }
    if (StringUtils.isEmpty(item.getValue())) {
      return getItemWithoutValueOrTitle(item.getTitle());
    }
    return getItemWithValueAndTitle(item);
  }

  //@formatter:off
  private static String getItemWithValueAndTitle(FeedbackDataItem item) {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append(DIV_START)
                    .append(SPAN_START)
                          .append(item.getTitle()).append(SagConstants.COLON).append(SagConstants.SPACE)
                    .append(SPAN_END)
                    .append(SPAN_START)
                          .append(item.getValue())
                    .append(SPAN_START)
              .append(DIV_END);
    return strBuilder.toString();
  }
  //@formatter:on

  //@formatter:off
  private static String getItemWithoutValueOrTitle(String content) {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append(DIV_START)
                    .append(SPAN_START)
                          .append(content)
                    .append(SPAN_END)
              .append(DIV_END);
    return strBuilder.toString();
  }
  //@formatter:on

  private static String getItemWithOrder(FeedbackDataItem item) {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append(LI_START).append(getHtmlItem(item)).append(LI_END);
    return strBuilder.toString();
  }

  private static String getItemsWithOrder(List<FeedbackDataItem> items) {
    StringBuilder strBuilder = new StringBuilder();
    for (FeedbackDataItem item : items) {
      strBuilder.append(getItemWithOrder(item));
    }
    return strBuilder.toString();
  }

  //@formatter:off
  private static String getHtmlItems(String title, List<FeedbackDataItem> items) {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append(BR).append(DIV_START)
                  .append(DIV_START)
                         .append(title).append(SagConstants.COLON).append(SagConstants.SPACE)
                  .append(DIV_END)
                  .append(UL_START)
                        .append(getItemsWithOrder(items))
                  .append(UL_END)
              .append(DIV_END).append(BR);
    return strBuilder.toString();
  }
  //@formatter:on
}
