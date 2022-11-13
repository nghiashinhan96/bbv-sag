package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.VDeliveryProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VDeliveryProfileRepository
    extends JpaRepository<VDeliveryProfile, Integer>, JpaSpecificationExecutor<VDeliveryProfile> {

}
