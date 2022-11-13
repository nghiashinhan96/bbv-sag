package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.api.offer.OfferRepository;
import com.sagag.eshop.repo.api.offer.ViewOfferRepository;
import com.sagag.eshop.repo.criteria.offer.OfferSearchCriteria;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.offer.Offer;
import com.sagag.eshop.repo.entity.offer.ViewOffer;
import com.sagag.eshop.repo.specification.offer.VOfferSpecifications;
import com.sagag.eshop.service.api.OfferService;
import com.sagag.eshop.service.converter.OfferConverters;
import com.sagag.eshop.service.converter.ViewOfferConverters;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.offer.OfferDto;
import com.sagag.eshop.service.dto.offer.OfferGeneralDto;
import com.sagag.eshop.service.formatter.NumberFormatterContext;
import com.sagag.eshop.service.helper.OfferCalculationHelper;
import com.sagag.services.common.enums.offer.OfferStatus;
import com.sagag.services.common.enums.offer.OfferTecStateType;
import com.sagag.services.common.enums.offer.OfferType;

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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

@Service
@Transactional
@Slf4j
public class OfferServiceImpl implements OfferService {

  private static final Pattern nonDigitRegex = Pattern.compile("[^0-9]");

  @Autowired
  private OfferRepository offerRepo;

  @Autowired
  private ViewOfferRepository viewOfferRepo;

  @Autowired
  private OrganisationRepository orgRepo;

  @Autowired
  private NumberFormatterContext numberFormatterContext;

  @Override
  public Optional<OfferDto> getOfferDetails(final UserInfo user, final Long offerId) {
    return getOne(user.getOrganisationId(), offerId).map(OfferConverters::convert);
  }

  @Override
  public Page<OfferGeneralDto> searchOffers(OfferSearchCriteria criteria, Pageable pageable) {
    log.debug("Get offer by criteria = {}, pageable = {}", criteria, pageable);

    Assert.notNull(criteria, "The given search criteria must not be null");

    try {
      convertPriceFormat(criteria);
    } catch (ParseException e) {
      return Page.empty();
    }

    final Specification<ViewOffer> specOffer = VOfferSpecifications.of(criteria);


    final long start = System.currentTimeMillis();
    final Page<ViewOffer> offers = viewOfferRepo.findAll(specOffer, pageable);
    log.debug("Perf:OfferServiceImpl-> searchOffers-> findAll  {} ms",
        System.currentTimeMillis() - start);

    return offers.map(ViewOfferConverters::convertToGeneralDto);
  }

  private void convertPriceFormat(OfferSearchCriteria criteria) throws ParseException {
    if (StringUtils.isBlank(criteria.getPrice())) {
      return;
    }
    String formatedPrice = criteria.getPrice();
    NumberFormat numberFormat =
        numberFormatterContext.getFormatterByAffiliateShortName(criteria.getCollectionShortname());
    final DecimalFormat decimalFormatter = ((DecimalFormat) numberFormat);
    decimalFormatter.setParseBigDecimal(true);
    BigDecimal parsedPrice = (BigDecimal) decimalFormatter.parse(formatedPrice);
    formatedPrice = parsedPrice.toString();
    String formatedPricePlainDigit = formatedPrice.replaceAll(nonDigitRegex.pattern(), "");
    String pricePlainDigit = criteria.getPrice().replaceAll(nonDigitRegex.pattern(), "");
    if (formatedPricePlainDigit.length() == pricePlainDigit.length()) {
      criteria.setPrice(formatedPrice);
    }
  }

  @Override
  public void remove(final UserInfo user, final Long offerId) {
    log.debug("Remove offer by offer id {}", offerId);
    final Offer offer =
        getOne(user.getOrganisationId(), offerId).orElseThrow(() -> new ValidationException(
            String.format("Not found offer with id %d", offerId)));
    offer.setTecstate(OfferTecStateType.INACTIVE.name());
    offer.setModifiedUserId(user.getId());
    offer.setModifiedDate(Calendar.getInstance().getTime());

    offerRepo.save(offer);
  }

  @Override
  public Optional<Offer> getOne(final Integer orgId, final Long offerId) {
    return offerRepo.findByActiveOfferId(offerId, orgId);
  }

  @Override
  public OfferDto create(final UserInfo user) {
    log.debug("Create new offer for user {}", user.getUsername());

    final Offer savedOffer = createNewOffer(user);
    log.debug("Offer created successfully id = {}", savedOffer.getId());
    return OfferConverters.convert(savedOffer);
  }

  @Override
  public Offer createNewOffer(final UserInfo user) {
    final Date now = Calendar.getInstance().getTime();
    final Organisation organisation =
        orgRepo.findById(user.getOrganisationId()).orElseThrow(() -> new ValidationException(
            String.format("Not found customer with id %d", user.getOrganisationId())));
    final Offer offer = Offer.builder().organisation(organisation).ownerId(user.getId())
        .status(OfferStatus.OPEN.name()).offerDate(now).totalGrossPrice(NumberUtils.DOUBLE_ZERO)
        .createdUserId(user.getId()).createdDate(now).tecstate(OfferTecStateType.ACTIVE.name())
        .type(OfferType.OFFER.name()).vat(getVatValue(user.getSettings())).altOfferPriceUsed(false)
        .version(1).build();
    return offerRepo.save(offer);
  }

  private static Double getVatValue(final OwnSettings settings) {
    if (Objects.isNull(settings)) {
      return null;
    }
    return OfferCalculationHelper.defaultVatToPercentValue(settings.getVatRate());
  }

}
