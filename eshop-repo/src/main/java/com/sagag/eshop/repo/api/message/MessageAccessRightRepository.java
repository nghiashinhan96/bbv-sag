package com.sagag.eshop.repo.api.message;

import com.sagag.eshop.repo.entity.message.MessageAccessRight;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interfacing for {@link MessageAccessRight}.
 *
 */
public interface MessageAccessRightRepository extends JpaRepository<MessageAccessRight, Integer> {

}
