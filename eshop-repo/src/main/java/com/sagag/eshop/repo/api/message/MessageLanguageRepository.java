package com.sagag.eshop.repo.api.message;

import com.sagag.eshop.repo.entity.message.MessageLanguage;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interfacing for {@link MessageLanguage}.
 *
 */
public interface MessageLanguageRepository extends JpaRepository<MessageLanguage, Long> {

}
