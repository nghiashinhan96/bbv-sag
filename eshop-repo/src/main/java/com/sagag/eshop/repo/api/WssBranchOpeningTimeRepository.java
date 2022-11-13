package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.WssBranch;
import com.sagag.eshop.repo.entity.WssBranchOpeningTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WssBranchOpeningTimeRepository
    extends JpaRepository<WssBranchOpeningTime, Integer>, JpaSpecificationExecutor<WssBranchOpeningTime> {

	void deleteAllByWssBranch(WssBranch wssBranch);
}
