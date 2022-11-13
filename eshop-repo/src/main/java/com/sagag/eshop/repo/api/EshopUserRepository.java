package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.EshopUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Eshop user JPA Repository.
 */
public interface EshopUserRepository extends JpaRepository<EshopUser, Long> {

  List<EshopUser> findUsersByEmail(String email);

  List<EshopUser> findUsersByUsername(String username);

  /**
   * Returns user full name by id.
   *
   * @param userId user id
   * @return the full name
   */
  @Query("select u.firstName + ' ' + u.lastName from EshopUser u where u.id= :userId")
  String searchFullNameById(@Param("userId") Long userId);

  @Query(value = "select new com.sagag.eshop.repo.entity.EshopUser("
      + "u.id, u.username, u.firstName, u.lastName, l.langiso, u.email, u.hourlyRate, u.vatConfirm, u.type) "
      + "from EshopUser u join u.language l "
      + "where u.id = :userId")
  Optional<EshopUser> findUserByUserId(@Param("userId") long userId);

  @Query(value = "select new com.sagag.eshop.repo.entity.EshopUser("
      + "u.id, u.username, u.firstName, u.lastName, l.langiso, u.email, u.hourlyRate, u.vatConfirm, "
      + "u.type, u.signInDate, lg.firstLoginDate, lg.lastOnBehalfOfDate, u.originalUserId) "
      + "from EshopUser u join u.language l join u.login lg "
      + "where u.id = :userId")
  Optional<EshopUser> findUserLoginByUserId(@Param("userId") long userId);

  /**
   * Returns the username from user id.
   *
   * @param userId user id
   * @return a username
   */
  @Query("select u.username from EshopUser u where u.id = :id")
  Optional<String> findUsernameById(@Param("id") Long userId);

  @Query("select case when count(u) > 0 then true else false end "
      + "from EshopUser u, AadAccounts aad "
      + "where u.id=:userId "
      + "and u.groupUsers is empty "
      + "and u.email = aad.primaryContactEmail "
      + "and aad.permitGroup='SALES' ")
   boolean isAadSaleAccountHasNoRole(@Param("userId") Long userId);

  /**
   * Updates login date for user.
   *
   * @param lastLoginDate last login date
   * @param userId user id who logged in
   */
  @Modifying
  @Query(value = "update EshopUser u set u.signInDate = :loginDate where u.id=:userId")
  @Transactional
  void updateLoginSignInDate(@Param("loginDate") Date lastLoginDate, @Param("userId") long userId);

  @Query(value = "select u from EshopUser u inner join u.groupUsers gu "
                + "inner join gu.eshopGroup eg " + "inner join eg.organisationGroups og "
                + "inner join og.organisation o "
                + "where o.orgCode= :custNr and u.login.isUserActive = 1")
  List<EshopUser> findUsersByCustomerNumber(@Param("custNr") String custNr);

  @Query(value = "select u from EshopUser u "
      + "inner join u.groupUsers gu "
      + "inner join gu.eshopGroup eg "
      + "inner join eg.organisationGroups og "
      + "inner join og.organisation o "
      + "where o.id= :orgId")
  List<EshopUser> findUsersByOrgId(@Param("orgId") Integer orgId);

  @Query("select u.phone from EshopUser u where u.id = :id")
  Optional<String> findPhoneById(@Param("id") long id);

  @Query("select u.email from EshopUser u where u.id = :id")
  Optional<String> findEmailById(@Param("id") long id);

  /**
   * Remove EshopUser by userIds.
   *
   * @param userIds list of identified id
   */
  @Modifying
  @Query(value = "DELETE from EshopUser u where u.id IN :userIds")
  void removeEshopUsersByIds(@Param("userIds") List<Long> userIds);

  /**
   * Finds user by types.
   *
   * @param types the list of types to find
   * @param pageable the pageable request
   * @return Pageable of {@link EshopUser}
   */
  @Query(value = "SELECT u FROM EshopUser u WHERE u.type IN :types")
  Page<EshopUser> findUsersByType(@Param("types") List<String> types, Pageable pageable);
}
