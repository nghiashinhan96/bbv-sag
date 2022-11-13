package com.sagag.services.ivds.response;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
public class GetArticleInformationResponse implements Serializable {

  private static final long serialVersionUID = -1388733267672767970L;

  private Map<String, ArticleInformationResponseItem> items;

  public Map<String, ArticleInformationResponseItem> getItems() {
    return this.items.entrySet().stream()
        .filter(entry -> !StringUtils.isBlank(entry.getKey()))
        .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
  }

}
