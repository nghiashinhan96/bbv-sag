package com.sagag.services.tools.repository.target;


import com.sagag.services.tools.domain.target.VUserDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Jpa repository for user detail view.
 */
@Repository
public interface VUserDetailRepository extends JpaRepository<VUserDetail, Long> {

  /**
   * Returns the organisation id for that user.
   *
   * @param userId the user id
   * @return the organisation id.
   */
  @Query("select u.orgId from VUserDetail u where u.userId = :userId")
  Optional<Integer> findOrgIdByUserId(@Param("userId") Long userId);
}
