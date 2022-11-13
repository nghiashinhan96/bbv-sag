package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.Feedback;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
