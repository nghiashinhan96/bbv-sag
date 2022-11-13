package com.sagag.eshop.repo.api.message;

import com.sagag.eshop.repo.entity.message.MessageVisibility;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interfacing for {@link MessageVisibility}.
 *
 */
public interface MessageVisibilityRepository extends JpaRepository<MessageVisibility, Integer> {

}
