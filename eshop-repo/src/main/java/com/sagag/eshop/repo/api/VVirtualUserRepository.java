package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.VVirtualUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VVirtualUserRepository extends JpaRepository<VVirtualUser, Long> {

  /**
   * Finds virtual userss.
   *
   * @param pageable the pageable request
   * @return Pageable of {@link VVirtualUser}
   */
  @Query(value = "SELECT u FROM VVirtualUser u ORDER BY u.firstLoginDate")
  Page<VVirtualUser> findVirtualUsers(Pageable pageable);

}
