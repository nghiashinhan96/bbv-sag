package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.UserSettings;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Integer> {
}
