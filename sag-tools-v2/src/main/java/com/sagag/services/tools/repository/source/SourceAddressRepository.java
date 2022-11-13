package com.sagag.services.tools.repository.source;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@OracleProfile
public interface SourceAddressRepository extends JpaRepository<SourceAddress, Long> {

  @Query(value = "select * from SHOP.ADDRESS sa "
  + "inner join SHOP.ORGANISATION_ADDRESS oa on oa.ADDRESS_ID = sa.ID "
  + "inner join SHOP.ORGANISATIONLINK ol on ol.CLIENT_ID = oa.ORGANISATION_ID "
  + "inner join SHOP.LOGIN_ROLEASSIGNMENT ra on ra.ORGANISATION_ID = ol.CLIENT_ID "
  + "where ra.PERSON_ID = :personId", nativeQuery = true)
  List<SourceAddress> findAddressByByPersonId(@Param("personId") Long personId);

}
