package com.sagag.eshop.repo.api;


import com.sagag.eshop.repo.entity.OrganisationProperty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrganisationPropertyRepository extends JpaRepository<OrganisationProperty, Long> {

  List<OrganisationProperty> findByOrganisationId(@Param("organisationId") final Long organisationId);

}

