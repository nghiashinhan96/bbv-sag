package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.VActiveUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EshopUserViewRepository extends JpaRepository<VActiveUser, Long>,
    JpaSpecificationExecutor<VActiveUser> {

}
