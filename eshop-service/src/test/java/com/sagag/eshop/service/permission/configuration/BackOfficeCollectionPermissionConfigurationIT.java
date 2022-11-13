package com.sagag.eshop.service.permission.configuration;

import com.sagag.eshop.repo.api.collection.CollectionPermissionRepository;
import com.sagag.eshop.repo.entity.collection.CollectionPermission;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;
import com.sagag.services.domain.eshop.dto.collection.OrganisationCollectionDto;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ServiceApplication.class})
@EshopIntegrationTest
@Transactional
public class BackOfficeCollectionPermissionConfigurationIT {
  private String affiliateShortName = "derendinger-ch";

  @Autowired
  private BackOfficeCollectionPermissionConfiguration configuration;

  @Autowired
  private CollectionPermissionRepository collectionPermissionRepository;

  @Test
  public void shouldGetPermissionForCollection() {
    OrganisationCollectionDto data = OrganisationCollectionDto.builder()
        .affiliateShortName(affiliateShortName)
        .build();
    List<PermissionConfigurationDto> result = configuration.getPermissions(data);
    Boolean isWholesalerEnable = result.stream().filter(p -> p.getPermission().equalsIgnoreCase("WHOLESALER"))
        .map(PermissionConfigurationDto::isEditable)
        .findAny().orElse(null);
    Assert.assertThat(isWholesalerEnable, Matchers.is(false));
    Assert.assertThat(result, Matchers.notNullValue());
    Assert.assertThat(result, Matchers.hasSize(Matchers.greaterThanOrEqualTo(1)));
  }

  @Test
  public void shouldGetPermissionForCollection2() {
    List<CollectionPermission> permissions =
        collectionPermissionRepository.findByCollectionShortName(affiliateShortName);
    List<PermissionConfigurationDto> result = configuration.getPermissions(affiliateShortName, permissions, false);
    Boolean isWholesalerEnable = result.stream().filter(p -> p.getPermission().equalsIgnoreCase("WHOLESALER"))
        .map(PermissionConfigurationDto::isEditable)
        .findAny().orElse(null);
    Assert.assertThat(isWholesalerEnable, Matchers.is(false));
    Assert.assertThat(result, Matchers.notNullValue());
    Assert.assertThat(result, Matchers.hasSize(Matchers.greaterThanOrEqualTo(1)));
  }

}
