package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.offer.OfferPersonRepository;
import com.sagag.eshop.repo.api.offer.ViewOfferPersonRepository;
import com.sagag.eshop.repo.criteria.offer.OfferPersonSearchCriteria;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.offer.OfferAddress;
import com.sagag.eshop.repo.entity.offer.OfferPerson;
import com.sagag.eshop.repo.entity.offer.OfferPersonProperty;
import com.sagag.eshop.repo.entity.offer.ViewOfferPerson;
import com.sagag.eshop.repo.specification.offer.VOfferPersonSpecifications;
import com.sagag.eshop.service.api.OfferPersonService;
import com.sagag.eshop.service.converter.OfferPersonConverters;
import com.sagag.eshop.service.converter.ViewOfferPersonConverters;
import com.sagag.eshop.service.dto.offer.OfferPersonDto;
import com.sagag.eshop.service.dto.offer.ViewOfferPersonDto;
import com.sagag.services.common.enums.offer.OfferAddressType;
import com.sagag.services.common.enums.offer.OfferCountryIsoType;
import com.sagag.services.common.enums.offer.OfferPersonPropertyType;
import com.sagag.services.common.enums.offer.OfferPersonStatus;
import com.sagag.services.common.enums.offer.OfferPersonType;
import com.sagag.services.common.enums.offer.OfferTecStateType;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation class of offer person.
 */
@Service
@Slf4j
@Transactional
public class OfferPersonServiceImpl implements OfferPersonService {

  private static final String THE_GIVEN_ORGANISATION_ID_MUST_NOT_BE_NULL_MSG =
      "The given organisation id must not be null";

  private static final String THE_GIVEN_SEARCH_CRITERIA_MUST_NOT_BE_NULL_MSG =
      "The given search criteria must not be null";

  private static final String NOT_FOUND_OFFER_DETAILS_INFO_MSG = "Not found end customer has id %s";

  private static final String NOT_FOUND_OFFER_ADDRESS_INFO_MSG =
      "Not found address of customer has id %s";

  private static final String NOT_FOUND_ANY_END_CUSTOMER_MSG = "Not found any end customer";

  private static final String NOT_FOUND_ANY_ESHOP_USERS_MSG = "Not found any eshop users";

  private static final String OFFER_PERSON_ID_MUST_NOT_BE_NULL_MSG =
      "The offer person id must not be null";

  private static final String OFFER_PERSON_MUST_NOT_BE_NULL_MSG =
      "The offer person must not be null";

  private static final String MODIFIED_USER_ID_MUST_NOT_BE_NULL_MSG =
      "The modified user id must not be null";

  private static final int FIRST_VERSION = 1;

  @Autowired
  private OfferPersonRepository offerPersonRepo;

  @Autowired
  private ViewOfferPersonRepository viewOfferPersonRepo;

  @Autowired
  private EshopUserRepository eshopUserRepo;

  @Override
  public OfferPersonDto createOfferPerson(final OfferPersonDto offerPersonDto) {

    final Long currentUserId = offerPersonDto.getCurrentUserId();
    final EshopUser eshopUser = eshopUserRepo.findById(currentUserId).orElse(null);
    if (eshopUser == null) {
      throw new NoSuchElementException(NOT_FOUND_ANY_ESHOP_USERS_MSG);
    }

    final Date now = Calendar.getInstance().getTime();

    // Build offer person
    final OfferPerson offerPerson = OfferPerson.builder()
        .organisationId(offerPersonDto.getOrganisationId()).type(OfferPersonType.ADDRESSEE.name())
        .status(OfferPersonStatus.ACTIVE.toString())
        .offerCompanyName(offerPersonDto.getCompanyName()).firstName(offerPersonDto.getFirstName())
        .lastName(offerPersonDto.getLastName()).email(offerPersonDto.getEmail())
        .languageId(eshopUser.getLanguage().getId()).hourlyRate(eshopUser.getHourlyRate())
        .createdUserId(currentUserId).createdDate(now).modifiedUserId(currentUserId)
        .modifiedDate(now).tecstate(OfferTecStateType.ACTIVE.name()).version(FIRST_VERSION).build();

    final OfferPerson offerPersonEntity = offerPersonRepo.save(offerPerson);
    final long offerPersonId = offerPersonEntity.getId();

    // Build offer person property
    final Set<OfferPersonProperty> offerPersonProperties = new HashSet<>();
    if (StringUtils.isNotBlank(offerPersonDto.getPhone())) {
      offerPersonProperties.add(createOfferPersonProperty(offerPersonId,
          OfferPersonPropertyType.PHONE.getValue(), offerPersonDto.getPhone()));
    }
    if (StringUtils.isNotBlank(offerPersonDto.getFax())) {
      offerPersonProperties.add(createOfferPersonProperty(offerPersonId,
          OfferPersonPropertyType.FAX.getValue(), offerPersonDto.getFax()));
    }
    if (!Objects.isNull(offerPersonDto.getSalutation())) {
      offerPersonProperties.add(
          createOfferPersonProperty(offerPersonId, OfferPersonPropertyType.SALUTATION.getValue(),
              String.valueOf(offerPersonDto.getSalutation())));
    }

    // Build offer address
    final OfferAddress offerAddress = createOfferAddress(offerPersonId, offerPersonDto, now);

    offerPerson.setOfferAddress(offerAddress);
    offerPerson.setProperties(offerPersonProperties);

    return OfferPersonConverters.convert(offerPersonRepo.save(offerPerson));
  }

