package com.sagag.eshop.repo.api.offer;

import com.sagag.eshop.repo.entity.offer.ViewOfferPerson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ViewOfferPersonRepository extends JpaRepository<ViewOfferPerson, Long>,
    JpaSpecificationExecutor<ViewOfferPerson> {
}
