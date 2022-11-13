package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.Language;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {

  Optional<Language> findOneByLangiso(String languageIso);

  @Query("select l.id from Language l where l.langiso = :langIso")
  Integer findIdByLangiso(@Param("langIso") String langIso);
}
