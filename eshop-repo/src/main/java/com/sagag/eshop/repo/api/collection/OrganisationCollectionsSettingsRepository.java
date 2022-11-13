package com.sagag.eshop.repo.api.collection;

import com.sagag.eshop.repo.cache.RepoCacheMaps;
import com.sagag.eshop.repo.entity.collection.OrganisationCollectionsSettings;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrganisationCollectionsSettingsRepository
    extends JpaRepository<OrganisationCollectionsSettings, Integer> {

  List<OrganisationCollectionsSettings> findByCollectionId(int collectionId);

  @Query("select ocs from OrganisationCollectionsSettings ocs "
      + "where ocs.collectionId=:collectionId "
      + "and ocs.settingKey=:settingKey")
  Optional<OrganisationCollectionsSettings> findByCollectionIdAndKey(
      @Param("collectionId") Integer collectionId, @Param("settingKey") String settingKey);

  @Query("select ocs from OrganisationCollectionsSettings ocs, OrganisationCollection oc "
      + "where oc.shortname=:collectionShortname and oc.id=ocs.collectionId")
  @Cacheable(cacheNames = RepoCacheMaps.ORGANISATION_COLLECTIONS_SETTINGS_BY_COLL_SHORTNAME,
  unless = RepoCacheMaps.RESULT_IS_NULL)
  List<OrganisationCollectionsSettings> findByCollectionShortname(
      @Param("collectionShortname") String collectionShortname);

  @Query("select ocs from OrganisationCollectionsSettings ocs, CollectionRelation cr, Organisation o "
      + "where o.shortname=:orgShortname and cr.organisationId=o.id and ocs.collectionId=cr.collectionId")
  List<OrganisationCollectionsSettings> findSettingsByOrgShortname(@Param("orgShortname") String orgShortname);
  
  @Query("select ocs.settingValue from OrganisationCollectionsSettings ocs, CollectionRelation cr, Organisation o "
      + "where o.shortname=:orgShortname and cr.organisationId=o.id "
      + "and ocs.collectionId=cr.collectionId and ocs.settingKey=:key")
  Optional<String> findSettingValueByOrgShortnameAndSettingKey(
      @Param("orgShortname") String orgShortname, @Param("key") String key);

  @Query("select ocs.settingValue from OrganisationCollectionsSettings ocs, OrganisationCollection oc "
      + "where oc.shortname=:collectionShortname and oc.id=ocs.collectionId and ocs.settingKey=:key")
  Optional<String> findSettingValueByCollectionShortnameAndSettingKey(
      @Param("collectionShortname") String collectionShortname, @Param("key") String key);

  @Override
  @CacheEvict(cacheNames = RepoCacheMaps.ORGANISATION_COLLECTIONS_SETTINGS_BY_COLL_SHORTNAME)
  <S extends OrganisationCollectionsSettings> S save(S entity);

  @Override
  @CacheEvict(cacheNames = RepoCacheMaps.ORGANISATION_COLLECTIONS_SETTINGS_BY_COLL_SHORTNAME,
  allEntries = true)
  <S extends OrganisationCollectionsSettings> List<S> saveAll(Iterable<S> entities);
}
