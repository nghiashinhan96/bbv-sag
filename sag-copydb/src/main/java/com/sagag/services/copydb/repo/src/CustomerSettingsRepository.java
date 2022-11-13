package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.CustomerSettings;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerSettingsRepository extends JpaRepository<CustomerSettings, Integer> {
}
