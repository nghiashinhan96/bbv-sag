package com.sagag.eshop.repo.api.client;

import com.sagag.eshop.repo.entity.client.EshopClient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EshopClientRepository extends JpaRepository<EshopClient, Integer> {

  Optional<EshopClient> findByClientName(String clientName);

}