  @Override
  public Page<ViewOfferPersonDto> searchOfferPersons(final OfferPersonSearchCriteria criteria,
      final Pageable pageable) {
    log.debug("Get offer persons by criteria = {}, pageable = {}", criteria, pageable);

    Assert.notNull(criteria, THE_GIVEN_SEARCH_CRITERIA_MUST_NOT_BE_NULL_MSG);
    Assert.notNull(criteria.getOrganisationId(), THE_GIVEN_ORGANISATION_ID_MUST_NOT_BE_NULL_MSG);

    final Specification<ViewOfferPerson> specOfferPerson =
        VOfferPersonSpecifications.of(criteria);

    final long start = System.currentTimeMillis();
    final Page<ViewOfferPerson> results = viewOfferPersonRepo.findAll(specOfferPerson, pageable);
    log.debug("Perf:OfferPersonServiceImpl-> searchfferPersons-> findAll  {} ms",
        System.currentTimeMillis() - start);
    if (results == null) {
      throw new NoSuchElementException(NOT_FOUND_ANY_END_CUSTOMER_MSG);
    }
    return results.map(ViewOfferPersonConverters.optionalOfferPersonConverter());
  }

  private OfferAddress createOfferAddress(final Long offerPersonId,
      final OfferPersonDto offerPerson, final Date time) {
    final Long currentUserId = offerPerson.getCurrentUserId();
    return OfferAddress.builder().personId(offerPersonId).line1(offerPerson.getRoad())
        .line2(offerPerson.getAdditionalAddress1()).line3(offerPerson.getAdditionalAddress2())
        .countryiso(OfferCountryIsoType.CH.name()).zipcode(offerPerson.getPostCode())
        .city(offerPerson.getPlace()).tecstate(OfferTecStateType.ACTIVE.name())
        .type(OfferAddressType.DEFAULT.name()).createdUserId(currentUserId).createdDate(time)
        .modifiedUserId(currentUserId).modifiedDate(time).poBox(offerPerson.getPoBox())
        .version(FIRST_VERSION).build();
  }

  private static OfferPersonProperty createOfferPersonProperty(final Long offerPersonId,
      final String type, final String value) {
    return OfferPersonProperty.builder().personId(offerPersonId).type(type).value(value).build();
  }

  @Override
  public OfferPersonDto editOfferPerson(final OfferPersonDto offerPersonDto) {

    log.debug("Edit the existing offer person = {}", offerPersonDto);

    Assert.notNull(offerPersonDto, OFFER_PERSON_MUST_NOT_BE_NULL_MSG);
    Assert.notNull(offerPersonDto.getId(), OFFER_PERSON_ID_MUST_NOT_BE_NULL_MSG);

    final Long offerPersonId = offerPersonDto.getId();
    final Long currentUserId = offerPersonDto.getCurrentUserId();
    final Date now = Calendar.getInstance().getTime();

    final EshopUser eshopUser = eshopUserRepo.findById(currentUserId).orElse(null);
    if (eshopUser == null) {
      throw new NoSuchElementException(NOT_FOUND_ANY_ESHOP_USERS_MSG);
    }

    final OfferPerson offerPerson = offerPersonRepo.findById(offerPersonId).orElse(null);
    if (offerPerson == null) {
      throw new NoSuchElementException(
          String.format(NOT_FOUND_OFFER_DETAILS_INFO_MSG, offerPersonId));
    }

    // Update offer address
    offerPerson
        .setOfferAddress(updateOfferAddress(offerPerson.getOfferAddress(), offerPersonDto, now));

    // Update offer person property info
    offerPerson
        .setProperties(updateOfferPersonProperties(offerPerson.getProperties(), offerPersonDto));

    // Update offer person
    offerPerson.setOfferCompanyName(offerPersonDto.getCompanyName());
    offerPerson.setFirstName(offerPersonDto.getFirstName());
    offerPerson.setLastName(offerPersonDto.getLastName());
    offerPerson.setEmail(offerPersonDto.getEmail());
    offerPerson.setLanguageId(eshopUser.getLanguage().getId());
    offerPerson.setModifiedUserId(currentUserId);
    offerPerson.setModifiedDate(now);
    offerPerson.setVersion(offerPerson.getVersion() + NumberUtils.INTEGER_ONE);

    return OfferPersonConverters.convert(offerPersonRepo.save(offerPerson));
  }

