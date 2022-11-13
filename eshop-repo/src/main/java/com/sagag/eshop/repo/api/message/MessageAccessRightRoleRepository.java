package com.sagag.eshop.repo.api.message;

import com.sagag.eshop.repo.entity.message.MessageAccessRightRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interfacing for {@link MessageAccessRightRole}.
 *
 */
public interface MessageAccessRightRoleRepository extends
    JpaRepository<MessageAccessRightRole, Integer> {

}
