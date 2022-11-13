package com.sagag.eshop.repo.api.message;

import com.sagag.eshop.repo.entity.message.MessageArea;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interfacing for {@link MessageArea}.
 *
 */
public interface MessageAreaRepository extends JpaRepository<MessageArea, Integer> {

}
