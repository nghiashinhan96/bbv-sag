package com.sagag.eshop.service.finalcustomer.register;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.collection.CollectionRelationRepository;
import com.sagag.eshop.repo.api.collection.OrganisationCollectionRepository;
import com.sagag.eshop.repo.entity.collection.CollectionRelation;
import com.sagag.eshop.service.finalcustomer.register.result.AddingOrganisationStepResult;
import com.sagag.eshop.service.finalcustomer.register.result.NewFinalCustomerStepResult;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class AssigningToCollectionStepTest extends AbstractNewFinalCustomerTest {

  @InjectMocks
  private AssigningToCollectionStep assigningToCollectionStep;

  @Mock
  private OrganisationCollectionRepository organisationCollectionRepo;

  @Mock
  private CollectionRelationRepository collectionRelationRepo;

  @Test
  public void processItem_shouldAddCollectionRelation_givenNewFinalCustomerDto() throws Exception {

    when(organisationCollectionRepo.findCollectionIdByShortname(anyString()))
        .thenReturn(Optional.of(6));

    CollectionRelation collectionRelation = CollectionRelation.builder().id(1).collectionId(6)
        .organisationId(160).isActive(true).build();
    when(collectionRelationRepo.save(any(CollectionRelation.class))).thenReturn(collectionRelation);

    NewFinalCustomerStepResult stepResult = new NewFinalCustomerStepResult();
    stepResult
        .setAddingOrganisationStepResult(new AddingOrganisationStepResult(buildOrganisation()));

    NewFinalCustomerStepResult result =
        assigningToCollectionStep.processItem(buildNewFinalCustomerDto(), stepResult);

    Integer collectionRelationId =
        result.getAssigningToCollectionStepResult().getCollectionRelationId();
    assertNotNull(collectionRelationId);
    assertThat(collectionRelationId, Matchers.is(1));
  }
}
