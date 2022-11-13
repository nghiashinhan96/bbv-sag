package com.sagag.eshop.repo.api.collection;

import com.sagag.eshop.repo.entity.VCollectionSearch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VCollectionRepository extends JpaRepository<VCollectionSearch, Long>,
    JpaSpecificationExecutor<VCollectionSearch> {
}
