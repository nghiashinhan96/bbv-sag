package com.sagag.eshop.repo.api.message;

import com.sagag.eshop.repo.entity.message.MessageType;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interfacing for {@link MessageType}.
 *
 */
public interface MessageTypeRepository extends JpaRepository<MessageType, Integer>{

}
