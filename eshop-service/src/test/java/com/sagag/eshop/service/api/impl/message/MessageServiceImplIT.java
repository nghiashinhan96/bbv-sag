package com.sagag.eshop.service.api.impl.message;

import com.sagag.eshop.service.api.MessageService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.criteria.VMessageSearchRequestCriteria;
import com.sagag.services.domain.eshop.message.dto.MessageFilterOptionsMasterData;
import com.sagag.services.domain.eshop.message.dto.MessageLanguageDto;
import com.sagag.services.domain.eshop.message.dto.MessageLocationTypeDto;
import com.sagag.services.domain.eshop.message.dto.MessageMasterDataDto;
import com.sagag.services.domain.eshop.message.dto.MessageResultDto;
import com.sagag.services.domain.eshop.message.dto.MessageSavingRequestDto;
import com.sagag.services.domain.eshop.message.dto.MessageSearchResultDto;
import com.sagag.services.domain.eshop.message.dto.MessageStyleDto;
import com.sagag.services.domain.eshop.message.dto.MessageTypeDto;
import com.sagag.services.domain.eshop.message.dto.MessageVisibilityDto;
import com.sagag.services.domain.eshop.message.dto.SupportedAffiliateDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * This class provides integration test for {@link MessageService}.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Slf4j
@Transactional
public class MessageServiceImplIT {

  @Autowired
  private MessageService messageService;

  @Test
  public void getMessageMasterData_shouldReturnMessageMasterData() throws Exception {
    long startTime = System.currentTimeMillis();
    MessageMasterDataDto masterData = messageService.getMasterData();
    log.info("Executed MessageService.getMessageMasterDatas method after {}",
        System.currentTimeMillis() - startTime);
    Assert.assertNotNull(masterData);

    List<MessageLocationTypeDto> locationTypes = masterData.getLocationTypes();
    Assert.assertThat(locationTypes.size(), Matchers.greaterThanOrEqualTo(2));
    Assert.assertThat(locationTypes.get(0).getLocationType(), Matchers.is("AFFILIATE"));
    Assert.assertThat(locationTypes.get(1).getLocationType(), Matchers.is("CUSTOMER"));


    List<MessageTypeDto> types = masterData.getTypes();
    Assert.assertThat(types.size(), Matchers.is(3));
    Assert.assertThat(types.get(0).getType(), Matchers.is("BANNER"));
    Assert.assertThat(types.get(1).getType(), Matchers.is("PANEL"));
    Assert.assertThat(types.get(2).getType(), Matchers.is("ALL"));

    List<MessageStyleDto> styles = masterData.getStyles();
    Assert.assertThat(styles.size(), Matchers.is(5));
    Assert.assertThat(styles.get(0).getStyle(), Matchers.is("DEFAULT"));
    Assert.assertThat(styles.get(1).getStyle(), Matchers.is("PRIMARY"));

    List<MessageVisibilityDto> visibilities = masterData.getVisibilities();
    Assert.assertThat(visibilities.size(), Matchers.is(3));
    Assert.assertThat(visibilities.get(0).getVisibility(), Matchers.is("ONCE"));
    Assert.assertThat(visibilities.get(1).getVisibility(), Matchers.is("UNTIL_CLOSE"));
    Assert.assertThat(visibilities.get(2).getVisibility(), Matchers.is("UNTIL_CLOSE_AS_USER_SECTION"));


    List<SupportedAffiliateDto> supportedAffiliates = masterData.getSupportedAffiliates();
    Assert.assertThat(supportedAffiliates.size(), Matchers.lessThanOrEqualTo(7));
  }

