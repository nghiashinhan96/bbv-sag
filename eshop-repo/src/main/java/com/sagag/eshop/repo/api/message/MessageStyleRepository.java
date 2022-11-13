package com.sagag.eshop.repo.api.message;

import com.sagag.eshop.repo.entity.message.MessageStyle;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interfacing for {@link MessageStyle}.
 *
 */
public interface MessageStyleRepository extends JpaRepository<MessageStyle, Integer> {

}
