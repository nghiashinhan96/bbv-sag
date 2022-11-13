package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.Language;
import com.sagag.services.domain.eshop.dto.LanguageDto;

import java.util.List;
import java.util.Optional;

public interface LanguageService {
  /**
   * Return All {@link LanguageDto}
   *
   * @return
   */
  public List<LanguageDto> getAllLanguage();

  /**
   * find one by Id
   * @param id
   * @return
   */
  public Language getOneById(int id);

  /**
   * Finds one by languageIso.
   *
   * @param languageIso
   * @return Optional of {Language}
   */
  Optional<Language> findOneByLangiso(String languageIso);

  /**
   * Finds language by langiso or tecdoc
   * @param language
   * @return
   */
  Language findLanguageByLangIsoOrTecDoc(String langIsoOrTecDoc);
}
