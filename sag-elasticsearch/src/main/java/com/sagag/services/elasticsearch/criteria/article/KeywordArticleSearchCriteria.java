package com.sagag.services.elasticsearch.criteria.article;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class KeywordArticleSearchCriteria extends ArticleAggregateCriteria {

  @NonNull
  private String text;

  @NonNull
  private String[] affNameLocks;

  private boolean usePartsExt;

  private boolean onUsePartsExtMode;

  private boolean perfectMatched;

  private boolean searchExternal = true;

  private boolean directMatch;

  private boolean doubleLoopSearch;

  private boolean hasPreviousData;

  private boolean useExternalParts;

  public boolean hasText() {
    return !StringUtils.isBlank(text);
  }

  public void usePartsExt() {
    setUsePartsExt(true);
  }

  public void resetUsePartsExt() {
    setUsePartsExt(false);
  }

  public void onUsePartsExt() {
    setOnUsePartsExtMode(true);
  }

  @JsonIgnore
  private List<String> preferSagsysIds;

}
