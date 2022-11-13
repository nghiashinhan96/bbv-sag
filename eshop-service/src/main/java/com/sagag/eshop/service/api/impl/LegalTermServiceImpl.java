package com.sagag.eshop.service.api.impl;

import static com.sagag.eshop.repo.enums.DocTypeAssignedStatus.ACTIVE;
import static java.time.temporal.ChronoUnit.DAYS;

import com.sagag.eshop.repo.api.VLegalTermRepository;
import com.sagag.eshop.repo.api.legal_term.LegalDoctypeCustomerAcceptedRepository;
import com.sagag.eshop.repo.api.legal_term.LegalDoctypeRepository;
import com.sagag.eshop.repo.entity.legal_term.LegalDoctypeCustomerAccepted;
import com.sagag.eshop.service.api.LegalTermService;
import com.sagag.eshop.service.converter.VLegalTermConverters;
import com.sagag.services.domain.sag.legal_term.LegalTermDto;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

@Service
public class LegalTermServiceImpl implements LegalTermService {

  @Autowired
  private VLegalTermRepository vLegalTermRepository;

  @Autowired
  private LegalDoctypeRepository doctypeRepository;

  @Autowired
  private LegalDoctypeCustomerAcceptedRepository customerAcceptedRepository;

  @Override
  public List<LegalTermDto> getLegalTerms(Integer orgId, String language, Date firstLoginDate) {
    final List<LegalTermDto> result = getLegalTermsByLoginUser(orgId, language);
    if (CollectionUtils.isEmpty(result)) {
      return Collections.emptyList();
    }
    result.forEach(lgt -> {
      lgt.setAccepted(legalTermAccepted().test(lgt));
      if (!lgt.isAccepted()) {
        lgt.setDaysLeft(calculateDaysLeft(lgt, firstLoginDate));
      }
    });

    return result.stream().sorted(Comparator.comparing(LegalTermDto::getSort))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void acceptLegalTerm(Long termId, Integer orgId) {
    Assert.notNull(orgId, "The given customer id must not be null");
    if (!doctypeRepository.existsById(termId)) {
      throw new ValidationException("Can not found legal term");
    }
    Long customerId = (long) orgId;
    LegalDoctypeCustomerAccepted targetTerm =
        customerAcceptedRepository.findByLegalDoctypeIdAndCustomerId(termId, customerId)
        .orElseThrow(() -> new ValidationException("Can not found user's legal term"));

    //accept the term
    targetTerm.setTimeAccepted(LocalDateTime.now());
    customerAcceptedRepository.save(targetTerm);
  }

  @Override
  public boolean hasExpiredTerms(Integer orgId, String language, Date firstLoginDate) {
    Predicate<LegalTermDto> expired = term -> calculateDaysLeft(term, firstLoginDate) == 0;
    return getLegalTermsByLoginUser(orgId, language).stream().anyMatch(expired);
  }

  @Override
  public boolean isAllTermsAccepted(Integer orgId, String language) {
    List<LegalTermDto> legalTerms = getLegalTermsByLoginUser(orgId, language);
    Predicate<List<LegalTermDto>> allLegalTermAccepted =
        lgts -> lgts.stream().allMatch(legalTermAccepted());
    return CollectionUtils.isEmpty(legalTerms) || allLegalTermAccepted.test(legalTerms);
  }

  @Override
  public List<LegalTermDto> getUnacceptedTerms(Integer orgId, String language,
      Date firstLoginDate) {
    Predicate<LegalTermDto> legalTermUnAccepted = lgt -> Objects.isNull(!lgt.isAccepted());

    List<LegalTermDto> unAcceptedLegalTerms = getLegalTerms(orgId, language, firstLoginDate)
        .stream().filter(legalTermUnAccepted).collect(Collectors.toList());

    Comparator<LegalTermDto> byDaysLeft = Comparator.comparing(LegalTermDto::getDaysLeft);
    return Collections.singletonList(unAcceptedLegalTerms.stream().min(byDaysLeft).orElse(null));
  }

  private List<LegalTermDto> getLegalTermsByLoginUser(Integer orgId, String language) {
    if (Objects.isNull(orgId)) {
      return Collections.emptyList();
    }
    Long customerId = (long) orgId;
    return vLegalTermRepository.findAllByCustomerIdAndLanguageAndStatus(customerId, language, ACTIVE.getValue()).stream()
            .map(VLegalTermConverters.converter())
            .collect(Collectors.toList());
  }

  private static Predicate<LegalTermDto> legalTermAccepted() {
    return lgt -> Objects.nonNull(lgt.getTimeAccepted());
  }

  private static int calculateDaysLeft(LegalTermDto lgt, Date firstLoginDate) {
    LocalDate now = LocalDate.now();
    LocalDate firstLoginLocalDate = firstLoginDate.toInstant()
        .atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDate deadLineDate = firstLoginLocalDate.isBefore(lgt.getDateValidFrom())
        ? lgt.getDateValidFrom().plusDays(lgt.getAcceptancePeriodDays())
            : firstLoginLocalDate.plusDays(lgt.getAcceptancePeriodDays());
    return !deadLineDate.isBefore(now) ? Math.toIntExact(Math.abs(DAYS.between(now, deadLineDate)))
        : NumberUtils.INTEGER_ZERO;
  }

}
