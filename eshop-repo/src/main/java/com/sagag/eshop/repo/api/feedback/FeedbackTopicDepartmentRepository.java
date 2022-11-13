package com.sagag.eshop.repo.api.feedback;

import com.sagag.eshop.repo.entity.feedback.FeedbackTopicDepartment;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interfacing for {@link FeedbackTopicDepartment}.
 *
 */
public interface FeedbackTopicDepartmentRepository extends JpaRepository<FeedbackTopicDepartment, Integer> {

}
