package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.EshopCartItem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EshopCartItemRepository extends JpaRepository<EshopCartItem, Long> {
}
