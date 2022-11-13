package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.EshopAddress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EshopAddressRepository extends JpaRepository<EshopAddress, Integer> {

  @Query("SELECT add.zipcode "
      + "FROM EshopAddress add INNER JOIN add.organisationAddresses ogadd INNER JOIN ogadd.organisation og "
      + "ON og.orgCode = :customerNumber "
      + "INNER JOIN add.addressType t "
      + "ON t.type='DEFAULT'")
  List<String> findZipsByCustomerNr(@Param("customerNumber") String customerNumber);
}