  private OfferAddress updateOfferAddress(final OfferAddress offerAddress,
      final OfferPersonDto offerPersonDto, final Date time) {

    if (offerAddress == null) {
      throw new NoSuchElementException(
          String.format(NOT_FOUND_OFFER_ADDRESS_INFO_MSG, offerPersonDto.getId()));
    }

    offerAddress.setLine1(offerPersonDto.getRoad());
    offerAddress.setLine2(offerPersonDto.getAdditionalAddress1());
    offerAddress.setLine3(offerPersonDto.getAdditionalAddress2());
    offerAddress.setZipcode(offerPersonDto.getPostCode());
    offerAddress.setCity(offerPersonDto.getPlace());
    offerAddress.setModifiedUserId(offerPersonDto.getCurrentUserId());
    offerAddress.setModifiedDate(time);
    offerAddress.setPoBox(offerPersonDto.getPoBox());
    offerAddress.setVersion(offerAddress.getVersion() + NumberUtils.INTEGER_ONE);
    return offerAddress;
  }

  private Set<OfferPersonProperty> updateOfferPersonProperties(
      final Set<OfferPersonProperty> offerPersonProperties, final OfferPersonDto offerPersonDto) {

    final Long offerPersonId = offerPersonDto.getId();

    final Map<String, OfferPersonProperty> properties = offerPersonProperties.stream()
        .collect(Collectors.toMap(OfferPersonProperty::getType, Function.identity()));

    Stream.of(OfferPersonPropertyType.values()).forEach(type -> {
      switch (type) {
        case PHONE:
          updateOfferPersonProperty(offerPersonId, properties,
              OfferPersonPropertyType.PHONE.getValue(), offerPersonDto.getPhone());
          break;
        case FAX:
          updateOfferPersonProperty(offerPersonId, properties,
              OfferPersonPropertyType.FAX.getValue(), offerPersonDto.getFax());
          break;
        case SALUTATION:
          updateOfferPersonProperty(offerPersonId, properties,
              OfferPersonPropertyType.SALUTATION.getValue(),
              String.valueOf(offerPersonDto.getSalutation()));
          break;
        default:
          // Do nothing for other cases
          break;
      }
    });

    return properties.values().stream().collect(Collectors.toSet());
  }

  private static void updateOfferPersonProperty(final Long offerPersonId,
      final Map<String, OfferPersonProperty> properties, final String type, final String value) {

    final String valueProp = StringUtils.defaultIfBlank(value, StringUtils.EMPTY);
    final OfferPersonProperty property = properties.get(type);

    final OfferPersonProperty updatedProperty =
        createOfferPersonProperty(offerPersonId, type, valueProp);

    if (Objects.isNull(property)) {
      // Add new property
      properties.put(type, updatedProperty);
    } else {
      // Update existing property
      properties.replace(type, property, updatedProperty);
    }
  }

  @Override
  public Optional<OfferPersonDto> getOfferPersonDetails(final Long offerPersonId) {
    log.debug("Get offer person details of id = {}", offerPersonId);
    Assert.notNull(offerPersonId, OFFER_PERSON_ID_MUST_NOT_BE_NULL_MSG);
    final OfferPerson offerPerson = offerPersonRepo.findById(offerPersonId).orElse(null);
    if (Objects.isNull(offerPerson)) {
      return Optional.empty();
    }
    return Optional.of(offerPerson).map(OfferPersonConverters.optionalOfferPersonConverter());
  }

  @Override
  public OfferPersonDto removeOfferPerson(final Long currentUserId, final Long offerPersonId) {
    log.debug("Remove the existing shop article id = {} by modified user id = ", offerPersonId,
        currentUserId);

    Assert.notNull(offerPersonId, OFFER_PERSON_ID_MUST_NOT_BE_NULL_MSG);
    Assert.notNull(currentUserId, MODIFIED_USER_ID_MUST_NOT_BE_NULL_MSG);

    final OfferPerson offerPerson = offerPersonRepo.findById(offerPersonId).orElse(null);
    if (offerPerson == null) {
      throw new NoSuchElementException(
          String.format(NOT_FOUND_OFFER_DETAILS_INFO_MSG, offerPersonId));
    }

    // Inactive offer person
    offerPerson.setTecstate(OfferTecStateType.INACTIVE.name());
    offerPerson.setStatus(OfferPersonStatus.INACTIVE.name());
    offerPerson.setModifiedDate(Calendar.getInstance().getTime());
    offerPerson.setModifiedUserId(currentUserId);

    // Inactive offer address
    final OfferAddress offerAddress = offerPerson.getOfferAddress();
    offerAddress.setTecstate(OfferTecStateType.INACTIVE.name());
    offerAddress.setModifiedDate(Calendar.getInstance().getTime());
    offerAddress.setModifiedUserId(currentUserId);
    offerPerson.setOfferAddress(offerAddress);

    return OfferPersonConverters.convert(offerPersonRepo.save(offerPerson));
  }
}
