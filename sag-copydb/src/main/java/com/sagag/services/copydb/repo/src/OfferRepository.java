package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.Offer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {
}
