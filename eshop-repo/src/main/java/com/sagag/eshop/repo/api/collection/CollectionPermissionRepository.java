package com.sagag.eshop.repo.api.collection;
import com.sagag.eshop.repo.entity.collection.CollectionPermission;

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

}
