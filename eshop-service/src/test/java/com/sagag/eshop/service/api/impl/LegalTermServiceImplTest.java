package com.sagag.eshop.service.api.impl;

import static com.sagag.eshop.repo.enums.DocTypeAssignedStatus.ACTIVE;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.LoginRepository;
import com.sagag.eshop.repo.api.legal_term.LegalDoctypeCustomerAcceptedRepository;
import com.sagag.eshop.repo.api.legal_term.LegalDoctypeRepository;
import com.sagag.eshop.repo.api.legal_term.LegalDocumentRepository;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.legal_term.LegalDoctypeCustomerAccepted;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.sag.legal_term.LegalTermDto;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.ValidationException;

@RunWith(SpringRunner.class)
public class LegalTermServiceImplTest {

  private static final String DOC_TYPE_1 = "DOC_1";

  @InjectMocks
  private LegalTermServiceImpl service;

  @Mock
  private LegalDocumentRepository legalDocumentRepository;

  @Mock
  private LegalDoctypeRepository doctypeRepository;

  @Mock
  private LegalDoctypeCustomerAcceptedRepository customerAcceptedRepository;

  @Mock
  LoginRepository loginRepository;

  @Mock
  private OrganisationService organisationService;

  private Long customerId;
  private String language;
  private UserInfo userInfo;
  private Organisation customer;

  @Captor
  private ArgumentCaptor<Login> loginUser;

  @Captor
  private ArgumentCaptor<LegalDoctypeCustomerAccepted> term;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    customerId = 1l;
    language = "DE";
    userInfo = mock(UserInfo.class);
    customer = mock(Organisation.class);
    when(userInfo.getOrganisationId()).thenReturn(Math.toIntExact(customerId));
    when(customer.getId()).thenReturn(Math.toIntExact(customerId));
    when(userInfo.getLanguage()).thenReturn(language);
    when(userInfo.getFirstLoginDate()).thenReturn(new Date(0));
  }

  @Test
  public void givenNonAcceptedLegalTermShouldGetLegalTerms() {
    // given
    LocalDate now = LocalDate.now();
    final int acceptancePeriodDays = 30;
    final int daysPassed = 12;
    final int daysLeft = acceptancePeriodDays - daysPassed;
    final List<Map<String, Object>> savedLegalTerms = new ArrayList<>();

    Map<String, Object> data =
        generateData(DOC_TYPE_1, null, now.minusDays(daysPassed), acceptancePeriodDays);
    savedLegalTerms.add(data);

    when(legalDocumentRepository.findByCustomerIdAndLanguageAndStatus(anyLong(), any(), anyInt()))
    .thenReturn(savedLegalTerms);

    when(organisationService.findOrganisationByOrgCode(any())).thenReturn(Optional.of(customer));

    // when
    List<LegalTermDto> result = service.getLegalTerms(
        userInfo.getOrganisationId(), userInfo.getLanguage(), userInfo.getFirstLoginDate());

    // then
    LegalTermDto expected = result.get(0);
    assertThat(expected.getDaysLeft(), Matchers.equalTo(daysLeft));
    assertThat(expected.isAccepted(), Matchers.is(false));
  }

  @Test
  public void givenAcceptedLegalTermShouldGetLegalTerms() {
    // given
    LocalDate now = LocalDate.now();
    final int acceptancePeriodDays = 30;
    final List<Map<String, Object>> savedLegalTerms = new ArrayList<>();
    Map<String, Object> data =
        generateData(DOC_TYPE_1, LocalDateTime.now(), now, acceptancePeriodDays);
    savedLegalTerms.add(data);

    when(legalDocumentRepository.findByCustomerIdAndLanguageAndStatus(anyLong(), any(), anyInt()))
    .thenReturn(savedLegalTerms);

    when(organisationService.findOrganisationByOrgCode(any())).thenReturn(Optional.of(customer));

    // when
    List<LegalTermDto> result = service.getLegalTerms(
        userInfo.getOrganisationId(), userInfo.getLanguage(), userInfo.getFirstLoginDate());
    // then
    LegalTermDto expected = result.get(0);
    assertThat(expected.getDaysLeft(), Matchers.nullValue());
    assertTrue(expected.isAccepted());
  }

  @Test(expected = ValidationException.class)
  public void givenNonExistTermShouldThrowEx() {
    // given
    final Long termId = 1l;
    // when
    service.acceptLegalTerm(termId, customerId.intValue());
  }

  @Test
  public void shouldAcceptLegalTermSuccessfully() {
    // given
    final Long termId = 1l;
    final LegalDoctypeCustomerAccepted targetTerm = new LegalDoctypeCustomerAccepted();
    final Login login = new Login();
    when(doctypeRepository.existsById(termId)).thenReturn(true);
    when(customerAcceptedRepository.findByLegalDoctypeIdAndCustomerId(termId, customerId))
        .thenReturn(Optional.of(targetTerm));
    when(loginRepository.findByUserId(customerId)).thenReturn(Optional.of(login));
    when(organisationService.findOrganisationByOrgCode(any())).thenReturn(Optional.of(customer));
    // when
    service.acceptLegalTerm(termId, customerId.intValue());
    // then
    verify(customerAcceptedRepository).save(term.capture());
    assertNotNull(term.getValue().getTimeAccepted());
  }

  @Test
  public void shouldAcceptLegalTermAndUpdateLegalAccepted() {
    // given
    final Long termId = 1l;
    final LegalDoctypeCustomerAccepted targetTerm = mock(LegalDoctypeCustomerAccepted.class);

    when(doctypeRepository.existsById(termId)).thenReturn(true);
    when(customerAcceptedRepository.findByLegalDoctypeIdAndCustomerId(termId, customerId))
        .thenReturn(Optional.of(targetTerm));
    when(organisationService.findOrganisationByOrgCode(any())).thenReturn(Optional.of(customer));
    LocalDate now = LocalDate.now();
    final int acceptancePeriodDays = 30;
    final List<Map<String, Object>> savedLegalTerms = new ArrayList<>();
    Map<String, Object> data =
        generateData(DOC_TYPE_1, LocalDateTime.now(), now, acceptancePeriodDays);
    savedLegalTerms.add(data);
    when(legalDocumentRepository.findByCustomerIdAndLanguageAndStatus(customerId, language,
        ACTIVE.getValue())).thenReturn(savedLegalTerms);
    // when
    service.acceptLegalTerm(termId, customerId.intValue());
    // then
    verify(customerAcceptedRepository).save(targetTerm);
  }

  private Map<String, Object> generateData(String docName, LocalDateTime timeAccepted,
      LocalDate dateValidFrom, int acceptancePeriodDays) {
    Map<String, Object> result = new HashMap<>();
    result.put("NAME", docName);
    result.put("TIME_ACCEPTED", timeAccepted);
    result.put("DATE_VALID_FROM", dateValidFrom);
    result.put("ACCEPTANCE_PERIOD_DAYS", acceptancePeriodDays);
    return result;
  }

}
