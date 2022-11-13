package com.sagag.services.ax.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.domain.sag.erp.BulkArticleResult;

import lombok.Data;

/**
 * Class to receive the bulk article result from Dynamic AX ERP.
 *
 */
@Data
@JsonPropertyOrder({ "article", "articleId" })
public class AxBulkArticleResult implements Serializable {

  private static final long serialVersionUID = 7730964825321219428L;

  private AxArticle article;

  private String articleId;

  public BulkArticleResult toBulkArticleResult() {
    return BulkArticleResult.builder().articleId(this.articleId)
        .article(this.article.toArticleDto()).build();
  }

}
