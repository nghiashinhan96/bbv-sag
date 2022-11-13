package com.sagag.services.tools.batch.customer.klaus;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.domain.target.Country;
import com.sagag.services.tools.domain.target.Organisation;
import com.sagag.services.tools.domain.target.OrganisationType;
import com.sagag.services.tools.repository.target.CountryRepository;
import com.sagag.services.tools.repository.target.OrganisationRepository;
import com.sagag.services.tools.repository.target.OrganisationTypeRepository;
import com.sagag.services.tools.repository.target.SupportedAffiliateRepository;
import com.sagag.services.tools.support.SupportedAffiliate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Optional;

import javax.transaction.Transactional;

@Component
@StepScope
@Transactional
public class CreateKlausAffiliateTasklet extends AbstractTasklet implements IKlausCustomerMapper {

  @Autowired
  private OrganisationRepository orgRepo;

  @Autowired
  private OrganisationTypeRepository orgTypeRepo;

  @Autowired
  private SupportedAffiliateRepository supportedAffRepo;

  @Autowired
  private CountryRepository countryRepo;

  @Override
  public RepeatStatus execute(StepContribution contribution,
      ChunkContext chunkContext) throws Exception {
    Optional<Integer> orgIdOpt = orgRepo.findIdByShortName(SupportedAffiliate.KLAUS.getAffiliate());
    if (!orgIdOpt.isPresent()) {
      orgRepo.save(createAffiliateOrganisation());
    }

    if (!supportedAffRepo.findFirstByShortName(SupportedAffiliate.KLAUS.getAffiliate())
        .isPresent()) {
      com.sagag.services.tools.domain.target.SupportedAffiliate supportedAff =
          createSupportedAffiliate();
      supportedAffRepo.save(supportedAff);
    }

    return finish(contribution);
  }

  private Organisation createAffiliateOrganisation() {
    final Organisation affOrganisation = new Organisation();

    affOrganisation.setName(SupportedAffiliate.KLAUS.getCompanyName());
    affOrganisation.setOrgCode(IKlausCustomerMapper.DUMMY_ORG_CODE);

    final Integer orgTypeId = orgTypeRepo.findByName("AFFILIATE").map(OrganisationType::getId)
        .orElseThrow(() -> new IllegalArgumentException("Not found org type for AFFILIATE name"));
    affOrganisation.setOrgTypeId(orgTypeId);
    affOrganisation.setShortname(SupportedAffiliate.KLAUS.getAffiliate());
    affOrganisation.setParentId(orgRepo.findIdByShortName(DF_ORG_COLLECTION_NAME)
        .orElseThrow(() -> new IllegalArgumentException()));
    affOrganisation.setCustomerSettings(null);

    return affOrganisation;
  }

  private com.sagag.services.tools.domain.target.SupportedAffiliate createSupportedAffiliate() {
    com.sagag.services.tools.domain.target.SupportedAffiliate aff =
        new com.sagag.services.tools.domain.target.SupportedAffiliate();

    aff.setCompanyName(SupportedAffiliate.KLAUS.getCompanyName());
    aff.setShortName(SupportedAffiliate.KLAUS.getAffiliate());
    aff.setCountryId(countryRepo.findByCode(IKlausCustomerMapper.COUNTRY_CODE).map(Country::getId)
        .orElse(null));
    aff.setUpdatedDate(Calendar.getInstance().getTime());
    aff.setEsShortName(SupportedAffiliate.KLAUS.getEsShortName());
    aff.setSalesOriginId(SupportedAffiliate.KLAUS.getSalesOriginId());
    aff.setLogoLink(StringUtils.EMPTY);
    aff.setNoReplyEmail(StringUtils.EMPTY);
    aff.setShowPfandArticle(false);

    return aff;
  }

}
