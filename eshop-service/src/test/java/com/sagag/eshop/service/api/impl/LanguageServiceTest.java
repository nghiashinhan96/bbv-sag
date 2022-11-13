package com.sagag.eshop.service.api.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.LanguageRepository;
import com.sagag.eshop.repo.entity.Language;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

/**
 * Unit test class for Language service.
 */
@EshopMockitoJUnitRunner
public class LanguageServiceTest {

  @InjectMocks
  LanguageServiceIml languageService;

  @Mock
  private LanguageRepository languageRepo;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void givenLangiso_ShouldGetLanguage() throws Exception {
    final String langiso = "de";
    when(languageRepo.findOneByLangiso(langiso))
        .thenReturn(Optional.of(new Language()));

    Optional<Language> LanguageOpt = languageService.findOneByLangiso(langiso);
    Assert.assertTrue(LanguageOpt.isPresent());
    verify(languageRepo).findOneByLangiso(langiso);
  }

  @Test
  public void givenEmptyLangIso_ShouldNotGetLanguage() throws Exception {
    final String langiso = StringUtils.EMPTY;

    Optional<Language> LanguageOpt = languageService.findOneByLangiso(langiso);
    Assert.assertFalse(LanguageOpt.isPresent());
  }
}
