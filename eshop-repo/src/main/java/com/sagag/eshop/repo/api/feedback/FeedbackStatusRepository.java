package com.sagag.eshop.repo.api.feedback;

import com.sagag.eshop.repo.entity.feedback.FeedbackStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository interfacing for {@link FeedbackStatus}.
 *
 */
public interface FeedbackStatusRepository extends JpaRepository<FeedbackStatus, Integer> {

  @Query("select s.id from FeedbackStatus s where s.statusCode = :code")
  Optional<Integer> findIdByStatusCode(@Param("code") String code);

}
