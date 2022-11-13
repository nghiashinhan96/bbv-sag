package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.LanguageRepository;
import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.api.SupportedAffiliateRepository;
import com.sagag.eshop.repo.api.message.MessageAccessRightRepository;
import com.sagag.eshop.repo.api.message.MessageAreaRepository;
import com.sagag.eshop.repo.api.message.MessageHidingRepository;
import com.sagag.eshop.repo.api.message.MessageLanguageRepository;
import com.sagag.eshop.repo.api.message.MessageLocationRelationRepository;
import com.sagag.eshop.repo.api.message.MessageLocationRepository;
import com.sagag.eshop.repo.api.message.MessageLocationTypeRepository;
import com.sagag.eshop.repo.api.message.MessageRepository;
import com.sagag.eshop.repo.api.message.MessageStyleRepository;
import com.sagag.eshop.repo.api.message.MessageSubAreaRepository;
import com.sagag.eshop.repo.api.message.MessageTypeRepository;
import com.sagag.eshop.repo.api.message.MessageVisibilityRepository;
import com.sagag.eshop.repo.api.message.VMessageRepository;
import com.sagag.eshop.repo.entity.message.Message;
import com.sagag.eshop.repo.entity.message.MessageAccessRight;
import com.sagag.eshop.repo.entity.message.MessageHiding;
import com.sagag.eshop.repo.entity.message.MessageLanguage;
import com.sagag.eshop.repo.entity.message.MessageLocation;
import com.sagag.eshop.repo.entity.message.MessageLocationRelation;
import com.sagag.eshop.repo.entity.message.MessageSubArea;
import com.sagag.eshop.repo.entity.message.VMessage;
import com.sagag.eshop.repo.specification.VMessageSpecifications;
import com.sagag.eshop.service.affiliate.SupportedAffiliatePredicate;
import com.sagag.eshop.service.api.MessageService;
import com.sagag.eshop.service.converter.message.MessageAreaConvert;
import com.sagag.eshop.service.converter.message.MessageLanguageConverter;
import com.sagag.eshop.service.converter.message.MessageLocationTypeConverter;
import com.sagag.eshop.service.converter.message.MessageTypeConverter;
import com.sagag.eshop.service.converter.message.VMessageConverters;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.enums.MessageLocationTypeEnum;
import com.sagag.eshop.service.exception.CustomerNotFoundException;
import com.sagag.eshop.service.exception.MessageTimeOverlapException;
import com.sagag.eshop.service.utils.MessageLocationTypeComparator;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.criteria.VMessageSearchCriteria;
import com.sagag.services.domain.eshop.criteria.VMessageSearchRequestCriteria;
import com.sagag.services.domain.eshop.dto.LanguageDto;
import com.sagag.services.domain.eshop.message.dto.MessageDto;
import com.sagag.services.domain.eshop.message.dto.MessageFilterOptionsMasterData;
import com.sagag.services.domain.eshop.message.dto.MessageLanguageDto;
import com.sagag.services.domain.eshop.message.dto.MessageLocationDto;
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

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * Implementation class of {@link MessageService}.
 *
 */
