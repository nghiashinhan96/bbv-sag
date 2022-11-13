package com.sagag.services.tools.batch.sales.registration;

import com.sagag.services.tools.domain.target.EshopUser;
import com.sagag.services.tools.domain.target.Language;
import com.sagag.services.tools.domain.target.Salutation;
import com.sagag.services.tools.repository.target.LanguageRepository;
import com.sagag.services.tools.repository.target.SalutationRepository;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@StepScope
public class CreateSalesUserItemProcessor implements ItemProcessor<Integer, EshopUser> {

  public static final String SALUTATION_OTHER = "SALUTATION_OTHER";

  private static final String DEFAULT_LANG = "DE";

  @Value("${sales.pattern.emailPrefix:TST_}")
  private String prefixPattern;

  @Value("${sales.pattern.emailAddress:@sag-ag.ch}")
  private String emailAddress;

  @Value("${sales.pattern.userNamePrefix:sales}")
  private String userNamePrefix;

  private static final String FIRST_NAME = "Test";

  @Autowired
  private LanguageRepository languageRepository;

  @Autowired
  private SalutationRepository salutationRepo;

  @Override
  @Transactional(readOnly = true)
  public EshopUser process(Integer item) throws Exception {
    final String userName = item < 10 ? userNamePrefix + "0" + item : userNamePrefix + item;
    final Salutation salutation = salutationRepo.findOneByCode(SALUTATION_OTHER)
        .orElseThrow(() -> new IllegalArgumentException("Invalid salutation."));
    final Language language = languageRepository.findOneByLangiso(DEFAULT_LANG)
        .orElseThrow(() -> new IllegalArgumentException("Invalid languages."));
    return EshopUser.builder()
        .salutation(salutation)
        .firstName(FIRST_NAME)
        .lastName(userName)
        .email(prefixPattern + item + emailAddress)
        .username(userName)
        .language(language)
        .emailConfirmation(false)
        .newsletter(false)
        .vatConfirm(true)
        .build();
  }

}
