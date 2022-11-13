package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.MessageType;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageTypeRepository extends JpaRepository<MessageType, Integer> {
}
