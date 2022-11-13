package com.sagag.services.gtmotive.builder.mainmoduleservice;

import static org.junit.Assert.assertThat;

import com.sagag.services.gtmotive.DataProvider;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsThreeReference;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsThreeResponse;
import com.sagag.services.gtmotive.lang.GtmotiveLanguageProvider;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
public class GtmotivePartsThreeResponseBuilderTest {

  @InjectMocks
  private GtmotivePartsThreeResponseBuilder gtmotivePartsThreeResponseBuilder;

  @Mock
  private GtmotivePartsThreeReferencesCaseOneHandler gtmotivePartsThreeReferencesCaseOneHandler;

  @Mock
  private GtmotivePartsThreeReferencesCaseTwoHandler gtmotivePartsThreeReferencesCaseTwoHandler;

  @Mock
  private GtmotivePartsThreeReferencesCaseThreeHandler gtmotivePartsThreeReferencesCaseThreeHandler;

  @Mock
  private GtmotiveLanguageProvider gtmotiveLanguageProvider;

  @Test
  public void build_shouldReturnReferenceCaseOne_givenCaseOneData() throws Exception {
    GtmotivePartsThreeReference partOne = new GtmotivePartsThreeReference();
    partOne.setCode("0000071775979");
    partOne.setDescription("Stoßdämpfer li Federung vo");
    List<GtmotivePartsThreeReference> caseOne = Arrays.asList(partOne);
    Mockito.when(
        gtmotivePartsThreeReferencesCaseOneHandler.achieveReferences(Mockito.any(), Mockito.any()))
        .thenReturn(caseOne);

    GtmotivePartsThreeResponse response =
        gtmotivePartsThreeResponseBuilder.build(DataProvider.gtmotivePartsThreeCaseOneResponse());

    List<GtmotivePartsThreeReference> references = response.getReferences();
    GtmotivePartsThreeReference reference = references.get(0);

    assertThat(references.size(), Matchers.is(1));
    assertThat(reference.getCode(), Matchers.is("0000071775979"));
    assertThat(reference.getDescription(), Matchers.is("Stoßdämpfer li Federung vo"));
  }

  @Test
  public void build_shouldReturnReferenceCaseTwo_givenCaseTwoData() throws Exception {
    GtmotivePartsThreeReference partOne = new GtmotivePartsThreeReference();
    partOne.setCode("2223HS");
    GtmotivePartsThreeReference partTwo = new GtmotivePartsThreeReference();
    partTwo.setCode("2201XP");
    List<GtmotivePartsThreeReference> caseTwo = Arrays.asList(partOne, partTwo);
    Mockito.when(
        gtmotivePartsThreeReferencesCaseTwoHandler.achieveReferences(Mockito.any(), Mockito.any()))
        .thenReturn(caseTwo);

    GtmotivePartsThreeResponse response =
        gtmotivePartsThreeResponseBuilder.build(DataProvider.gtmotivePartsThreeCaseTwoResponse());
    List<GtmotivePartsThreeReference> references = response.getReferences();
    GtmotivePartsThreeReference reference = references.get(0);

    assertThat(references.size(), Matchers.is(2));
    assertThat(reference.getCode(), Matchers.is("2223HS"));
  }

  @Test
  public void build_shouldReturnReferenceCaseThree_givenCaseThreeData() throws Exception {
    GtmotivePartsThreeReference partOne = new GtmotivePartsThreeReference();
    partOne.setCode("8119412100");
    partOne.setDescription("ÜBER");
    GtmotivePartsThreeReference partTwo = new GtmotivePartsThreeReference();
    partTwo.setCode("8119612040");
    partTwo.setDescription("MITTE");
    GtmotivePartsThreeReference partThree = new GtmotivePartsThreeReference();
    partThree.setCode("8119812040");
    partThree.setDescription("UNTEN");

    List<GtmotivePartsThreeReference> caseThree = Arrays.asList(partOne, partTwo, partThree);
    Mockito.when(gtmotivePartsThreeReferencesCaseThreeHandler.achieveReferences(Mockito.any(),
        Mockito.any())).thenReturn(caseThree);

    GtmotivePartsThreeResponse response =
        gtmotivePartsThreeResponseBuilder.build(DataProvider.gtmotivePartsThreeCaseThreeResponse());
    List<GtmotivePartsThreeReference> references = response.getReferences();
    GtmotivePartsThreeReference referenceOne = references.get(0);
    GtmotivePartsThreeReference referenceTwo = references.get(1);
    GtmotivePartsThreeReference referenceThree = references.get(2);

    assertThat(references.size(), Matchers.is(3));
    assertThat(referenceOne.getCode(), Matchers.is("8119412100"));
    assertThat(referenceOne.getDescription(), Matchers.is("ÜBER"));
    assertThat(referenceTwo.getCode(), Matchers.is("8119612040"));
    assertThat(referenceTwo.getDescription(), Matchers.is("MITTE"));
    assertThat(referenceThree.getCode(), Matchers.is("8119812040"));
    assertThat(referenceThree.getDescription(), Matchers.is("UNTEN"));
  }

  @Test
  public void build_shouldReturnReferenceCaseOneAndTwo_givenCaseOneAndTwoData() throws Exception {
    GtmotivePartsThreeReference partOne = new GtmotivePartsThreeReference();
    partOne.setCode("34116855006");
    GtmotivePartsThreeReference partTwo = new GtmotivePartsThreeReference();
    partTwo.setCode("34116855006");
    GtmotivePartsThreeReference partThree = new GtmotivePartsThreeReference();
    partThree.setCode("34116855000");
    GtmotivePartsThreeReference partFour = new GtmotivePartsThreeReference();
    partThree.setCode("34116786392");

    List<GtmotivePartsThreeReference> caseOne = Arrays.asList(partOne, partTwo, partThree);
    Mockito.when(
        gtmotivePartsThreeReferencesCaseOneHandler.achieveReferences(Mockito.any(), Mockito.any()))
        .thenReturn(caseOne);

    List<GtmotivePartsThreeReference> caseTow = Arrays.asList(partFour);
    Mockito.when(
        gtmotivePartsThreeReferencesCaseTwoHandler.achieveReferences(Mockito.any(), Mockito.any()))
        .thenReturn(caseTow);

    GtmotivePartsThreeResponse response =
        gtmotivePartsThreeResponseBuilder.build(DataProvider.gtmotivePartsThreeResponse());
    List<GtmotivePartsThreeReference> references = response.getReferences();
    assertThat(references.size(), Matchers.is(4));
  }

  @Test
  public void build_shouldReturnError_giveDataCaseError() throws Exception {
    GtmotivePartsThreeResponse response =
        gtmotivePartsThreeResponseBuilder.build(DataProvider.gtmotivePartsThreeCaseHasError());
    Integer errorCode = response.getErrorCode();
    assertThat(errorCode, Matchers.is(12));
  }
}
