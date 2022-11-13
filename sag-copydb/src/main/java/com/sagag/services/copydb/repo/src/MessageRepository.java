package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.Message;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
