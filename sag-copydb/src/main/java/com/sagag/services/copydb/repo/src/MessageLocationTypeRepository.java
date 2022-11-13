package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.MessageLocationType;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageLocationTypeRepository extends JpaRepository<MessageLocationType, Integer> {
}
