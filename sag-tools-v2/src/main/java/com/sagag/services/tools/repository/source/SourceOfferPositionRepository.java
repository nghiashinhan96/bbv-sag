package com.sagag.services.tools.repository.source;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceOfferPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@OracleProfile
public interface SourceOfferPositionRepository extends JpaRepository<SourceOfferPosition, Long> {

  List<SourceOfferPosition> findByOfferIdIn(List<Long> offerIds);

}
