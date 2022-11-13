package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.EshopUser;
import com.sagag.services.tools.domain.target.Login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface LoginRepository extends JpaRepository<Login, Integer> {

  /**
   * Finds the login information for specific user.
   *
   * @param userId user id
   * @return the login information object
   */
  @Query(value = "select l from Login l inner join l.eshopUser u where u.id=:userId")
  Login findOneByUserId(@Param("userId") long userId);

  /**
   * Remove Login by userIds.
   *
   * @param userIds list of identified id
   */
  @Modifying
  @Query(value = "DELETE FROM LOGIN WHERE USER_ID IN :userIds", nativeQuery = true)
  void removeLoginByUserIds(@Param("userIds") List<Long> userIds);

  /**
   * Returns the minimum of first login date by customer number.
   *
   * <pre>
   *
   * @param userId the request user id
   * @return the optional of {@link Date}
   */
  @Query(value = "select min(l.firstLoginDate) from Login l, VUserDetail vud " + "where vud.orgCode = :custNr and l.eshopUser.id = vud.userId")
  Date findMinFirstLoginDateByCustNr(@Param("custNr") String custNr);

  /**
   * Updates first login date for user.
   *
   * @param firstLogInDate first login date
   * @param id the id of login instance
   */
  @Modifying
  @Query(value = "update Login l set l.firstLoginDate = :firstLoginDate where l.id=:id")
  void updateFirstLoginDate(@Param("firstLoginDate") Date firstLoginDate, @Param("id") int id);

  /**
   * Updates last on behalf of date for user.
   *
   * @param lastOnBehalfOfDate last on behalf of date
   * @param id the id of login instance
   */
  @Modifying
  @Query(value = "update Login l set l.lastOnBehalfOfDate = :lastOnBehalfOfDate where l.id=:id")
  void updateLastOnBehalfOfDate(@Param("lastOnBehalfOfDate") Date lastOnBehalfOfDate, @Param("id") int id);

  Optional<Login> findFirstByEshopUser(EshopUser eshopUser);

}
