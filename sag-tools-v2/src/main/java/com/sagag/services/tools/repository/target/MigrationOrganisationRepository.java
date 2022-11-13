package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.MigrationOrganisation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MigrationOrganisationRepository extends JpaRepository<MigrationOrganisation, Long> {

  @Override
  List<MigrationOrganisation> findAll();

  Optional<MigrationOrganisation> findOneByOrgId(Long orgCode);

}
