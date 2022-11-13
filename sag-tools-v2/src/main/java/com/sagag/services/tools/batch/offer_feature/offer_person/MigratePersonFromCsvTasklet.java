package com.sagag.services.tools.batch.offer_feature.offer_person;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.csv.CsvFinalCustomer;
import com.sagag.services.tools.domain.csv.CsvOrganisationLink;
import com.sagag.services.tools.domain.csv.CsvPerson;
import com.sagag.services.tools.domain.csv.CsvRoleAssignment;
import com.sagag.services.tools.domain.target.TargetOfferPerson;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.repository.target.TargetOfferPersonRepository;
import com.sagag.services.tools.support.CommonInitialDataLoader;
import com.sagag.services.tools.support.CommonInitialResource;
import com.sagag.services.tools.utils.CsvUtils;
import com.sagag.services.tools.utils.DefaultUtils;
import com.sagag.services.tools.utils.EntityManagerUtils;
import com.sagag.services.tools.utils.EnvUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.hibernate.internal.SessionImpl;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Component
@Slf4j
@OracleProfile
public class MigratePersonFromCsvTasklet extends AbstractTasklet {

  private static final String TABLE = "OFFER_PERSON";

  private static final String NOT_MIGRATED_FILE_PATH =
    SystemUtils.getUserDir() + "/csv/person_not_migrated_data.csv";

  @Value("${dirPath:/csv/person}")
  private String dirPath;

  @Autowired
  private TargetOfferPersonRepository targetOfferPersonRepo;

  @Autowired
  protected CommonInitialDataLoader initialDataLoader;

  @Autowired
  protected CommonInitialResource commonInitialResource;

  @Override
  public RepeatStatus execute(StepContribution contribution,
    ChunkContext chunkContext) throws BatchJobException {

    final File personDir = new File(SystemUtils.getUserDir() + dirPath);
    if (!personDir.isDirectory()) {
      throw new IllegalArgumentException("The current path is not directory");
    }

    // Update custom config to set exactly id
    SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    EntityManagerUtils.turnOnIdentityInsert(session, TABLE);

    final Map<Long, CsvFinalCustomer> finalCustomerMap;
    final Map<Long, CsvOrganisationLink> orgLinks;
    final Map<Long, CsvRoleAssignment> roles;
    final Map<Long, Integer> organisationIdMap = commonInitialResource.getOrganisationIdMap();
    if (EnvUtils.isProductionEnv(activeProfile)) {
      finalCustomerMap = initialDataLoader.loadFinalCustomers();
      orgLinks = initialDataLoader.loadOrgLinks();
      roles = initialDataLoader.loadRoleAssignment();
    } else {
      finalCustomerMap = new HashMap<>();
      orgLinks = new HashMap<>();
      roles = new HashMap<>();
    }

    final List<CsvPerson> notMigratedList = new ArrayList<>();
    File[] csvFiles = personDir.listFiles();
    for (File file : csvFiles) {
      Assert.notNull(file, "The given file must not be null");
      final List<CsvPerson> personList = CsvUtils.read(file, CsvPerson.class);
      if (CollectionUtils.isEmpty(personList)) {
        continue;
      }

      final List<TargetOfferPerson> targetList = new ArrayList<>();
      personList.stream()
        .filter(item -> OfferPersonType.ADDRESSEE.name().equalsIgnoreCase(item.getType()))
        .forEach(person -> {
          log.debug("Source offer person = {}", person);
          CsvOrganisationLink orgLink = findOrgLinkByPersonId(person.getId(), roles, orgLinks);
          Integer orgId = findOrgIdByRoleAssignment(orgLink, organisationIdMap);
          if (orgId == null) {
            notMigratedList.add(person);
          } else {
            String finalCustomerCompanyName = findCompanyNameOfFinalCustomer(orgLink.getClientId(), finalCustomerMap);
            // Just map real id with prod env
            Long createdUserId = defaultUserId;
            Long modifiedUserId = null;
            Integer langId = commonInitialResource.getLanguageId(person.getLangIso());
            if (EnvUtils.isProductionEnv(activeProfile)) {
              createdUserId = commonInitialResource.getDefaultUserId(person.getUserCreatedId(), defaultUserId);
              modifiedUserId = commonInitialResource.getDefaultModifiedUserId(person.getUserModifiedId(), defaultUserId);

            }
            final TargetOfferPerson offerPerson =
              mapSourceToTarget(person, orgId, createdUserId, modifiedUserId, langId, finalCustomerCompanyName);
            log.debug("Target offer person = {}", offerPerson);
            targetList.add(offerPerson);
          }
        });

      if (!CollectionUtils.isEmpty(targetList)) {
        targetOfferPersonRepo.saveAll(targetList);
      }
    }

    // Write the not migrated person records
    CsvUtils.write(NOT_MIGRATED_FILE_PATH, notMigratedList);

    return finish(contribution);
  }

  private static CsvOrganisationLink findOrgLinkByPersonId(Long personId, Map<Long, CsvRoleAssignment> roles,
    Map<Long, CsvOrganisationLink> orgLinks) {
    CsvRoleAssignment role = roles.get(personId);
    if (role == null) {
      return null;
    }
    return orgLinks.get(role.getOrgId());
  }

  private static Integer findOrgIdByRoleAssignment(CsvOrganisationLink orgLink, Map<Long, Integer> organisationIdMap) {
    if (orgLink == null) {
      return null;
    }
    return organisationIdMap.get(orgLink.getVendorId());
  }

  private String findCompanyNameOfFinalCustomer(Long orgId, Map<Long, CsvFinalCustomer> finalCustomerMap) {
    CsvFinalCustomer finalCustomer = finalCustomerMap.get(orgId);
    if (finalCustomer == null) {
      return StringUtils.EMPTY;
    }
    return finalCustomer.getName();
  }

  private TargetOfferPerson mapSourceToTarget(CsvPerson source,
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
