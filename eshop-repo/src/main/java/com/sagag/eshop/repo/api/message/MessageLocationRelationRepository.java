package com.sagag.eshop.repo.api.message;

import com.sagag.eshop.repo.entity.message.Message;
import com.sagag.eshop.repo.entity.message.MessageLocationRelation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageLocationRelationRepository extends JpaRepository<MessageLocationRelation, Integer> {

  List<MessageLocationRelation> deleteByMessage(Message message);
}
