package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.AddressType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AddressTypeRepository extends JpaRepository<AddressType, Integer> {

  Optional<AddressType> findAddressByType(@Param("type") String type);

}
