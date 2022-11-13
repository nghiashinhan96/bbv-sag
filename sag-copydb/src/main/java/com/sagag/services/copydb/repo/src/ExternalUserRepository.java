package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.ExternalUser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalUserRepository extends JpaRepository<ExternalUser, Long> {
}
