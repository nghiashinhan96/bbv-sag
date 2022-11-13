package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.EshopFavorite;
import com.sagag.eshop.repo.enums.EshopFavoriteType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interfacing for {@link EshopFavorite}.
 *
 */
public interface EshopFavoriteRepository extends JpaRepository<EshopFavorite, Integer> {

  @Query(value = "select u from EshopFavorite u where u.type = 'LEAF_NODE' and "
      + " u.userId = :userId and u.treeId = :treeId and u.leafId = :leafId and u.gaId = :gaId")
  Optional<EshopFavorite> findFavoriteItemTypeLeaf(@Param("userId") Long userId,
      @Param("treeId") String treeId, @Param("leafId") String leafId, @Param("gaId") String gaId);

  @Query(value = "select u from EshopFavorite u where u.type = 'ARTICLE' and "
      + " u.userId = :userId and u.articleId = :articleId ")
  Optional<EshopFavorite> findFavoriteItemTypeArticle(@Param("userId") Long userId,
      @Param("articleId") String articleId);

  @Query(value = "select u from EshopFavorite u where u.type = 'VEHICLE' and "
      + " u.userId = :userId and u.vehicleId = :vehicleId ")
  Optional<EshopFavorite> findFavoriteItemTypeVehicle(@Param("userId") Long userId,
      @Param("vehicleId") String vehicleId);

  @Query(
      value = "select u.comment from EshopFavorite u where u.userId = :userId and u.comment <> '' "
          + "and upper(u.comment) Like CONCAT('%',upper(:key),'%') "
          + "group by u.comment order by max(u.lastUpdate) desc")
  List<String> findLatestUniqueComments(@Param("userId") Long userId, @Param("key") String key,
      Pageable pageable);

  List<EshopFavorite> findByUserIdOrderByCreatedTimeDesc(Long userId);

  @Query(value = "select u from EshopFavorite u where u.userId = :userId "
      + "order by u.createdTime desc ")
  Page<EshopFavorite> findLatestFavoriteByUserId(@Param("userId") Long userId, Pageable pageable);

  @Query(value = "select u from EshopFavorite u where u.userId = :userId and ("
      + " (u.type = 'ARTICLE' AND (u.articleId in :articleIds)) or "
      + " (u.type = 'VEHICLE' AND (u.vehicleId in :vehicleIds)) or "
      + " (u.type = 'LEAF_NODE' AND concat(trim(u.treeId),'_', trim(u.leafId),'_', trim(u.gaId)) IN :leafKeyIds))")
  List<EshopFavorite> findFavoriteItems(@Param("userId") Long userId,
      @Param("articleIds") List<String> articleIds, @Param("vehicleIds") List<String> vehicleIds,
      @Param("leafKeyIds") List<String> leafKeyIds);

  @Query(value = "select u from EshopFavorite u where u.type = 'ARTICLE' and "
      + " u.userId = :userId and u.articleId in :articleIds ")
  List<EshopFavorite> findFavoriteItemsByArticleIds(@Param("userId") Long userId,
      @Param("articleIds") List<String> articleIds);

  @Query(value = "select u from EshopFavorite u where u.type = 'VEHICLE' and "
      + " u.userId = :userId and u.vehicleId in :vehicleIds ")
  List<EshopFavorite> findFavoriteItemsByVehicleIds(@Param("userId") Long userId,
      @Param("vehicleIds") List<String> vehicleIds);

  @Query(value = "select u from EshopFavorite u where " + "( :type is null or u.type = :type ) "
      + "and u.userId = :userId and ( upper(u.title) Like CONCAT('%',upper(:key),'%') "
      + "or upper(u.comment) Like CONCAT('%',upper(:key),'%')) order by u.createdTime desc")
  Page<EshopFavorite> searchFavoriteItems(@Param("userId") Long userId,
      @Param("type") EshopFavoriteType favoriteType, @Param("key") String keySearch,
      Pageable pageable);

  /**
   * Removes user favorite entries by userId.
   *
   * @param userIds list of identified id
   */
  @Modifying
  @Query(value = "DELETE FROM ESHOP_FAVORITE WHERE USER_ID IN :userIds", nativeQuery = true)
  void removeFavoriteItemsByUserIds(@Param("userIds") List<Long> userIds);
}
