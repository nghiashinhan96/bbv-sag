package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.MappingUserIdEblConnect;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MappingUserIdEblConnectRepository extends JpaRepository<MappingUserIdEblConnect, Long> {

  List<MappingUserIdEblConnect> findByConnectOrgIdIn(List<Integer> connectOrgIds);

  @Query("select distinct m.connectOrgId from MappingUserIdEblConnect m where m.eblOrgId = :eblOrgId")
  Optional<Integer> findConnectOrgIdByEblOrgId(@Param("eblOrgId") Long eblOrgId);

  @Query("select distinct m.connectOrgId from MappingUserIdEblConnect m where m.eblOrgId = :vendorId")
  Integer findOrgIdByEbl(@Param("vendorId") Long vendorId);

  /**
   * @deprecated Please use {@link MappingUserIdEblConnectRepository#findUserIdsByEbl }
   */
  @Deprecated
  @Query("select m.connectUserId from MappingUserIdEblConnect m where m.eblUserId = :personId")
  Long findUserIdByEbl(@Param("personId") Long personId);

  @Query("select m.connectUserId from MappingUserIdEblConnect m where m.eblUserId = :personId")
  List<Long> findUserIdsByEbl(@Param("personId") Long personId);
}
