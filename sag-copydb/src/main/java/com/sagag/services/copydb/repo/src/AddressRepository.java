package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.Address;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
