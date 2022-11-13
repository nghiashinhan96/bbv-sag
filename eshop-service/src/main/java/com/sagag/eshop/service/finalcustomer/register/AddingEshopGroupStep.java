package com.sagag.eshop.service.finalcustomer.register;

import com.sagag.eshop.repo.api.EshopGroupRepository;
import com.sagag.eshop.repo.api.EshopRoleRepository;
import com.sagag.eshop.repo.api.GroupRoleRepository;
import com.sagag.eshop.repo.api.OrganisationGroupRepository;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopRole;
import com.sagag.eshop.repo.entity.GroupRole;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.OrganisationGroup;
import com.sagag.eshop.service.dto.finalcustomer.NewFinalCustomerDto;
import com.sagag.eshop.service.finalcustomer.register.result.AddingEshopGroupStepResult;
import com.sagag.eshop.service.finalcustomer.register.result.NewFinalCustomerStepResult;
import com.sagag.services.domain.eshop.common.EshopUserCreateAuthority;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Component
public class AddingEshopGroupStep extends AbstractNewFinalCustomerStep {

  @Autowired
  private EshopGroupRepository eshopGroupRepo;

  @Autowired
  private EshopRoleRepository eshopRoleRepo;

  @Autowired
  private GroupRoleRepository groupRoleRepo;

  @Autowired
  private OrganisationGroupRepository orgGroupRepo;

  @Override
  public String getStepName() {
    return "Adding eshop group step";
  }

  @Override
  public NewFinalCustomerStepResult processItem(NewFinalCustomerDto finalCustomerDto,
      NewFinalCustomerStepResult stepResult) {
    Organisation organisation = stepResult.getAddingOrganisationStepResult().getOrganisation();
    String customerNr = finalCustomerDto.getCustomerNr();
    String customerName = finalCustomerDto.getCustomerName();

    EshopGroup adminUserGroup = prepareAdminUserGroup(customerNr, customerName);
    EshopRole adminRole = prepareEshopRole(EshopUserCreateAuthority.FINAL_USER_ADMIN.name());
    prepareGroupRole(adminUserGroup, adminRole);

    EshopGroup normalUserGroup = prepareNormalUserGroup(customerNr, customerName);
    EshopRole userRole = prepareEshopRole(EshopUserCreateAuthority.FINAL_NORMAL_USER.name());
    prepareGroupRole(normalUserGroup, userRole);

    prepareOrganisationGroup(organisation, adminUserGroup);
    prepareOrganisationGroup(organisation, normalUserGroup);
    stepResult.setAddingEshopGroupStepResult(
        (new AddingEshopGroupStepResult(Arrays.asList(adminUserGroup, normalUserGroup))));
    return stepResult;
  }

  private void prepareOrganisationGroup(Organisation organisation, EshopGroup eshopGroup) {
    OrganisationGroup orgGroup = new OrganisationGroup();
    orgGroup.setEshopGroup(eshopGroup);
    orgGroup.setOrganisation(organisation);
    orgGroupRepo.save(orgGroup);
  }

  private void prepareGroupRole(EshopGroup eshopGroup, EshopRole eshopRole) {
    GroupRole eshopGroupRole = new GroupRole();
    eshopGroupRole.setEshopGroup(eshopGroup);
    eshopGroupRole.setEshopRole(eshopRole);
    groupRoleRepo.save(eshopGroupRole);
  }

  private EshopGroup prepareNormalUserGroup(String customerNr, String customerName) {
    EshopGroup eshopGroupNormal = new EshopGroup();
    eshopGroupNormal.setName("FINAL_CUSTOMER_" + customerNr + "_NORMAL_USER");
    eshopGroupNormal.setDescription("Normal user group of " + customerName);
    return eshopGroupRepo.save(eshopGroupNormal);
  }

  private EshopGroup prepareAdminUserGroup(String customerNr, String customerName) {
    EshopGroup eshopGroupAdmin = new EshopGroup();
    eshopGroupAdmin.setName("FINAL_CUSTOMER_" + customerNr + "_USER_ADMIN");
    eshopGroupAdmin.setDescription("User admin group of " + customerName);
    return eshopGroupRepo.save(eshopGroupAdmin);
  }

  private EshopRole prepareEshopRole(String roleName) {
    return eshopRoleRepo.findOneByName(roleName)
        .orElseThrow(() -> new NoSuchElementException("Not found role : " + roleName));
  }
}
