package com.sagag.services.tools.batch.offer_feature.offer_person;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourcePerson;
import com.sagag.services.tools.domain.target.TargetOfferPerson;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.repository.source.SourceOrganisationRepository;
import com.sagag.services.tools.repository.source.SourcePersonRepository;
import com.sagag.services.tools.repository.target.TargetOfferPersonRepository;
import com.sagag.services.tools.support.CommonInitialResource;
import com.sagag.services.tools.utils.CsvUtils;
import com.sagag.services.tools.utils.DefaultUtils;
import com.sagag.services.tools.utils.EntityManagerUtils;
import com.sagag.services.tools.utils.EnvUtils;
import com.sagag.services.tools.utils.ToolConstants;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.hibernate.internal.SessionImpl;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 */
@Component
@OracleProfile
@Slf4j
public class MigratePersonFromOrclTasklet extends AbstractTasklet {

  private static final String TABLE = "OFFER_PERSON";

  private static final String NOT_MIGRATED_FILE_PATH =
    SystemUtils.getUserDir() + "/csv/person_not_migrated_data.csv";

  @Autowired(required = false)
  private SourcePersonRepository sourcePersonRepo;

  @Autowired(required = false)
  private SourceOrganisationRepository sourceOrganisationRepo;

  @Autowired
  private TargetOfferPersonRepository targetOfferPersonRepo;

  @Autowired
  protected CommonInitialResource commonInitialResource;

  @Override
  public RepeatStatus execute(StepContribution contribution,
    ChunkContext chunkContext) throws BatchJobException {

    // Update custom config to set exactly id
    SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    EntityManagerUtils.turnOnIdentityInsert(session, TABLE);

    final long totalRecords = sourcePersonRepo.countByType(OfferPersonType.ADDRESSEE.name());
    final int size = ToolConstants.MAX_SIZE;
    final int totalPages = (int) (totalRecords / size) + 1;

    final List<SourcePerson> notMigratedList = new ArrayList<>();
    for (int curPage = 0; curPage < totalPages; curPage++) {
      Page<SourcePerson> page =
        sourcePersonRepo.findByType(OfferPersonType.ADDRESSEE.name(), PageRequest.of(curPage, size));
      if (!page.hasContent()) {
        continue;
      }

      List<TargetOfferPerson> people = page.getContent().stream()
        .distinct()
        .peek(source -> log.debug("Source person = {}", source))
        .map(person -> {
          final Long personId = person.getId();

          // Just map real id with prod env
          Long createdUserId = defaultUserId;
          Long modifiedUserId = null;
          Integer langId = commonInitialResource.getLanguageId(person.getLangIso());

          String finalCustomerCompanyName = StringUtils.EMPTY;
          Optional<String> finalCustomerNameOpt =
            sourceOrganisationRepo.findFinalCustomerNameByPersonId(personId);
          if (finalCustomerNameOpt.isPresent()) {
            finalCustomerCompanyName = finalCustomerNameOpt.get();
          }

          if (!EnvUtils.isProductionEnv(activeProfile)) {
            return mapSourceToTarget(person, defaultOrgId, createdUserId, modifiedUserId, langId, finalCustomerCompanyName);
          }

          Optional<BigDecimal> vendorIdOpt = sourceOrganisationRepo.findVendorIdByPersonId(personId);
          Integer orgId = null;
          if (vendorIdOpt.isPresent()) {
            orgId = commonInitialResource.getOrgId(vendorIdOpt.get().longValueExact());
          }
          if (orgId == null) {
            notMigratedList.add(person);
            return null;
          }

          return mapSourceToTarget(person, orgId, createdUserId, modifiedUserId, langId, finalCustomerCompanyName);
        })
        .filter(Objects::nonNull)
        .peek(target -> log.debug("Target person = {}", target))
        .collect(Collectors.toList());

      targetOfferPersonRepo.saveAll(people);
    }

    // Write the not migrated person records
    CsvUtils.write(NOT_MIGRATED_FILE_PATH, notMigratedList);

    return finish(contribution);
  }

  private TargetOfferPerson mapSourceToTarget(SourcePerson source,
    Integer orgId, Long createdUserId, Long modifiedUserId, Integer langId, String companyName) {
    TargetOfferPerson target = new TargetOfferPerson();
    target.setId(source.getId());
    target.setType(source.getType());
    target.setStatus(source.getStatus());
    target.setFirstName(DefaultUtils.toUtf8Value(source.getFirstName()));
    target.setLastName(DefaultUtils.toUtf8Value(source.getLastName()));
    target.setEmail(DefaultUtils.toUtf8Value(source.getEmail()));
    target.setCreatedDate(source.getDateCreated());
    target.setModifiedDate(source.getDateModified());
    target.setVersion(source.getVersion());
    target.setTecstate(source.getTecstate());
    target.setOfferCompanyName(StringUtils.defaultIfEmpty(companyName, null));
    target.setCreatedUserId(createdUserId);
    target.setModifiedUserId(modifiedUserId);
    target.setLanguageId(langId);
    target.setOrganisationId(orgId);
    return target;
  }
}
