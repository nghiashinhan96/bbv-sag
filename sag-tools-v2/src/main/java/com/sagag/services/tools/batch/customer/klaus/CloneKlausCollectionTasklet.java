package com.sagag.services.tools.batch.customer.klaus;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.domain.target.CollectionPermission;
import com.sagag.services.tools.domain.target.CollectionRelation;
import com.sagag.services.tools.domain.target.EshopPermission;
import com.sagag.services.tools.domain.target.OrganisationCollection;
import com.sagag.services.tools.domain.target.OrganisationCollectionsSettings;
import com.sagag.services.tools.repository.target.CollectionPermissionRepository;
import com.sagag.services.tools.repository.target.CollectionRelationRepository;
import com.sagag.services.tools.repository.target.EshopPermissionRepository;
import com.sagag.services.tools.repository.target.OrganisationCollectionRepository;
import com.sagag.services.tools.repository.target.OrganisationCollectionsSettingsRepository;
import com.sagag.services.tools.repository.target.OrganisationRepository;
import com.sagag.services.tools.support.SupportedAffiliate;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Component
@StepScope
@Transactional
public class CloneKlausCollectionTasklet extends AbstractTasklet implements IKlausCustomerMapper {

  @Autowired
  private OrganisationRepository orgRepo;

  @Autowired
  private OrganisationCollectionRepository orgCollectionRepo;

  @Autowired
  private OrganisationCollectionsSettingsRepository orgCollectionSettingsRepo;

  @Autowired
  private CollectionPermissionRepository collectionPermissionRepo;

  @Autowired
  private EshopPermissionRepository eshopPermissionRepo;

  @Autowired
  private CollectionRelationRepository collectionRelationRepo;

  @Override
  public RepeatStatus execute(StepContribution contribution,
      ChunkContext chunkContext) throws Exception {

    Optional<Integer> orgIdOpt = orgRepo.findIdByShortName(SupportedAffiliate.KLAUS.getAffiliate());
    Optional<OrganisationCollection> orgCollectionOpt = orgCollectionRepo.findByShortname(
        SupportedAffiliate.KLAUS.getAffiliate());

    if (orgCollectionOpt.isPresent()) {

      buildOrgCollectionRelationForKlausAffiliate(orgCollectionOpt.get().getId(),
          orgIdOpt.get().intValue())
      .ifPresent(collectionRelationRepo::save);

      return noOp(contribution);
    }

    Integer orgId = orgIdOpt.get();
    OrganisationCollection orgCollection = orgCollectionRepo.save(
        cloneOrganisationCollection(orgId));

    orgCollectionSettingsRepo.saveAll(cloneOrgCollectionSettings(orgCollection.getId()));
    collectionPermissionRepo.saveAll(cloneOrgCollectionPermissions(orgCollection.getId()));

    buildOrgCollectionRelationForKlausAffiliate(orgCollection.getId(), orgIdOpt.get().intValue())
    .ifPresent(collectionRelationRepo::save);

    return finish(contribution);
  }

  private OrganisationCollection cloneOrganisationCollection(int orgId) {
    OrganisationCollection orgCollection = new OrganisationCollection();
    orgCollection.setName(SupportedAffiliate.KLAUS.getCompanyName());
    orgCollection.setAffiliateId(orgId);
    orgCollection.setDescription("This is the Default Collection");
    orgCollection.setShortname(SupportedAffiliate.KLAUS.getAffiliate());
    return orgCollection;
  }

  private List<OrganisationCollectionsSettings> cloneOrgCollectionSettings(int collectionId) {
    final List<OrganisationCollectionsSettings> existingKlausOrgColSettings =
        orgCollectionSettingsRepo.findByCollectionId(collectionId);

    if (!CollectionUtils.isEmpty(existingKlausOrgColSettings)) {
      return Collections.emptyList();
    }

    final List<OrganisationCollectionsSettings> settings = orgCollectionSettingsRepo
        .findByCollectionShortname(DF_ORG_COLLECTION_NAME);
    return settings.stream().map(item -> {
      OrganisationCollectionsSettings newSettings = new OrganisationCollectionsSettings();
      newSettings.setCollectionId(collectionId);
      newSettings.setSettingKey(item.getSettingKey());
      newSettings.setSettingValue(item.getSettingValue());
      return newSettings;
    }).collect(Collectors.toList());
  }

  private List<CollectionPermission> cloneOrgCollectionPermissions(int collectionId) {
    final List<Integer> permissionIds = eshopPermissionRepo.findAll().stream()
        .map(EshopPermission::getId).collect(Collectors.toList());
    return permissionIds.stream().map(permissionId -> {
      CollectionPermission cPerm = new CollectionPermission();
      cPerm.setCollectionId(collectionId);
      cPerm.setEshopPermissionId(permissionId);
      return cPerm;
    }).collect(Collectors.toList());
  }

  private Optional<CollectionRelation> buildOrgCollectionRelationForKlausAffiliate(
      int klausOrgCollectionId, int affOrgId) {
    if (collectionRelationRepo.findByCollectionIdAndOrganisationId(
        klausOrgCollectionId, affOrgId).isPresent()) {
      return Optional.empty();
    }
    CollectionRelation colRelation = buildOrgCollectionRelation(klausOrgCollectionId, affOrgId);
    return Optional.of(colRelation);
  }
}
