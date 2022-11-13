package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.AllocationType;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AllocationTypeRepository extends JpaRepository<AllocationType, Integer> {
}
