package com.sagag.services.tools.repository.sagsys;

import com.sagag.services.tools.config.SagsysProfile;
import com.sagag.services.tools.domain.sagsys.SourceUserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@SagsysProfile
public interface SourceUserSettingsRepository extends JpaRepository<SourceUserSettings, Integer> {

}
