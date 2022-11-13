package com.sagag.services.tools.repository.source;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourcePersonProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@OracleProfile
public interface SourcePersonPropertyRepository extends JpaRepository<SourcePersonProperty, Long> {

  @Query("select pp from SourcePersonProperty pp where pp.sourcePersonPropertyId.personId = :personId")
  List<SourcePersonProperty> findByPersonId(@Param("personId") Long personId);

}
