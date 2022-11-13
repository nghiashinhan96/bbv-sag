package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.Organisation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, Integer> {

	@Query("select p.id from Organisation p")
	List<Integer> findOrganisationIdList();

	Optional<Organisation> findOneById(final int orgId);

  Optional<Organisation> findOrganisationByOrgCode(String customerNr);

  Optional<Organisation> findOneByOrgCode(String orgCode);

  @Query("select o.id from Organisation o where o.shortname =:shortname")
  Optional<Integer> findIdByShortName(@Param("shortname") String shortname);

  @Query("select o.orgCode from Organisation o where o.orgCode in (:orgCodes)")
  List<String> findExistedOrgCodeByOrgCodes(@Param("orgCodes") List<String> orgCodes);

}
