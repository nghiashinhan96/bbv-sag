package com.sagag.services.ax.domain;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Class to receive the list of articles info response from Dynamic AX ERP.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AxArticlesResourceSupport extends ResourceSupport implements Serializable {
  private static final long serialVersionUID = 7387505673717189141L;

  private List<AxBulkArticleResult> articles;

  @JsonIgnore
  public boolean hasArticle() {
    return !CollectionUtils.isEmpty(articles);
  }
}
