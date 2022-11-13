package com.sagag.eshop.repo.api.feedback;

import com.sagag.eshop.repo.entity.feedback.Feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interfacing for {@link Feedback}.
 *
 */
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

  /**
   * Removes feedback by userId.
   *
   * @param userIds list of identified id
   */
  @Modifying
  @Query(value = "DELETE FROM FEEDBACK WHERE USER_ID IN :userIds", nativeQuery = true)
  void removeFeedbackByUserIds(@Param("userIds") List<Long> userIds);
}
