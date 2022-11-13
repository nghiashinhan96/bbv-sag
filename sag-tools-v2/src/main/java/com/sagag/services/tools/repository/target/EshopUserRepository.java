package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.EshopUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * EShop User JPA Repository interface.
 */
@Repository
public interface EshopUserRepository extends JpaRepository<EshopUser, Long> {

  List<EshopUser> findByUsernameAndEmail(String username, String email);

  @Query(value = "select eu from EshopUser eu where eu.setting = :setting")
  Optional<EshopUser> findBySettingId(@Param("setting") Integer setting);

  List<EshopUser> findUsersByUsername(String username);

  Optional<EshopUser> findByUsername(String username);

  @Query(value = "select u.id from EshopUser u inner join u.groupUsers gu "
      + "inner join gu.eshopGroup eg " + "inner join eg.organisationGroups og "
      + "inner join og.organisation o "
      + "where o.orgCode= :custNr and u.login.isUserActive = 1 and u.type like '%ON_BEHALF_ADMIN%'")
  Optional<Long> findUserOnBehalfAdminByCustomerNumber(@Param("custNr") String custNr);

  Optional<EshopUser> findByEmail(String email);

}