  @Test(expected = IllegalArgumentException.class)
  public void createMessage_shouldCreateSuccessfully_GivenFullInfo() throws Exception {
    long startTime = System.currentTimeMillis();
    messageService.create(buildMessageDto(), 1L);
    log.info("Executed MessageService.create method after {}", System.currentTimeMillis()
        - startTime);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createMessage_shouldThrowException_givenOverlapMessage() throws Exception {
    MessageSavingRequestDto dto = buildMessageDto();
    dto.setDateValidFrom("2019-06-11 00:00:00.000");
    dto.setDateValidTo("2019-07-11 00:00:00.000");
    messageService.create(dto, 1L);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createMessage_shouldThrowException_GivenModelMissedLocationId() throws Exception {
    MessageSavingRequestDto model = buildMessageDto();
    model.setLocationTypeId(null);
    messageService.create(model, 1L);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createMessage_shouldThrowException_GivenModelMissedAccessRightId() throws Exception {
    MessageSavingRequestDto model = buildMessageDto();
    model.setAccessRightId(null);
    messageService.create(model, 1L);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createMessage_shouldThrowException_GivenModelMissedTypeId() throws Exception {
    MessageSavingRequestDto model = buildMessageDto();
    model.setTypeId(null);
    messageService.create(model, 1L);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createMessage_shouldThrowException_GivenModelMissedSubAreaId() throws Exception {
    MessageSavingRequestDto model = buildMessageDto();
    model.setSubAreaId(null);
    messageService.create(model, 1L);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createMessage_shouldThrowException_GivenModelMissedStyleId() throws Exception {
    MessageSavingRequestDto model = buildMessageDto();
    model.setStyleId(null);
    messageService.create(model, 1L);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createMessage_shouldThrowException_GivenModelMissedStartDate() throws Exception {
    MessageSavingRequestDto model = buildMessageDto();
    model.setDateValidFrom(null);
    messageService.create(model, 1L);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createMessage_shouldThrowException_GivenModelMissedEndDate() throws Exception {
    MessageSavingRequestDto model = buildMessageDto();
    model.setDateValidTo(null);
    messageService.create(model, 1L);
  }


  @Test(expected = IllegalArgumentException.class)
  public void createMessage_shouldThrowException_GivenModelWithStartDateAfterEndDate()
      throws Exception {
    MessageSavingRequestDto model = buildMessageDto();
    model.setDateValidFrom("2021-02-11 00:00:00.000");
    model.setDateValidTo("2021-01-11 00:00:00.000");
    messageService.create(model, 1L);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createMessage_shouldThrowException_GivenModelMissedLanguages() throws Exception {
    MessageSavingRequestDto model = buildMessageDto();
    model.setMessageLanguages(null);
    messageService.create(model, 1L);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createMessage_shouldThrowException_GivenModelHasLanguagesMissedLangIso()
      throws Exception {
    MessageLanguageDto de = MessageLanguageDto.builder().content("Content in de").build();
    MessageSavingRequestDto model = buildMessageDto();
    model.setMessageLanguages(Arrays.asList(de));
    messageService.create(model, 1L);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createMessage_shouldThrowException_GivenModelHasLanguagesMissedContent()
      throws Exception {
    MessageLanguageDto de = MessageLanguageDto.builder().langIso("DE").build();
    MessageSavingRequestDto model = buildMessageDto();
    model.setMessageLanguages(Arrays.asList(de));
    messageService.create(model, 1L);
  }

  private MessageSavingRequestDto buildMessageDto() {
    // @formatter:off
    MessageLanguageDto de =
        MessageLanguageDto.builder().langIso("DE").content("Content in de").build();
    MessageLanguageDto fr =
        MessageLanguageDto.builder().langIso("FR").content("Content in fr").build();
    MessageLanguageDto it =
        MessageLanguageDto.builder().langIso("IT").content("Content in it").build();
    List<MessageLanguageDto> languages = Arrays.asList(de, fr, it);

    return MessageSavingRequestDto.builder()
        .active(true)
        .title("Ms 1")
        .locationTypeId(1)
        .locationValue(SupportedAffiliate.DERENDINGER_AT.getAffiliate())
        .accessRightId(1)
        .typeId(1)
        .subAreaId(4)
        .styleId(1)
        .visibilityId(1)
        .dateValidFrom("2021-01-11 00:00:00.000")
        .dateValidTo("2021-02-11 00:00:00.000")
        .messageLanguages(languages)
        .build();
    // @formatter:on
  }

  @Test
  public void searchMessages_shouldReturnMessages_givenMessageTitle() throws Exception {
    String title = "ms-001";
    PageRequest pageRequest = PageRequest.of(0, 10);
    VMessageSearchRequestCriteria criteria =
        VMessageSearchRequestCriteria.builder().title(title).build();
    Page<MessageSearchResultDto> results = messageService.searchMessage(criteria, pageRequest);
    Assert.assertThat(results.getContent().size(), Matchers.is(1));
    Assert.assertThat(results.getContent().get(0).getTitle(), Matchers.is(title));
  }

  @Test
  public void searchMessages_shouldReturnMessages_givenMessageType() throws Exception {
    String type = "BANNER";
    PageRequest pageRequest = PageRequest.of(0, 10);
    VMessageSearchRequestCriteria criteria =
        VMessageSearchRequestCriteria.builder().type(type).build();
    Page<MessageSearchResultDto> results = messageService.searchMessage(criteria, pageRequest);
    Assert.assertThat(results.getContent().size(), Matchers.greaterThan(1));
    Assert.assertThat(results.getContent().get(0).getType(), Matchers.is(type));
  }

  @Test
  public void searchMessages_shouldReturnMessages_givenMessageSubArea() throws Exception {
    String subArea = "LOGIN_PAGE_1";
    PageRequest pageRequest = PageRequest.of(0, 10);
    VMessageSearchRequestCriteria criteria =
        VMessageSearchRequestCriteria.builder().subArea(subArea).build();
    Page<MessageSearchResultDto> results = messageService.searchMessage(criteria, pageRequest);
    Assert.assertThat(results.getContent().size(), Matchers.greaterThan(1));
    Assert.assertThat(results.getContent().get(0).getSubArea(), Matchers.is(subArea));
  }

  @Test
  public void searchMessages_shouldReturnMessages_givenMessageLocationValue() throws Exception {
    String locationValue = "derendinger-at";
    PageRequest pageRequest = PageRequest.of(0, 10);
    VMessageSearchRequestCriteria criteria =
        VMessageSearchRequestCriteria.builder().locationValue(locationValue).build();
    Page<MessageSearchResultDto> results = messageService.searchMessage(criteria, pageRequest);
    Assert.assertThat(results.getContent().size(), Matchers.is(4));
    Assert.assertThat(StringUtils.trim(results.getContent().get(0).getLocationValue()),
        Matchers.is(locationValue));
  }

  @Test
  public void searchMessages_shouldReturnMessages_givenMessageTypeAndSearchDescByTitle() throws Exception {
    String type = "BANNER";
    PageRequest pageRequest = PageRequest.of(0, 10);
    VMessageSearchRequestCriteria criteria =
        VMessageSearchRequestCriteria.builder().type(type).orderDescByTitle(true).build();
    Page<MessageSearchResultDto> results = messageService.searchMessage(criteria, pageRequest);
    Assert.assertThat(results.getContent().size(), Matchers.greaterThan(1));
    Assert.assertThat(results.getContent().get(0).getTitle(), Matchers.is("ms-006"));
    Assert.assertThat(results.getContent().get(1).getTitle(), Matchers.is("ms-005"));
  }

  @Test
  public void searchMessages_shouldReturnMessages_givenMessageTypeAndSearchAcescByTitle() throws Exception {
    String type = "BANNER";
    PageRequest pageRequest = PageRequest.of(0, 10);
    VMessageSearchRequestCriteria criteria =
        VMessageSearchRequestCriteria.builder().type(type).orderDescByTitle(false).build();
    Page<MessageSearchResultDto> results = messageService.searchMessage(criteria, pageRequest);
    Assert.assertThat(results.getContent().size(), Matchers.greaterThan(1));
    Assert.assertThat(results.getContent().get(0).getTitle(), Matchers.is("ms-001"));
    Assert.assertThat(results.getContent().get(1).getTitle(), Matchers.is("ms-002"));
  }

  @Test
  public void searchMessages_shouldReturnAllMessages_givenNoSearchConditions() throws Exception {
    PageRequest pageRequest = PageRequest.of(0, 10);
    VMessageSearchRequestCriteria criteria =
        VMessageSearchRequestCriteria.builder().build();
    Page<MessageSearchResultDto> results = messageService.searchMessage(criteria, pageRequest);
    Assert.assertThat(results.getContent().size(), Matchers.greaterThan(1));
  }

  @Test
  public void getFilterOptionsMasterData_shouldGetFilteringOptions_givenNothing() throws Exception {
    MessageFilterOptionsMasterData options = messageService.getFilterOptionsMasterData();

    Assert.assertThat(options.getTypes().get(0).getType(), Matchers.is("BANNER"));
    Assert.assertThat(options.getTypes().get(1).getType(), Matchers.is("PANEL"));

    Assert.assertThat(options.getAreas().get(0).getArea(), Matchers.is("LOGIN_PAGE"));
    Assert.assertThat(options.getAreas().get(1).getArea(),
        Matchers.is("CUSTOMER_HOME_PAGE"));
  }

  @Test
  public void findById_shouldGetCorrespondingMessage_givenMessageId() throws Exception {
    MessageResultDto message = messageService.findById(1L);
    Assert.assertThat(message.getLocationTypeId(), Matchers.is(1));
    Assert.assertThat(message.getAffiliateShortName(), Matchers.is("derendinger-at"));
    Assert.assertNull(message.getCustomerNr());
    Assert.assertThat(message.getAccessRightId(), Matchers.is(2));
    Assert.assertThat(message.getTypeId(), Matchers.is(1));
    Assert.assertThat(message.getAreaId(), Matchers.is(1));
    Assert.assertThat(message.getSubAreaId(), Matchers.is(1));
    Assert.assertThat(message.getStyleId(), Matchers.is(1));
    Assert.assertThat(message.getVisibilityId(), Matchers.is(1));
  }

}
