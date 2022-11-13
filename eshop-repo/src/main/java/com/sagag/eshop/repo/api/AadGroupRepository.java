package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.AadGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AadGroupRepository extends JpaRepository<AadGroup, Long> {


  @Query(value = "select gr from AadGroup gr where gr.uuid in:uuids")
  Optional<List<AadGroup>> findAllByUuids(@Param("uuids") List<String> uuids);
}
