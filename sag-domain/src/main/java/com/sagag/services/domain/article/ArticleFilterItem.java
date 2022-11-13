package com.sagag.services.domain.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleFilterItem implements Serializable, Comparable<ArticleFilterItem> {

  private static final long serialVersionUID = -3996070262857123198L;

  private String id;

  private String description;

  private long amountItem;

  @JsonIgnore
  private Map<String, List<ArticleFilterItem>> subFilters;

  private List<ArticleFilterItem> children;

  private String uuid;

  private String type;

  @JsonProperty("isSelected")
  private boolean isSelected;

  private boolean hasChildren;

  private String parentId;

  @JsonProperty("isShown")
  private boolean isShown;

  @JsonProperty("isExpanded")
  private boolean isExpanded;

  private Map<String, String> descriptions;

  public static ArticleFilterItem showMoreFilterItem() {
    ArticleFilterItem item = new ArticleFilterItem();
    item.setId(UUID.randomUUID().toString());
    item.setShown(true);
    item.setDescription("showMore");
    item.setExpanded(false);
    return item;
  }

  @Override
  public int compareTo(final ArticleFilterItem otherArtFilterItem) {
    if (StringUtils.isBlank(this.getId())) {
      return -1;
    }
    return this.getId().compareTo(otherArtFilterItem.getId());
  }

  public List<ArticleFilterItem> getChildren() {
    return ListUtils.emptyIfNull(children);
  }
}
