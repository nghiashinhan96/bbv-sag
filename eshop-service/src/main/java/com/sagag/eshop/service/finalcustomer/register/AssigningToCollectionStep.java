package com.sagag.eshop.service.finalcustomer.register;

import com.sagag.eshop.repo.api.collection.CollectionRelationRepository;
import com.sagag.eshop.repo.api.collection.OrganisationCollectionRepository;
import com.sagag.eshop.repo.entity.collection.CollectionRelation;
import com.sagag.eshop.service.dto.finalcustomer.NewFinalCustomerDto;
import com.sagag.eshop.service.finalcustomer.register.result.AssigningToCollectionStepResult;
import com.sagag.eshop.service.finalcustomer.register.result.NewFinalCustomerStepResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class AssigningToCollectionStep extends AbstractNewFinalCustomerStep {

  @Autowired
  private OrganisationCollectionRepository organisationCollectionRepo;

  @Autowired
  private CollectionRelationRepository collectionRelationRepo;

  @Override
  public String getStepName() {
    return "Assign final customer to collection";
  }

  @Override
  public NewFinalCustomerStepResult processItem(NewFinalCustomerDto finalCustomer,
      NewFinalCustomerStepResult stepResult) {

    String collectionShortname = finalCustomer.getCollectionShortname();
    Integer organisationCollectionId =
        organisationCollectionRepo.findCollectionIdByShortname(collectionShortname)
            .orElseThrow(() -> new NoSuchElementException(
                "Not found collection has shortname = " + collectionShortname));

    Integer organisationId = stepResult.getAddingOrganisationStepResult().getOrganisation().getId();
    CollectionRelation collectionRelation =
        CollectionRelation.builder().collectionId(organisationCollectionId)
            .organisationId(organisationId).isActive(true).build();

    CollectionRelation createdCollectionRelation = collectionRelationRepo.save(collectionRelation);

    stepResult.setAssigningToCollectionStepResult(
        new AssigningToCollectionStepResult(createdCollectionRelation.getId()));
    return stepResult;
  }

}
