package com.sagag.eshop.repo.api.client;

import com.sagag.eshop.repo.entity.client.EshopClientResource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EshopClientResourceRepository extends JpaRepository<EshopClientResource, Integer> {

  @Query("select r.id from EshopClientResource r where r.name = :name")
  Optional<Integer> findIdByResourceName(@Param("name") String name);

}