@Transactional
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

  private static final String MESSAGE_IS_OVERLAP = "Message is overlap time, Please picking another period";

  @Autowired
  private MessageLocationTypeRepository messageLocationTypeRepo;

  @Autowired
  private MessageTypeRepository messageTypeRepo;

  @Autowired
  private MessageAreaRepository messageAreaRepo;

  @Autowired
  private MessageStyleRepository messageStyleRepo;

  @Autowired
  private MessageVisibilityRepository messageVisibilityRepo;

  @Autowired
  private MessageLocationRepository messageLocationRepo;

  @Autowired
  private MessageAccessRightRepository messageAccessRightRepo;

  @Autowired
  private MessageSubAreaRepository messageSubAreaRepo;

  @Autowired
  private MessageRepository messageRepo;

  @Autowired
  private SupportedAffiliateRepository supportedAffiliateRepo;

  @Autowired
  private LanguageRepository languageRepo;

  @Autowired
  private MessageLanguageRepository messageLanguageRepo;

  @Autowired
  private OrganisationRepository organisationRepo;

  @Autowired
  private MessageHidingRepository messageHidingRepo;

  @Autowired
  private VMessageRepository vMessageRepo;

  @Autowired
  private MessageLocationRelationRepository messageLocationRelationRepo;

  @Autowired
  private SupportedAffiliatePredicate supportedAffiliatePredicate;

  @Override
  public MessageMasterDataDto getMasterData() {
    // @formatter:off
    List<MessageLocationTypeDto> locationTypes =
        MessageLocationTypeConverter.fromEntities(messageLocationTypeRepo.findAll());

    List<MessageTypeDto> messageTypes =
        CollectionUtils.emptyIfNull(messageTypeRepo.findAll())
        .stream()
        .map(item -> SagBeanUtils.map(item, MessageTypeDto.class))
        .collect(Collectors.toList());

    List<MessageStyleDto> styles =
        CollectionUtils.emptyIfNull(messageStyleRepo.findAll())
        .stream()
        .map(item -> SagBeanUtils.map(item, MessageStyleDto.class))
        .collect(Collectors.toList());

    List<MessageVisibilityDto> visibilities =
        CollectionUtils.emptyIfNull(messageVisibilityRepo.findAll())
        .stream()
        .map(item -> SagBeanUtils.map(item, MessageVisibilityDto.class))
        .collect(Collectors.toList());

    List<LanguageDto> supportedLanguages =
        CollectionUtils.emptyIfNull(languageRepo.findAll())
        .stream()
        .map(item -> SagBeanUtils.map(item, LanguageDto.class))
        .collect(Collectors.toList());

    List<SupportedAffiliateDto> affiliates =
        ListUtils.emptyIfNull(supportedAffiliateRepo.findAllSupportedAffiliate())
        .stream()
        .filter(supportedAffiliatePredicate)
        .collect(Collectors.toList());

    return MessageMasterDataDto.builder()
        .locationTypes(locationTypes)
        .types(messageTypes)
        .styles(styles)
        .visibilities(visibilities)
        .supportedAffiliates(affiliates)
        .supportedLanguages(supportedLanguages)
        .build();
    // @formatter:on
  }

  @Override
  public void create(MessageSavingRequestDto model, Long createdUserId)
      throws MessageTimeOverlapException, CustomerNotFoundException {
    validateSavingRequestDto(model);

    Message message = Message.builder()
        .title(model.getTitle())
        .messageAccessRight(messageAccessRightRepo.getOne(model.getAccessRightId()))
        .messageType(messageTypeRepo.findById(model.getTypeId()).orElse(null))
        .messageSubArea(messageSubAreaRepo.findById(model.getSubAreaId()).orElse(null))
        .messageStyle(messageStyleRepo.findById(model.getStyleId()).orElse(null))
        .messageVisibility(messageVisibilityRepo.findById(model.getVisibilityId()).orElse(null))
        .active(model.getActive())
        .ssoTraining(BooleanUtils.isTrue(model.getSsoTraining()))
        .dateValidFrom(getDateFrom(model.getDateValidFrom()))
        .dateValidTo(getDateTo(model.getDateValidTo()))
        .createdUserId(createdUserId)
        .createdDate(Calendar.getInstance().getTime())
        .build();
    Message createdMessage = messageRepo.save(message);

    List<MessageLocation> createdMessageLocations = createMessageLocation(model.getMessageLocation());
    createMessageAndMessageLocationRelationship(createdMessageLocations, createdMessage);

    List<MessageLanguage> languages = model.getMessageLanguages()
        .stream()
        .map(item -> MessageLanguage.builder()
            .langIso(item.getLangIso())
            .content(item.getContent())
            .message(createdMessage).build())
        .collect(Collectors.toList());
    messageLanguageRepo.saveAll(languages);
  }

  private void createMessageAndMessageLocationRelationship(List<MessageLocation> messageLocation, Message message) {
    List<MessageLocationRelation> locationRelations = messageLocation.stream()
        .map(item -> MessageLocationRelation.builder()
            .message(message)
            .messageLocation(item)
            .build())
        .collect(Collectors.toList());
    messageLocationRelationRepo.saveAll(locationRelations);
  }

  @Override
  public void update(MessageSavingRequestDto model, Long messasgeId, Long updatedUserId)
      throws MessageTimeOverlapException, CustomerNotFoundException {
    validateSavingRequestDto(model, messasgeId);
    Message message = messageRepo.findById(messasgeId).orElseThrow(
        () -> new IllegalArgumentException("Message not found with id = " + messasgeId));

    destroyMessageAndMessageLocationRelationship(message);

    message.setTitle(model.getTitle());
    message.setActive(model.getActive());
    message.setSsoTraining(model.getSsoTraining());
    message.setModifiedUserId(updatedUserId);
    message.setModifiedDate(Calendar.getInstance().getTime());
    message.setDateValidFrom(getDateFrom(model.getDateValidFrom()));
    message.setDateValidTo(getDateTo(model.getDateValidTo()));
    message.setMessageAccessRight(messageAccessRightRepo.findById(model.getAccessRightId()).orElse(null));
    message.setMessageType(messageTypeRepo.findById(model.getTypeId()).orElse(null));
    message.setMessageSubArea(messageSubAreaRepo.findById(model.getSubAreaId()).orElse(null));
    message.setMessageStyle(messageStyleRepo.findById(model.getStyleId()).orElse(null));
    message.setMessageVisibility(messageVisibilityRepo.findById(model.getVisibilityId()).orElse(null));
    Message createdMessage = messageRepo.save(message);

    List<MessageLocation> createdMessageLocations = createMessageLocation(model.getMessageLocation());
    createMessageAndMessageLocationRelationship(createdMessageLocations, createdMessage);

    List<MessageLanguageDto> languageDtos = model.getMessageLanguages();
    List<MessageLanguage> languages = message.getMessageLanguages();
    languages.forEach(language -> {
      Optional<MessageLanguageDto> dto = findLanguage(languageDtos, language);
      dto.ifPresent(item -> language.setContent(item.getContent()));
    });
    messageLanguageRepo.saveAll(languages);
  }

  private List<MessageLocation> createMessageLocation(MessageLocationDto messageLocationDto) {
    List<MessageLocation> messageLocations = messageLocationDto.getLocationValues().stream()
        .map(item -> MessageLocation.builder()
            .messageLocationType(messageLocationTypeRepo.findById(messageLocationDto.getLocationTypeId()).orElse(null))
            .value(item)
            .build())
        .collect(Collectors.toList());
    return messageLocationRepo.saveAll(messageLocations);
  }

  private void destroyMessageAndMessageLocationRelationship(Message message) {
    List<MessageLocationRelation> deletedLocationRelations = messageLocationRelationRepo.deleteByMessage(message);
    messageLocationRepo.deleteAll(deletedLocationRelations.stream()
        .map(MessageLocationRelation::getMessageLocation).collect(Collectors.toList()));
  }

  private Optional<MessageLanguageDto> findLanguage(List<MessageLanguageDto> dtos, MessageLanguage entity) {
    return dtos.stream().filter(item -> item.getLangIso().equals(entity.getLangIso())).findFirst();
  }

  private void validateSavingRequestDto(MessageSavingRequestDto model)
      throws MessageTimeOverlapException, CustomerNotFoundException {
    validMandatoryFields(model);
    MessageLocationDto location = model.getMessageLocation();
    if (!messageRepo.isValidPeriod(
        location.getLocationTypeId(),
        location.getLocationValues(),
        model.getAccessRightId(),
        model.getSubAreaId(),
        model.getTypeId(),
        getDateFrom(model.getDateValidFrom()),
        getDateTo(model.getDateValidTo()))) {
      throw new MessageTimeOverlapException(MESSAGE_IS_OVERLAP);
    }
  }

  private void validateSavingRequestDto(MessageSavingRequestDto model, Long messageId)
      throws MessageTimeOverlapException, CustomerNotFoundException {
    Assert.notNull(messageId, "Message id must not be null");
    validMandatoryFields(model);
    MessageLocationDto location = model.getMessageLocation();
    if (!messageRepo.isValidPeriod(
        location.getLocationTypeId(),
        location.getLocationValues(),
        model.getAccessRightId(),
        model.getSubAreaId(),
        model.getTypeId(),
        getDateFrom(model.getDateValidFrom()),
        getDateTo(model.getDateValidTo()),
        messageId)) {
      throw new MessageTimeOverlapException(MESSAGE_IS_OVERLAP);
    }
  }


  private void validMandatoryFields(MessageSavingRequestDto model)
      throws CustomerNotFoundException {
    String locationType =
        messageLocationTypeRepo.findLocationTypeById(model.getLocationTypeId())
            .orElseThrow(() -> new IllegalArgumentException("Location type does not exist"));

    if (MessageLocationTypeEnum.CUSTOMER.name().equals(locationType)
        && !organisationRepo.isExistedByOrgCode(model.getLocationValue())) {
      throw new CustomerNotFoundException("Customer not found");
    }

    Assert.notNull(model.getMessageLocation(), "Location must not be null");
    String locationTypeNew =
        messageLocationTypeRepo.findLocationTypeById(model.getMessageLocation().getLocationTypeId())
            .orElseThrow(() -> new IllegalArgumentException("Location type does not exist"));
    Assert.notEmpty(model.getMessageLocation().getLocationValues(), "Location values must not be null");
    if (MessageLocationTypeEnum.CUSTOMER.name().equals(locationTypeNew)
        && !organisationRepo.isExistedByOrgCode(getOrgCodeFromMessageLocation(model.getMessageLocation()))) {
      throw new CustomerNotFoundException("Customer not found");
    }

    Assert.notNull(model.getTitle(), "Title must not be null");
    Assert.notNull(model.getLocationTypeId(), "Location type id must not be null");
    Assert.notNull(model.getAccessRightId(), "Access right id must not be null");
    Assert.notNull(model.getTypeId(), "Type id must not be null");
    Assert.notNull(model.getSubAreaId(), "Sub area id must not be null");
    Assert.notNull(model.getStyleId(), "Style id must not be null");
    Assert.notNull(model.getVisibilityId(), "Visibility id must not be null");
    Assert.notNull(model.getDateValidFrom(), "Date valid from must not be null");
    Assert.notNull(model.getDateValidTo(), "Date valid to must not be null");

    Date fromDate = getDateFrom(model.getDateValidFrom());
    Date toDate = getDateTo(model.getDateValidTo());
    Assert.isTrue(fromDate.before(toDate),
        "Date valid from must be before date valid to");

    List<MessageLanguageDto> messageLanguages = model.getMessageLanguages();
    Assert.notEmpty(messageLanguages, "Message language must not be empty");
    messageLanguages.forEach(item -> {
      Assert.hasText(item.getLangIso(), "Language code must not be empty");
      Assert.hasText(item.getContent(), "Content must must not be empty");
    });
  }

  private String getOrgCodeFromMessageLocation(MessageLocationDto messageLocation) {
    if(CollectionUtils.isEmpty(messageLocation.getLocationValues()) || messageLocation.getLocationValues().size() != 1) {
      throw new IllegalArgumentException("Invalid Customer code");
    }
    return messageLocation.getLocationValues().get(0);
  }

  @Override
  public List<MessageDto> getUnauthorizedMessages(final String affiliate, final String langIso) {
    Assert.hasText(affiliate, "Affiliate should not be empty!");
    Assert.hasText(langIso, "LangIso should not be empty!");
    final long start = System.currentTimeMillis();
    List<MessageDto> messageDtos =
        messageRepo.findNoAuthedMessages(langIso.toUpperCase(), affiliate);
    log.debug("Perf:MessageServiceImpl->getUnauthorizedMessages->Find unauthorized messages {} ms",
        System.currentTimeMillis() - start);
    return messageDtos;
  }

  @Override
  public List<MessageDto> getAuthorizedMessages(UserInfo user, String affiliate) {
    String searchAffliliate = user.getAffiliateShortName();
    List<String> roles = new ArrayList<>();
    if (user.isSaleOnBehalf() || user.isSalesUser()) {
      Assert.hasText(affiliate, "Affiliate is required for sale.");
      searchAffliliate = affiliate;
      roles.add(EshopAuthority.SALES_ASSISTANT.name());
    } else {
      roles.addAll(user.getRoles());
    }
    final long start = System.currentTimeMillis();
    List<MessageDto> messageDtos =
        messageRepo.findAuthedMessages(roles, user.getLanguage(), searchAffliliate,
            user.getCustNrStr());
    log.debug("Perf:MessageServiceImpl->getAuthorizedMessages->Find authorized messages {} ms",
        System.currentTimeMillis() - start);

    List<Long> hiddingMessageIds = messageHidingRepo.findHidingMessagesByUser(user.getId());

    List<MessageDto> displayedMessages = messageDtos.stream().filter(m -> !hiddingMessageIds.contains(m.getId()))
        .collect(Collectors.toList());


    return filterOverlapMessageLocation(displayedMessages);
  }

  private static List<MessageDto> filterOverlapMessageLocation(List<MessageDto> messageDtos) {
    return messageDtos.stream().collect(Collectors.collectingAndThen(
        Collectors.toMap(x -> Arrays.asList(x.getType(), x.getArea(), x.getSubArea(), x.getSort()),
            x -> x, BinaryOperator.minBy(new MessageLocationTypeComparator())),
        map -> new ArrayList<>(map.values())));
  }

  private static Date getDateFrom(String dateFrom) {
    return getDate(dateFrom + StringUtils.SPACE + DateUtils.BEGIN_OF_DAY);
  }

  private static Date getDateTo(String dateTo) {
    return getDate(dateTo + StringUtils.SPACE + DateUtils.END_OF_DAY_IGNORE_MILISECONDS);
  }

  private static Date getDate(String date) {
    return DateUtils.toDate(date, DateUtils.SWISS_DATE_TIME_PATTERN);
  }

  @Override
  public void hideMessages(Long userId, String messageIds) {
    Assert.hasText(messageIds, "There is no message to hide");
    messageIds =
        messageIds.trim().replaceAll(SagConstants.COMMA_NO_SPACE + SagConstants.DOLLAR,
            StringUtils.EMPTY);
    List<Long> messageIdList =
        Arrays.asList(messageIds.split(SagConstants.COMMA_NO_SPACE)).stream()
            .map(str -> Long.parseLong(str.trim())).collect(Collectors.toList());
    List<Long> hiddenMessageIdList = messageHidingRepo.findHidingMessagesByUser(userId);
    messageIdList.removeIf(hiddenMessageIdList::contains);
    for (Long id : messageIdList) {
      messageHidingRepo.save(MessageHiding.builder().userId(userId).messageId(id).build());
    }
  }

  @Override
  public Page<MessageSearchResultDto> searchMessage(VMessageSearchRequestCriteria requestCriteria,
      Pageable pageable) {
    // @formatter:off
    VMessageSearchCriteria searchCriteria =
        VMessageSearchCriteria.builder().title(requestCriteria.getTitle())
            .type(requestCriteria.getType()).area(requestCriteria.getArea())
            .subArea(requestCriteria.getSubArea())
            .locationValue(requestCriteria.getLocationValue()).active(requestCriteria.getActive())
            .orderDescByTitle(requestCriteria.getOrderDescByTitle())
            .orderDescByType(requestCriteria.getOrderDescByType())
            .orderDescByArea(requestCriteria.getOrderDescByArea())
            .orderDescBySubArea(requestCriteria.getOrderDescBySubArea())
            .orderDescByActive(requestCriteria.getOrderDescByActive())
            .orderDescByDateValidFrom(requestCriteria.getOrderDescByDateValidFrom())
            .orderDescByDateValidTo(requestCriteria.getOrderDescByDateValidTo())
            .build();

    Specification<VMessage> spec = VMessageSpecifications.searchMessages(searchCriteria);
    return vMessageRepo.findAll(spec, pageable).map(VMessageConverters.toDto());
    // @formatter:on
  }

  @Override
  public MessageFilterOptionsMasterData getFilterOptionsMasterData() {
    return MessageFilterOptionsMasterData.builder()
        .types(MessageTypeConverter.fromEntities(messageTypeRepo.findAll()))
        .areas(MessageAreaConvert.fromEntities(messageAreaRepo.findAll())).build();
  }

  @Override
  public void deleteMessage(Long messageId) {
    Assert.notNull(messageId, "Message id must not be null");

    Message message = this.messageRepo.findById(messageId).orElseThrow(
        () -> new IllegalArgumentException("Message not found with id = " + messageId));
    messageLanguageRepo.deleteAll(message.getMessageLanguages());
    messageHidingRepo.deleteAll(message.getMessageHidings());
    destroyMessageAndMessageLocationRelationship(message);
    messageRepo.delete(message);
  }

  @Override
  public MessageResultDto findById(Long id) {
    Message entity = messageRepo.findById(id).orElse(null);
    validateMessage(entity);

    MessageSubArea subArea = entity.getMessageSubArea();
    MessageAccessRight accessRight = entity.getMessageAccessRight();

    List<MessageLocation> locations = entity.getMessageLocationRelation().stream()
        .map(MessageLocationRelation::getMessageLocation)
        .collect(Collectors.toList());
    MessageLocation firstMessageLocation = locations.get(0);
    MessageLocationDto messageLocation = MessageLocationDto.builder()
        .locationTypeId(firstMessageLocation.getId())
        .locationValues(locations.stream().map(MessageLocation::getValue).collect(Collectors.toList()))
        .build();
    MessageLocationTypeEnum locationType =
        MessageLocationTypeEnum.fromDesc(firstMessageLocation.getMessageLocationType().getLocationType());
    return MessageResultDto
        .builder()
        .locationTypeId(firstMessageLocation.getMessageLocationType().getId())
        .accessRightId(accessRight.getId())
        .roleTypeId(accessRight.getMessageRoleType().getId())
        .typeId(entity.getMessageType().getId())
        .visibilityId(entity.getMessageVisibility().getId())
        .styleId(entity.getMessageStyle().getId())
        .subAreaId(subArea.getId())
        .areaId(subArea.getMessageArea().getId())
        .messageLanguages(MessageLanguageConverter.fromEntities(entity.getMessageLanguages()))
        .id(entity.getId())
        .active(entity.getActive())
        .ssoTraining(entity.getSsoTraining())
        .title(entity.getTitle())
        .dateValidFrom(entity.getDateValidFrom())
        .dateValidTo(entity.getDateValidTo())
        .messageLocation(messageLocation)
        .affiliateShortName(locationType.isUseAffiliate() ? firstMessageLocation.getValue() : null)
        .customerNr(locationType.isUseCustomerNr() ? firstMessageLocation.getValue() : null)
        .build();
  }

  private void validateMessage(Message entity) {
    if (Objects.isNull(entity)) {
      throw new IllegalArgumentException("Message does not exist");
    }

    if (CollectionUtils.isEmpty(entity.getMessageLocationRelation())) {
      throw new IllegalArgumentException("Message location does not exist");
    }

    MessageAccessRight messageAccessRight = entity.getMessageAccessRight();
    if (Objects.isNull(messageAccessRight)) {
      throw new IllegalArgumentException("Message access right does not exist");
    }
    if (Objects.isNull(messageAccessRight.getMessageRoleType())) {
      throw new IllegalArgumentException("Message role type does not exist");
    }
    if (Objects.isNull(entity.getMessageType())) {
      throw new IllegalArgumentException("Message type does not exist");
    }
    if (Objects.isNull(entity.getMessageVisibility())) {
      throw new IllegalArgumentException("Message visibility does not exist");
    }
    if (Objects.isNull(entity.getMessageStyle())) {
      throw new IllegalArgumentException("Message style does not exist");
    }

    MessageSubArea messageSubArea = entity.getMessageSubArea();
    if (Objects.isNull(messageSubArea)) {
      throw new IllegalArgumentException("Message sub area does not exist");
    }
    if (Objects.isNull(messageSubArea.getMessageArea())) {
      throw new IllegalArgumentException("Message area does not exist");
    }
  }
}
