package com.sagag.eshop.repo.api.client;

import com.sagag.eshop.repo.entity.client.VEshopClient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VEshopClientRepository extends JpaRepository<VEshopClient, Integer> {

  Optional<VEshopClient> findByClientName(String clientName);

}
