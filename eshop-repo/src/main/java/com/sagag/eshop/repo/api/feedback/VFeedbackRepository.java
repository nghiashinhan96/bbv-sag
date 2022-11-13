package com.sagag.eshop.repo.api.feedback;

import com.sagag.eshop.repo.entity.feedback.VFeedback;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interfacing for {@link VFeedback}.
 *
 */
public interface VFeedbackRepository extends JpaRepository<VFeedback, Long> {

}
