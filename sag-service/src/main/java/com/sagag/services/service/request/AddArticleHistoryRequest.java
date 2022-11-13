package com.sagag.services.service.request;

import com.sagag.services.common.enums.ArticleSearchMode;

import lombok.Data;

import java.io.Serializable;

/**
 * Request class for creating article history.
 */
@Data
public class AddArticleHistoryRequest implements Serializable {

  private static final long serialVersionUID = -7036116063632226639L;

  private String articleId;

  private String articleNumber;

  private String searchTerm;

  private String rawSearchTerm;

  private ArticleSearchMode searchMode;

}
