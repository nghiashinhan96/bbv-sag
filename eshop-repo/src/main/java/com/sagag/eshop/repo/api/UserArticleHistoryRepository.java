package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.UserArticleHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * User Article history JPA repository.
 */
public interface UserArticleHistoryRepository extends JpaRepository<UserArticleHistory, Long> {

  @Query("select uah from UserArticleHistory uah where uah.id = :id and uah.userId = :userId")
  Optional<UserArticleHistory> findByIdAndUserId(@Param("id") long id, @Param("userId") long userId);

  /**
   * Removes user article history by userId.
   *
   * @param userIds list of identified id
   */
  @Modifying
  @Query(value = "DELETE FROM USER_ARTICLE_HISTORY WHERE USER_ID IN :userIds", nativeQuery = true)
  void removeArticleHistoryByUserIds(@Param("userIds") List<Long> userIds);
}
