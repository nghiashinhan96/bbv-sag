package com.sagag.services.tools.repository.source;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Repository
@OracleProfile
public interface SourceOfferRepository extends JpaRepository<SourceOffer, Long> {

  long countByVendorIdIn(List<Long> vendorIds);

  Page<SourceOffer> findByVendorIdIn(List<Long> vendorIds, Pageable pageable);

  List<SourceOffer> findByVendorId(Long vendorId);

}
