package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.OfferAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferAddressRepository extends JpaRepository<OfferAddress, Long> {
}
