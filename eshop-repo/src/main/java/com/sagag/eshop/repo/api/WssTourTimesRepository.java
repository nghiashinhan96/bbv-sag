package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.WssTourTimes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WssTourTimesRepository
    extends JpaRepository<WssTourTimes, Integer>, JpaSpecificationExecutor<WssTourTimes> {

}
