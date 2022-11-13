package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.OrganisationCollectionsSettings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrganisationCollectionsSettingsRepository
    extends JpaRepository<OrganisationCollectionsSettings, Integer> {

  List<OrganisationCollectionsSettings> findByCollectionId(int collectionId);

  @Query("select ocs from OrganisationCollectionsSettings ocs, OrganisationCollection oc "
      + "where oc.shortname=:collectionShortname and oc.id=ocs.collectionId "
      + "and ocs.settingKey=:settingKey")
  Optional<OrganisationCollectionsSettings> findByCollectionShortnameAndKey(
      @Param("collectionShortname") String collectionShortname, @Param("settingKey") String settingKey);

  @Query("select ocs from OrganisationCollectionsSettings ocs, OrganisationCollection oc "
      + "where oc.shortname=:collectionShortname and oc.id=ocs.collectionId")
  List<OrganisationCollectionsSettings> findByCollectionShortname(
      @Param("collectionShortname") String collectionShortname);

  @Query("select ocs from OrganisationCollectionsSettings ocs, CollectionRelation cr, Organisation o "
      + "where o.shortname=:orgShortname and cr.organisationId=o.id and ocs.collectionId=cr.collectionId")
  List<OrganisationCollectionsSettings> findSettingsByOrgShortname(@Param("orgShortname") String orgShortname);

  @Query("select ocs.settingValue from OrganisationCollectionsSettings ocs, OrganisationCollection oc "
      + "where oc.shortname=:collectionShortname and oc.id=ocs.collectionId and ocs.settingKey=:key")
  Optional<String> findSettingValueByCollectionShortnameAndSettingKey(
      @Param("collectionShortname") String collectionShortname, @Param("key") String key);

  @Override
  <S extends OrganisationCollectionsSettings> S save(S entity);

  @Override
  <S extends OrganisationCollectionsSettings> List<S> saveAll(Iterable<S> entities);
}
