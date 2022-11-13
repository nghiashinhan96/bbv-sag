package com.sagag.services.tools.repository.target;


import com.sagag.services.tools.domain.target.LicenseSettings;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LicenseSettingsRepository extends JpaRepository<LicenseSettings, Integer> {

  Optional<LicenseSettings> findOneById(long id);

  Optional<LicenseSettings> findOneByPackId(long packId);

  Optional<LicenseSettings> findOneByPackArticleId(long packArticleId);

  Optional<LicenseSettings> findOneByPackName(String packName);
}
