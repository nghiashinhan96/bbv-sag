package com.sagag.eshop.repo.api.message;

import com.sagag.eshop.repo.entity.message.MessageRoleType;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interfacing for {@link MessageRoleType}.
 *
 */
public interface MessageRoleTypeRepository extends JpaRepository<MessageRoleType, Integer> {

}
