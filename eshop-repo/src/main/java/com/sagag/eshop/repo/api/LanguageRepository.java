package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.Language;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Integer> {

  Optional<Language> findOneByLangiso(String languageIso);

  Optional<Language> findOneByTecDoc(Integer tecDoc);

  @Query("select l from Language l where l.langiso = :langIso")
  Language findDefaultLanguage(@Param("langIso") String langIso);
}
