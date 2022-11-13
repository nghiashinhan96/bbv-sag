package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.OrganisationCollection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrganisationCollectionRepository extends JpaRepository<OrganisationCollection, Integer> {

  Optional<OrganisationCollection> findByName(String name);

  Optional<OrganisationCollection> findByShortname(String shortname);

  @Query("select col.id from OrganisationCollection col join Organisation org on col.affiliateId = org.id where org.shortname=:shortname")
  List<Integer> findCollectionIdsByAffiliateShortname(@Param("shortname") String shortname);
}
