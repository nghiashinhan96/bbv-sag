package com.sagag.services.tools.repository.target;
import com.sagag.services.tools.domain.target.CollectionPermission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for {@link CollectionPermission}
 */
public interface CollectionPermissionRepository extends JpaRepository<CollectionPermission, Integer> {

  @Query("select cp from OrganisationCollection oc, CollectionPermission cp "
      + "where oc.shortname=:collectionShortName and cp.collectionId=oc.id")
  List<CollectionPermission> findByCollectionShortName(@Param("collectionShortName") String collectionShortName);

  @Query("select cp from CollectionPermission cp where cp.collectionId in :collectionIds and cp.eshopPermissionId =:permissionId")
  List<CollectionPermission> findByCollectionIdsAndPermissionId(@Param("collectionIds") List<Integer> collectionIds,
      @Param("permissionId") Integer permissionId);
}
