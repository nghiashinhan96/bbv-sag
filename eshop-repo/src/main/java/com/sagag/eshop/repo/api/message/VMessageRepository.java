package com.sagag.eshop.repo.api.message;

import com.sagag.eshop.repo.entity.message.VMessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VMessageRepository extends JpaRepository<VMessage, Long>,
    JpaSpecificationExecutor<VMessage> {
}
