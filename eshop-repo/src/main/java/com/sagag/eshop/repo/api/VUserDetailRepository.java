package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.VUserDetail;
import com.sagag.services.common.enums.ExternalApp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Jpa repository for user detail view.
 */
public interface VUserDetailRepository
    extends JpaRepository<VUserDetail, Long>, JpaSpecificationExecutor<VUserDetail> {

  /**
   * Returns the view user details.
   *
   * @param userId the request user id
   * @return the optional of {@link VUserDetail}
   */
  Optional<VUserDetail> findByUserId(Long userId);

  @Query("select new com.sagag.eshop.repo.entity.VUserDetail("
      + "vu.userId, vu.userName, vu.saleOnBehalfOf) from VUserDetail vu where vu.orgCode =:orgCode and vu.userType is null")
  List<VUserDetail> findUsersByCustomer(@Param("orgCode") String orgCode);

  @Query("select new com.sagag.eshop.repo.entity.VUserDetail("
      + "vu.userId, vu.userName, vu.saleOnBehalfOf) from VUserDetail vu where vu.orgCode =:orgCode "
      + "and vu.userType = 'ON_BEHALF_ADMIN'")
  Optional<VUserDetail> findUserByOnBehalfAdmin(@Param("orgCode") String orgCode);

  /**
   * Returns the organisation id for that user.
   *
   * @param userId the user id
   * @return the organisation id.
   */
  @Query("select u.orgId from VUserDetail u where u.userId = :userId")
  Optional<Integer> findOrgIdByUserId(@Param("userId") Long userId);

  @Query("select vu.userName from VUserDetail vu where vu.orgId =:orgId")
  List<String> findUsernamesByOrgId(@Param("orgId") Integer orgId);

  @Query("select vu from VUserDetail vu where vu.orgId =:orgId and vu.userType is null")
  List<VUserDetail> findUsersByOrgId(@Param("orgId") Integer orgId);

  @Query(value = "select vu.orgCode from VUserDetail vu where vu.userId=:userId")
  Optional<String> findOrgCodeByUserId(@Param("userId") final Long userId);

  @Query(value = "select vu.userName from VUserDetail vu where vu.userId=:userId")
  Optional<String> findUsernameByUserId(@Param("userId") final Long userId);

  @Query(value = "select org.shortname from VUserDetail vu , Organisation org "
      + "where vu.orgParentId = org.id and vu.userId=:userId")
  Optional<String> findAffiliateShortNameByUserId(@Param("userId") Long userId);

  @Query("select case when count(vu) > 0 then true else false end "
      + "from VUserDetail vu where vu.orgCode =:orgCode and vu.userType is null")
  boolean existsUsersByOrgCode(@Param("orgCode") String orgCode);

  @Query("select case when count(vu) > 0 then true else false end "
    + "from VUserDetail vu where vu.userName = :username and vu.userType = 'ON_BEHALF_ADMIN'")
  boolean existsOnBehalfUserName(@Param("username") String username);
  
  @Query("select vu from VUserDetail vu where vu.orgId =:orgId "
      + "and vu.userId not in (select eu.eshopUserId from ExternalUser eu where eu.externalApp = :externalApp and eu.eshopUserId is not null)")
  List<VUserDetail> findUsersByOrgIdNotHaveExternalUser(@Param("orgId") Integer orgId, @Param("externalApp") ExternalApp externalApp);
}
