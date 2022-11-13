package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.LicenseSettings;
import com.sagag.services.domain.eshop.dto.LicenseSettingsDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LicenseSettingsRepository extends JpaRepository<LicenseSettings, Integer> {

  Optional<LicenseSettings> findOneById(long id);

  Optional<LicenseSettings> findOneByPackId(long packId);

  Optional<LicenseSettings> findOneByPackArticleId(long packArticleId);

  Optional<LicenseSettings> findOneByPackName(String packName);

  /**
   * Returns all available Licenses on type.
   *
   * @param typeOfLicense license type
   * @return a list of license settings
   */
  @Query("SELECT new com.sagag.services.domain.eshop.dto.LicenseSettingsDto(l.id, l.packId, l.packName) "
      + "FROM LicenseSettings l WHERE l.typeOfLicense = :typeOfLicense")
  List<LicenseSettingsDto> searchLicenses(@Param("typeOfLicense") String typeOfLicense);
}
