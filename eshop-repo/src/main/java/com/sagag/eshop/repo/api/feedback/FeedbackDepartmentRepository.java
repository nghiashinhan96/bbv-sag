package com.sagag.eshop.repo.api.feedback;

import com.sagag.eshop.repo.entity.feedback.FeedbackDepartment;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interfacing for {@link FeedbackDepartment}.
 *
 */
public interface FeedbackDepartmentRepository extends JpaRepository<FeedbackDepartment, Integer> {

}
