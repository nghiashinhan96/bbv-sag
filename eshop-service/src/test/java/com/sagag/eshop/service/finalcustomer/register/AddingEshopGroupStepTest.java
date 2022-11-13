package com.sagag.eshop.service.finalcustomer.register;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.EshopGroupRepository;
import com.sagag.eshop.repo.api.EshopRoleRepository;
import com.sagag.eshop.repo.api.GroupRoleRepository;
import com.sagag.eshop.repo.api.OrganisationGroupRepository;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopRole;
import com.sagag.eshop.repo.entity.GroupRole;
import com.sagag.eshop.repo.entity.OrganisationGroup;
import com.sagag.eshop.service.finalcustomer.register.result.AddingOrganisationStepResult;
import com.sagag.eshop.service.finalcustomer.register.result.NewFinalCustomerStepResult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class AddingEshopGroupStepTest extends AbstractNewFinalCustomerTest {

  @InjectMocks
  private AddingEshopGroupStep addingEshopGroupStep;

  @Mock
  private EshopGroupRepository eshopGroupRepo;

  @Mock
  private EshopRoleRepository eshopRoleRepo;

  @Mock
  private GroupRoleRepository groupRoleRepo;

  @Mock
  private OrganisationGroupRepository orgGroupRepo;

  @Test
  public void processItem_shouldAddEshopGroup_givenNewFinalCustomerDto() throws Exception {
    EshopGroup adminUserGroup =
        EshopGroup.builder().id(279).description("User admin group of final customer 1")
            .name("FINAL_CUSTOMER_100_USER_ADMIN").build();
    when(eshopGroupRepo.save(any(EshopGroup.class))).thenReturn(adminUserGroup);

    EshopRole adminRole = EshopRole.builder().id(7).description("Wholesaler User Admin Role")
        .name("FINAL_USER_ADMIN").build();
    when(eshopRoleRepo.findOneByName(anyString())).thenReturn(Optional.of(adminRole));

    GroupRole groupRole =
        GroupRole.builder().eshopGroup(adminUserGroup).eshopRole(adminRole).build();
    when(groupRoleRepo.save(any(GroupRole.class))).thenReturn(groupRole);

    OrganisationGroup orgGroup = OrganisationGroup.builder().eshopGroup(adminUserGroup)
        .organisation(buildOrganisation()).build();
    when(orgGroupRepo.save(any(OrganisationGroup.class))).thenReturn(orgGroup);

    NewFinalCustomerStepResult stepResult = new NewFinalCustomerStepResult();
    stepResult
        .setAddingOrganisationStepResult(new AddingOrganisationStepResult(buildOrganisation()));

    NewFinalCustomerStepResult result =
        addingEshopGroupStep.processItem(buildNewFinalCustomerDto(), stepResult);

    List<EshopGroup> eshopGroups = result.getAddingEshopGroupStepResult().getEshopGroups();
    assertNotNull(eshopGroups);
  }
}
