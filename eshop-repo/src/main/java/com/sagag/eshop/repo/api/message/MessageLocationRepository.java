package com.sagag.eshop.repo.api.message;

import com.sagag.eshop.repo.entity.message.MessageLocation;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interfacing for {@link MessageLocation}.
 *
 */
public interface MessageLocationRepository extends JpaRepository<MessageLocation, Integer> {

}
