package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.WssMarginsArticleGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WssMarginsArticleGroupRepository
    extends CrudRepository<WssMarginsArticleGroup, Integer>, JpaSpecificationExecutor<WssMarginsArticleGroup> {


  /**
   * Return WssMarginsArticleGroup from id
   *
   * @param id
   * @return WssMarginsArticleGroup
   */
  Optional<WssMarginsArticleGroup> findOneById(int id);

  @Query("select w from WssMarginsArticleGroup w where w.orgId = :orgId")
  List<WssMarginsArticleGroup> findAllByOrgId(@Param("orgId") int orgId);

  @Query("select w from WssMarginsArticleGroup w where w.orgId = :orgId and w.sagArtGroup is null")
  Optional<WssMarginsArticleGroup> findDefaultSettingByOrgId(@Param("orgId") int orgId);

  @Query("select w from WssMarginsArticleGroup w "
      + "where w.orgId = :orgId "
      + "and (w.sagArtGroup in :sagArtGroupIds or w.isDefault is true) "
      + "and w.isMapped is true "
      + "order by w.groupLevel desc")
  List<WssMarginsArticleGroup> findFirstOrDefaultSettingByOrgIdAndSagArticleGroupIds(
      @Param("orgId") int orgId, @Param("sagArtGroupIds") List<String> sagArtGroupIds,
      Pageable pageable);

  @Query("select w from WssMarginsArticleGroup w where w.orgId = :orgId and w.sagArtGroup = :sagArtGroup")
  Optional<WssMarginsArticleGroup> findMarginArticleGroupByOrgIdAndSagArtGroupId(@Param("orgId") int orgId, @Param("sagArtGroup") String sagArtGroup);

  @Query(value = "select case when count(w) > 0 then 'true' else 'false' end "
      + "from WssMarginsArticleGroup w where w.orgId = :orgId and w.parentId = :parentId ")
  boolean checkMarginArticleGroupHasChild(@Param("orgId") int orgId, @Param("parentId") int parentId);

  @Query(value = "select case when count(w) > 0 then 'true' else 'false' end "
      + "from WssMarginsArticleGroup w where w.orgId = :orgId and w.sagArtGroup = :sagArtGroup ")
  boolean checkMarginArticleGroupExist(@Param("orgId") int orgId, @Param("sagArtGroup") String sagArtGroup);

  @Query("select w from WssMarginsArticleGroup w where w.orgId = :orgId and w.leafId = :leafId")
  Optional<WssMarginsArticleGroup> findParentWssMarginArticleGroup(@Param("orgId") int orgId, @Param("leafId") String leafId);

  @Query(value = "select case when count(w) > 0 then 'true' else 'false' end "
      + "from WssMarginsArticleGroup w where w.orgId = :orgId and w.isMapped = true and w.isDefault = true ")
  boolean checkDefaultMarginArticleGroupExist(@Param("orgId") int orgId);

  @Query("select w from WssMarginsArticleGroup w where w.orgId = :orgId and w.isDefault = true")
  Optional<WssMarginsArticleGroup> findDefaultWssMarginArticleGroup(@Param("orgId") int orgId);

  @Query("select w from WssMarginsArticleGroup w where w.orgId = :orgId and w.isDefault = false and w.groupLevel = 1 order by w.sagArtGroup asc")
  Page<WssMarginsArticleGroup> findAllRootMarginArticleGroup(@Param("orgId") int orgId, Pageable pageable);

  @Query("select w from WssMarginsArticleGroup w where w.orgId = :orgId and w.isDefault = false and w.parentId = :parentId order by w.sagArtGroup asc")
  List<WssMarginsArticleGroup> findAllChildMarginArticleGroupByParentId(@Param("orgId") int orgId, @Param("parentId") int parentId);

  Optional<WssMarginsArticleGroup> findBySagArtGroupAndOrgId(String sagArtGroup, int orgId);

  @Query("select w from WssMarginsArticleGroup w where w.orgId = :orgId and w.isDefault = false order by w.sagArtGroup asc")
  List<WssMarginsArticleGroup> findAllMarginArticleGroupExceptDefault(@Param("orgId") int orgId);
  
  @Query("select w from WssMarginsArticleGroup w where w.orgId = :orgId and lower(w.sagArtGroup) = lower(:sagArtGroup)")
  Optional<WssMarginsArticleGroup> findWssMarginArticleGroupByArtGroup(@Param("orgId") int orgId, @Param("sagArtGroup") String sagArtGroup);
}
