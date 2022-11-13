package com.sagag.services.tools.repository.source;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceOrganisationProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@OracleProfile
public interface SourceOrganisationPropertyRepository extends JpaRepository<SourceOrganisationProperty, Long> {

  @Query("select op from SourceOrganisationProperty op where op.sourceOrganisationPropertyId.organisationId = :organisationId and op.sourceOrganisationPropertyId.type like '%offer%'")
  List<SourceOrganisationProperty> findOfferPropByOrganisationId(@Param("organisationId") Long organisationId);
}
