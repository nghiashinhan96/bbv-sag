package com.sagag.eshop.repo.api.offer;

import com.sagag.eshop.repo.entity.offer.ViewOffer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ViewOfferRepository
    extends JpaRepository<ViewOffer, Long>, JpaSpecificationExecutor<ViewOffer> {

}
