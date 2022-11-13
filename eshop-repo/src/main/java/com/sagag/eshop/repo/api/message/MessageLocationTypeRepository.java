package com.sagag.eshop.repo.api.message;

import com.sagag.eshop.repo.entity.message.MessageLocationType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository interfacing for {@link MessageLocationType}.
 *
 */
public interface MessageLocationTypeRepository extends JpaRepository<MessageLocationType, Integer> {

  @Query("select lt.locationType from MessageLocationType lt where lt.id =:id")
  Optional<String> findLocationTypeById(@Param("id") Integer id);
}
