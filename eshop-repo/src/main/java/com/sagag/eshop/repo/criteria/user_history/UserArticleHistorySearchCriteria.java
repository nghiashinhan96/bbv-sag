package com.sagag.eshop.repo.criteria.user_history;

import com.sagag.eshop.repo.enums.ArticleHistorySearchType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserArticleHistorySearchCriteria implements Serializable {

  private static final long serialVersionUID = -3269645650369152457L;

  private Integer orgId;

  private Long userId;

  private String fullName;

  private String filterMode;

  private String searchMode;

  private ArticleHistorySearchType searchType;

  private String searchTerm;

  private Date fromDate;

  private Date toDate;

  private Boolean orderDescBySelectDate;

}
