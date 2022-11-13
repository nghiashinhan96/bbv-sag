package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.AllocationType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interfacing for {@link AllocationType}.
 */
public interface AllocationTypeRepository extends JpaRepository<AllocationType, Integer> {

  Optional<AllocationType> findOneById(int id);

}
