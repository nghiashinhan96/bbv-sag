package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.Language;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Integration test class for {@link LanguageRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class LanguageRepositoryIT {

  private static final String LANGISO_DE = "DE";
  private static final String LANGISO_IT = "IT";

  @Autowired
  private LanguageRepository languageRepo;

  @Test
  public void testFindByLangiso() {
    final Optional<Language> language = languageRepo.findOneByLangiso(LANGISO_IT);
    Assert.assertThat(LANGISO_IT, Is.is(language.get().getLangiso()));
  }

  @Test
  public void testFindDefault() {
    final Language defaultLang = languageRepo.findDefaultLanguage(LANGISO_DE);
    Assert.assertThat(LANGISO_DE, Is.is(defaultLang.getLangiso()));
  }

}
