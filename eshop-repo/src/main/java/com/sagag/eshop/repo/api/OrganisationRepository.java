package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.cache.RepoCacheMaps;
import com.sagag.eshop.repo.entity.Organisation;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Organisation JPA repository interface.
 *
 */
public interface OrganisationRepository extends JpaRepository<Organisation, Integer> {

  Optional<Organisation> findOneById(int id);

  @Cacheable(cacheNames = RepoCacheMaps.ORGANISATIONS_BY_SHORTNAME,
      unless = RepoCacheMaps.RESULT_IS_NULL)
  Optional<Organisation> findOneByShortname(String shortname);

  @Cacheable(cacheNames = RepoCacheMaps.ORGANISATIONS_BY_ORGCODE,
      unless = RepoCacheMaps.RESULT_IS_NULL)
  Optional<Organisation> findOneByOrgCode(String orgCode);

  @Query("select o.name from Organisation o where o.shortname = :shortname")
  @Cacheable(cacheNames = RepoCacheMaps.COMPANY_NAMES_BY_SHORTNAME,
  unless = RepoCacheMaps.RESULT_IS_NULL)
  Optional<String> findCompanyNameByShortname(@Param("shortname") String shortname);

  @Query("select o.id from Organisation o where o.shortname =:shortname")
  @Cacheable(cacheNames = RepoCacheMaps.IDS_BY_SHORTNAME,
  unless = RepoCacheMaps.RESULT_IS_NULL)
  Optional<Integer> findIdByShortName(@Param("shortname") String shortname);

  /**
   * Searches all organisations by the user by id.
   *
   * @param userId user id that belongs to the organisations
   * @return the list of organisations
   */
  @Query(value = "select new com.sagag.eshop.repo.entity.Organisation("
      + "o.id, o.name, o.orgCode, o.parentId, o.shortname) "
      + "from Organisation o join o.organisationGroups og join og.eshopGroup eg "
      + "join eg.groupUsers gu join gu.eshopUser u where u.id = :userId")
  @Cacheable(cacheNames = RepoCacheMaps.ALL_ORGANISATIONS_BY_ID,
  unless = RepoCacheMaps.RESULT_IS_NULL)
  List<Organisation> findAllByUserId(@Param("userId") Long userId);

  /**
   * Searches the organisation by id.
   *
   * @param id organisation id
   * @return the organisation
   */
  @Query(value = "select new com.sagag.eshop.repo.entity.Organisation("
      + "o.id, o.name, o.orgCode, o.parentId, o.shortname) from Organisation o "
      + "where o.id = :orgId")
  @Cacheable(cacheNames = RepoCacheMaps.ORGANISATIONS_BY_ID,
  unless = RepoCacheMaps.RESULT_IS_NULL)
  Optional<Organisation> findByOrgId(@Param("orgId") int id);

  /**
   * The condition parentId = 1 should be improved because different data may have different id.
   */
  @Query("SELECT new com.sagag.eshop.repo.entity.Organisation("
      + "organisation.name, organisation.orgCode, parent.shortname) "
      + "FROM Organisation organisation INNER JOIN organisation.parent parent "
      + "WHERE organisation.parentId = parent.id AND (:orgCode = '' OR organisation.orgCode = :orgCode) "
      + "AND (:name ='' OR organisation.name LIKE concat('%',:name,'%')) "
      + "AND (:affiliate = '' OR organisation.parentId IN (SELECT o1.id FROM Organisation o1 WHERE o1.shortname = :affiliate)) "
      + "AND organisation.parentId in (SELECT affiliate.id FROM Organisation affiliate WHERE affiliate.parentId = 1)")
  Page<Organisation> findByNameAndOrgCodeAndAffiliate(@Param("name") String name,
      @Param("orgCode") String orgCode, @Param("affiliate") String affiliate, Pageable pageable);

  @Query("select o from Organisation o where o.organisationType.name = :typeName")
  @Cacheable(cacheNames = RepoCacheMaps.ALL_ORGANISATIONS_BY_TYPE,
  unless = RepoCacheMaps.RESULT_IS_NULL)
  List<Organisation> findOrganisationsByType(@Param("typeName") String typeName);

  @Query("select case when count(o) > 0 then true else false end from Organisation o where o.orgCode =:orgCode")
  @Cacheable(cacheNames = RepoCacheMaps.EXISTINGS_BY_ORGCODE)
  boolean isExistedByOrgCode(@Param("orgCode") String orgCode);

  @Query("select o.id from Organisation o where o.orgCode =:orgCode")
  @Cacheable(cacheNames = RepoCacheMaps.ID_BY_ORGCODE, unless = RepoCacheMaps.RESULT_IS_NULL)
  Optional<Integer> findIdByOrgCode(@Param("orgCode") String orgCode);

  /**
   * The condition parentId = 1 should be improved because different data may have different id.
   */
  @Query("SELECT organisation.id from Organisation organisation "
      + "INNER JOIN organisation.parent parent "
      + "WHERE parent.shortname = :affiliate "
      + "AND parent.parentId = 1")
  List<Integer> findOrgIdInAffiliate(@Param("affiliate") String affiliate);

  /**
   * The condition parentId = 1 should be improved because different data may have different id.
   */
  @Query("SELECT parent.shortname "
      + "FROM Organisation organisation INNER JOIN organisation.parent parent "
      + "WHERE (organisation.id = :id) "
      + "AND parent.parentId = 1")
  String findAffiliateByOrgId(@Param("id") int id);

  @Query("SELECT organisation.orgCode "
      + "FROM Organisation organisation "
      + "WHERE (organisation.id = :id) ")
  String findOrgCodeById(@Param("id") int id);

  @Query("SELECT organisation.id "
      + "FROM Organisation organisation "
      + "JOIN Organisation parent ON parent.id = organisation.parentId "
      + "WHERE (parent.orgCode = :orgCode) "
      )
  List<Long> findFinalOrgIdByOrgCode(@Param("orgCode") String orgCode);

  @Query("SELECT o.id FROM Organisation o WHERE o.parentId=:parentId")
  List<Integer> findIdsByParentId(@Param("parentId") Integer parentId);

  @Query("select o.name from Organisation o where o.id = :id")
  Optional<String> findNameById(@Param("id") int id);

  @Query("select o from Organisation o where o.id in :ids")
  List<Organisation> findByIds(@Param("ids") List<Integer> ids);
}
