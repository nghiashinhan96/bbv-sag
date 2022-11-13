package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.ExternalOrganisation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalOrganisationRepository extends JpaRepository<ExternalOrganisation, Long> {
}
