package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.AddressType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AddressTypeRepository extends JpaRepository<AddressType, Integer> {

  Optional<AddressType> findAddressByType(@Param("type") String type);

}
