package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.TargetOfferPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetOfferPositionRepository extends JpaRepository<TargetOfferPosition, Long> {

  @Query(value = "select count(p.id) from TargetOfferPosition p " +
      "where p.type = 'VENDORARTICLE' and p.connectVehicleId = null")
    long countOfferPositionsNotConvertVehicleId();

  @Query(value = "select count(p.id) from TargetOfferPosition p " +
		  "where (p.type = 'VENDORARTICLE' or p.type = 'VENDORARTICLEWITHOUTVEHICLE') and p.sagsysId = null")
    long countOfferPositionsNotConvertToIdSagsys();
}
