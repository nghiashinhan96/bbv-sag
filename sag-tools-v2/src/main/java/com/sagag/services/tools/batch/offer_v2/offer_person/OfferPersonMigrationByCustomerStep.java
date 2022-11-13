package com.sagag.services.tools.batch.offer_v2.offer_person;

import com.sagag.services.tools.batch.offer_feature.offer_person.OfferPersonType;
import com.sagag.services.tools.batch.offer_v2.AbstractOfferMigrationSimpleStep;
import com.sagag.services.tools.batch.offer_v2.OfferJpaQueryProvider;
import com.sagag.services.tools.batch.offer_v2.OfferUtils;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourcePerson;
import com.sagag.services.tools.domain.target.TargetOfferPerson;
import com.sagag.services.tools.query.SqlOfferPersonQueryBuilder;
import com.sagag.services.tools.repository.source.SourceOrganisationRepository;
import com.sagag.services.tools.repository.target.LanguageRepository;
import com.sagag.services.tools.repository.target.MappingUserIdEblConnectRepository;
import com.sagag.services.tools.repository.target.TargetOfferPersonRepository;
import com.sagag.services.tools.support.OfferTables;
import com.sagag.services.tools.utils.DefaultUtils;
import com.sagag.services.tools.utils.EntityManagerUtils;
import com.sagag.services.tools.utils.EnvUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.internal.SessionImpl;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Query;

@Configuration
@OracleProfile
@Order(OfferUtils.ORDERED_OFFER_PERSON)
@Slf4j
public class OfferPersonMigrationByCustomerStep
    extends AbstractOfferMigrationSimpleStep<SourcePerson, TargetOfferPerson> {

  @Value("${spring.profiles.active}")
  private String activeProfile;

  @Value("${default.userId:}")
  private Long defaultUserId;

  @Value("${default.organisationId:}")
  private Integer defaultOrgId;

  @Autowired(required=false)
  private SourceOrganisationRepository sourceOrganisationRepo;

  @Autowired
  private MappingUserIdEblConnectRepository mappingRepo;

  @Autowired
  private LanguageRepository langRepo;

  @Autowired
  private TargetOfferPersonRepository targetOfferPersonRepo;

  @Override
  public SimpleStepBuilder<SourcePerson, TargetOfferPerson> stepBuilder() {
    return stepBuilder("offerPersonMigrationByCustomerStep");
  }

  @Bean("offerPersonMigrationByCustomerStepItemReader")
  @StepScope
  @Override
  public ItemStreamReader<SourcePerson> itemReader() throws Exception {
    final List<Long> vendorIds =
        mappingUserIdEblConnectService.searchVendorIds(sysVars.getCustNrs());
    OfferJpaQueryProvider queryProvider = new OfferJpaQueryProvider() {

      @Override
      public Query createQuery() {
        log.debug("Creating query for the vendorIds = {}", vendorIds);
        return SqlOfferPersonQueryBuilder.builder()
            .em(getEntityManager())
            .selector("*")
            .vendorIds(vendorIds)
            .personType(OfferPersonType.ADDRESSEE)
            .clazz(SourcePerson.class)
            .build().createQuery();
      }
    };
    return jpaPagingItemReader(sourceEntityManagerFactory, queryProvider);
  }

  @Bean("offerPersonMigrationByCustomerStepItemProcessor")
  @StepScope
  @Override
  public ItemProcessor<SourcePerson, TargetOfferPerson> itemProcessor() {
    return person -> {
      log.debug("Offer Person ID = {}", person.getId());
      final Long personId = person.getId();
      // Just map real id with prod env
      Long createdUserId = defaultUserId;
      Long modifiedUserId = null;
      Integer langId = langRepo.findIdByLangiso(person.getLangIso());

      String finalCustomerCompanyName = StringUtils.EMPTY;
      Optional<String> finalCustomerNameOpt = sourceOrganisationRepo.findFinalCustomerNameByPersonId(personId);
      if (finalCustomerNameOpt.isPresent()) {
        finalCustomerCompanyName = finalCustomerNameOpt.get();
      }

      if (!EnvUtils.isProductionEnv(activeProfile)) {
        return mapSourceToTarget(person, defaultOrgId, createdUserId, modifiedUserId, langId, finalCustomerCompanyName);
      }

      Optional<BigDecimal> vendorIdOpt = sourceOrganisationRepo.findVendorIdByPersonId(personId);
      Integer orgId = null;
      if (vendorIdOpt.isPresent()) {
        orgId = mappingRepo.findOrgIdByEbl(vendorIdOpt.get().longValueExact());
      }
      if (orgId == null) {
        return null;
      }

      return mapSourceToTarget(person, orgId, createdUserId, modifiedUserId, langId, finalCustomerCompanyName);
    };
  }

  private TargetOfferPerson mapSourceToTarget(SourcePerson source, Integer orgId, Long createdUserId,
      Long modifiedUserId, Integer langId, String companyName) {
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

  @Bean("offerPersonMigrationByCustomerStepItemWriter")
  @StepScope
  @Override
  public ItemWriter<TargetOfferPerson> itemWriter() {
    return items -> {
      SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
      EntityManagerUtils.turnOnIdentityInsert(session, OfferTables.OFFER_PERSON.getTableName());

      final List<TargetOfferPerson> people = items.stream()
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
      targetOfferPersonRepo.saveAll(people);
    };
  }

}
