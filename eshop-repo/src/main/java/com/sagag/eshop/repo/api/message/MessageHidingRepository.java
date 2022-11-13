package com.sagag.eshop.repo.api.message;

import com.sagag.eshop.repo.entity.message.MessageHiding;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interfacing for {@link MessageHiding}.
 *
 */
public interface MessageHidingRepository extends JpaRepository<MessageHiding, Long> {

  @Query("SELECT m.messageId from MessageHiding m where m.userId = :userId")
  List<Long> findHidingMessagesByUser(@Param("userId") Long userId);

}
