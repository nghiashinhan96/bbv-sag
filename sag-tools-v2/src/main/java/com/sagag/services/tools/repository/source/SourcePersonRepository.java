package com.sagag.services.tools.repository.source;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourcePerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@OracleProfile
public interface SourcePersonRepository extends JpaRepository<SourcePerson, Long> {

  long countByType(String type);

  Page<SourcePerson> findByType(String type, Pageable pageable);

}
