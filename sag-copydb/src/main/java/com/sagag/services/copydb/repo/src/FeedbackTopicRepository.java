package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.FeedbackTopic;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackTopicRepository extends JpaRepository<FeedbackTopic, Integer> {
}
