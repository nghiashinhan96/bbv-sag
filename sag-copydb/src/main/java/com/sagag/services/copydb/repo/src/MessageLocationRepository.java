package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.MessageLocation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageLocationRepository extends JpaRepository<MessageLocation, Integer> {
}
