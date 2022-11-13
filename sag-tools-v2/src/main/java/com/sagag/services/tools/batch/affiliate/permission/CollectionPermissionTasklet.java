package com.sagag.services.tools.batch.affiliate.permission;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.domain.target.CollectionPermission;
import com.sagag.services.tools.repository.target.CollectionPermissionRepository;
import com.sagag.services.tools.repository.target.EshopPermissionRepository;
import com.sagag.services.tools.repository.target.OrganisationCollectionRepository;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Component
@StepScope
@Transactional
public class CollectionPermissionTasklet extends AbstractTasklet {

  @Value("#{'${sag.batch.permission.affiliate_shortname:derendinger-at}'}")
  private String affiliateShortName;

  @Value("#{'${sag.batch.permission.permission_name:}'}")
  private String permissionName;

  @Value("#{'${sag.batch.permission.enable:}'}")
  private Boolean enable;

  @Autowired
  private OrganisationCollectionRepository organisationCollectionRepo;

  @Autowired
  private EshopPermissionRepository eshopPermissionRepo;

  @Autowired
  private CollectionPermissionRepository collectionPermissionRepo;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    List<Integer> collectionIds = organisationCollectionRepo.findCollectionIdsByAffiliateShortname(affiliateShortName);
    if (CollectionUtils.isEmpty(collectionIds)) {
      return noOp(contribution);
    }

    Integer permissionId = eshopPermissionRepo.findPermissionIdByName(permissionName).orElseThrow(() -> new IllegalArgumentException("Not found permission"));
    List<CollectionPermission> savedCollectionPerms = collectionPermissionRepo.findByCollectionIdsAndPermissionId(collectionIds, permissionId);
    if (CollectionUtils.isNotEmpty(savedCollectionPerms)) {
      collectionPermissionRepo.deleteAll(savedCollectionPerms);
    }

    if (enable) {
      List<CollectionPermission> newCollectionPerms = collectionIds.stream()
          .map(collectionId -> CollectionPermission.builder().collectionId(collectionId).eshopPermissionId(permissionId).build()).collect(Collectors.toList());
      collectionPermissionRepo.saveAll(newCollectionPerms);
    }
    return finish(contribution);
  }
}
