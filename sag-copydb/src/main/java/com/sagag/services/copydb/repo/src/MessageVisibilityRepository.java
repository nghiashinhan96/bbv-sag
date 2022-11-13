package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.MessageVisibility;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageVisibilityRepository extends JpaRepository<MessageVisibility, Integer> {
}
