package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.LanguageRepository;
import com.sagag.eshop.repo.entity.Language;
import com.sagag.eshop.service.api.LanguageService;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.domain.eshop.dto.LanguageDto;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class LanguageServiceIml implements LanguageService {

  @Autowired
  private LanguageRepository languageRepo;

  @Autowired
  private LocaleContextHelper localeContextHelper;

  @Override
  public List<LanguageDto> getAllLanguage() {
    return languageRepo.findAll().stream()
        .map(language -> LanguageDto.builder()
            .id(language.getId())
            .langcode(language.getLangcode())
            .langiso(language.getLangiso())
            .description(language.getDescription()).build())
        .collect(Collectors.toList());
  }

  @Override
  public Language getOneById(int id) {
    return languageRepo.findById(id).orElse(null);
  }

  @Override
  public Optional<Language> findOneByLangiso(String languageIso) {
    if (StringUtils.isBlank(languageIso)) {
      return Optional.empty();
    }
    return languageRepo.findOneByLangiso(languageIso);
  }

  @Override
  public Language findLanguageByLangIsoOrTecDoc(String langIso) {
    final Supplier<Language> defaultLanguageSupplier = () -> languageRepo.findDefaultLanguage(
        StringUtils.upperCase(localeContextHelper.defaultAppLocaleLanguage()));
    if (StringUtils.isBlank(langIso)) {
      return defaultLanguageSupplier.get();
    }

    if (NumberUtils.isDigits(langIso)) {
      return languageRepo.findOneByTecDoc(Integer.valueOf(langIso))
          .orElseGet(defaultLanguageSupplier);
    }

    return languageRepo.findOneByLangiso(langIso).orElseGet(defaultLanguageSupplier);
  }
}
