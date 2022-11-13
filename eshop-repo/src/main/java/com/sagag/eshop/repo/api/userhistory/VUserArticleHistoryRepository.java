package com.sagag.eshop.repo.api.userhistory;

import com.sagag.eshop.repo.entity.user_history.VUserArticleHistory;
import com.sagag.services.domain.eshop.dto.ArticleHistoryDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VUserArticleHistoryRepository extends JpaRepository<VUserArticleHistory, Long>,
    JpaSpecificationExecutor<VUserArticleHistory> {

  @Query(value = "SELECT new com.sagag.services.domain.eshop.dto."
      + "ArticleHistoryDto(vuah.id, vuah.articleId, vuah.artnrDisplay, vuah.selectDate, vuah.searchTerm, "
      + "vuah.searchMode, vuah.fullName, vuah.fromSource, vuah.searchTermWithArtNr, vuah.rawSearchTerm) "
      + "FROM VUserArticleHistory vuah WHERE vuah.userId = :userId "
      + "ORDER BY vuah.selectDate DESC")
  Page<ArticleHistoryDto> findTopArticleHistories(@Param("userId") long userId, Pageable pageable);

  @Query(value = "SELECT TOP 1 vuah.ID "
      + "FROM V_USER_ARTICLE_HISTORY vuah WHERE vuah.USER_ID = :userId "
      + "AND lower(vuah.RAW_SEARCH_TERM) = lower(:rawSearchTerm) "
      + "AND (:artId IS NULL OR lower(vuah.ARTICLE_ID) = lower(:artId)) "
      + "AND lower(vuah.FROM_SOURCE) = lower(:fromSource) "
      + "ORDER BY vuah.SELECT_DATE DESC", nativeQuery = true)
  Long findLatestBySearchTermAndArticleId(@Param("userId") long userId,
      @Param("rawSearchTerm") String rawSearchTerm, @Param("artId") String artId, @Param("fromSource") String fromSource);

  @Query(
      value = "SELECT TOP 1 vuah.ID "
          + "FROM V_USER_ARTICLE_HISTORY vuah WHERE vuah.USER_ID = :userId "
          + "AND lower(vuah.RAW_SEARCH_TERM) = lower(:rawSearchTerm) "
          + "AND lower(vuah.SEARCH_MODE) != lower(:searchMode) "
          + "AND lower(vuah.FROM_SOURCE) = lower(:fromSource) "
          + "ORDER BY vuah.SELECT_DATE DESC",
      nativeQuery = true)
  Long findLatestBySearchTermAndNotSearchMode(@Param("userId") long userId,
      @Param("rawSearchTerm") String rawSearchTerm, @Param("searchMode") String searchMode,
      @Param("fromSource") String fromSource);
}
