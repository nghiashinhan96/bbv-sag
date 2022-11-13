package com.sagag.services.copydb.repo.dest;

import com.sagag.services.copydb.domain.dest.DestMessage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DestMessageRepository extends JpaRepository<DestMessage, Long> {
}
