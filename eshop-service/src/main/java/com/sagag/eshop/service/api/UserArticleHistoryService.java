package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.criteria.user_history.UserArticleHistorySearchCriteria;
import com.sagag.services.common.enums.ArticleSearchMode;
import com.sagag.services.common.enums.UserHistoryFromSource;
import com.sagag.services.domain.eshop.dto.ArticleHistoryDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserArticleHistoryService {

  /**
   * Returns the article histories search by criteria.
   *
   * @param criteria
   * @param pageable
   * @return the page of article histories
   */
  Page<ArticleHistoryDto> searchArticleHistories(UserArticleHistorySearchCriteria criteria,
      Pageable pageable);

  /**
   * Get top article historical data for a user.
   *
   * @param userId the user to get his selected article history
   * @return a page of article history.
   */
  Page<ArticleHistoryDto> getLastestArticleHistory(long userId);

  void addArticleHistory(long userId, String articleId, String articleNumber, String searchTerm,
      ArticleSearchMode searchMode, UserHistoryFromSource fromSource, String rawSearchTerm);

  /**
   * update Article History Select Date
   * @param userId
   * @param userArticleHistoryId
   */
  void updateArticleHistorySelectDate(long userId, long userArticleHistoryId);

}
