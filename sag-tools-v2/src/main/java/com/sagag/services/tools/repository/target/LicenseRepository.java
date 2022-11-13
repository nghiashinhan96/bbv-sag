package com.sagag.services.tools.repository.target;


import com.sagag.services.tools.domain.target.License;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseRepository extends JpaRepository<License, Integer> {
}
