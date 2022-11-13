package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.License;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseRepository extends JpaRepository<License, Long> {
}
