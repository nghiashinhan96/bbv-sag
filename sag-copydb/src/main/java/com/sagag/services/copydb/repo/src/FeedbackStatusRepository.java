package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.FeedbackStatus;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackStatusRepository extends JpaRepository<FeedbackStatus, Integer> {
}
