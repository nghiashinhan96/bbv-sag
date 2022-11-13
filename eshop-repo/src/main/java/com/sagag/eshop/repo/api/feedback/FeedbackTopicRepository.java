package com.sagag.eshop.repo.api.feedback;

import com.sagag.eshop.repo.entity.feedback.FeedbackTopic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interfacing for {@link FeedbackTopic}.
 *
 */
public interface FeedbackTopicRepository extends JpaRepository<FeedbackTopic, Integer> {

  FeedbackTopic findByTopicCode(String code);

  @Query("select t.topicCode from FeedbackTopic t order by sort")
  List<String> findSortedTopicCodes();
}
