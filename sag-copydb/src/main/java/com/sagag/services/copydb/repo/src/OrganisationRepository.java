package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.Organisation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganisationRepository extends JpaRepository<Organisation, Integer> {
}
