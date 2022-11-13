package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.EshopUser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EshopUserRepository extends JpaRepository<EshopUser, Long> {
}
