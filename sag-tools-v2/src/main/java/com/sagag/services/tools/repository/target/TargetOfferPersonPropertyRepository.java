package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.TargetOfferPersonProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetOfferPersonPropertyRepository extends JpaRepository<TargetOfferPersonProperty, Long> {
}
