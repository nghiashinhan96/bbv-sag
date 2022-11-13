package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.VFinalCustomer;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VFinalCustomerRepository
    extends CrudRepository<VFinalCustomer, Integer>, JpaSpecificationExecutor<VFinalCustomer> {

  Optional<VFinalCustomer> findByOrgId(Integer orgId);

}
