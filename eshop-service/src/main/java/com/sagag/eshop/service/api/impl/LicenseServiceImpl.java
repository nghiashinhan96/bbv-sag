package com.sagag.eshop.service.api.impl;

import com.google.common.base.MoreObjects;
import com.sagag.eshop.repo.api.LicenseRepository;
import com.sagag.eshop.repo.api.LicenseSettingsRepository;
import com.sagag.eshop.repo.api.collection.OrganisationCollectionRepository;
import com.sagag.eshop.repo.entity.License;
import com.sagag.eshop.repo.entity.LicenseSettings;
import com.sagag.eshop.repo.specification.AdminLicenseSpecifications;
import com.sagag.eshop.service.api.LicenseService;
import com.sagag.eshop.service.converter.LicenseConverters;
import com.sagag.eshop.service.dto.HaynesProLicenseSettingDto;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.LicenseExportException;
import com.sagag.eshop.service.exception.LicenseNotFoundException;
import com.sagag.eshop.service.exporter.LicenseExporter;
import com.sagag.services.common.enums.HaynesProLicenseTypeEnum;
import com.sagag.services.common.enums.LicenseType;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.backoffice.dto.BackOfficeLicenseDto;
import com.sagag.services.domain.eshop.criteria.LicenseSearchCriteria;
import com.sagag.services.domain.eshop.dto.CustomerLicenseDto;
import com.sagag.services.domain.eshop.dto.LicenseDto;
import com.sagag.services.domain.eshop.dto.LicenseSettingsDto;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

/**
 * Class service implementation for License.
 */
@Service
@Slf4j
public class LicenseServiceImpl implements LicenseService {

  private static final String SORTED_BEGIN_DATE_FIELD = "beginDate";

  @Autowired
  private LicenseRepository licenseRepo;

  @Autowired
  private LicenseSettingsRepository licenseSettingsRepo;

  @Autowired
  private OrganisationCollectionRepository orgCollectionRepo;


  @Override
  public Optional<LicenseSettingsDto> getLicenseSettingsByPackId(final Long packId) {
    if (packId == null) {
      return Optional.empty();
    }
    return licenseSettingsRepo.findOneByPackId(packId)
        .map(LicenseConverters.licenseSettingsConverter());
  }

  @Override
  public Optional<LicenseSettingsDto> getLicenseSettingsByArticleId(final Long packArticleId) {
    if (packArticleId == null) {
      return Optional.empty();
    }
    return licenseSettingsRepo.findOneByPackArticleId(packArticleId)
        .map(LicenseConverters.licenseSettingsConverter());
  }

  @Override
  public int getVinCallsLeft(final Long customerNr) {
    if (Objects.isNull(customerNr)) {
      return NumberUtils.INTEGER_ZERO;
    }
    final Integer vinCallsNr =
        licenseRepo.findAvailableLicenseCalls(customerNr, LicenseType.VIN.name());
    return MoreObjects.firstNonNull(vinCallsNr, NumberUtils.INTEGER_ZERO);
  }

  @Override
  @Transactional
  public void createVinLicense(LicenseDto licenseDto) {
    Assert.notNull(licenseDto, "The given update object must not be null");
    License license = LicenseConverters.licenseEntityConverter().apply(licenseDto);
    licenseRepo.save(license);
  }

  @Override
  public List<LicenseSettingsDto> getAllLicensePackage() {
    return licenseSettingsRepo.findAll().stream()
        .map(LicenseConverters.licenseSettingsConverter())
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void increaseQuantityUsed(final Long userId, final String custNr,
      final LicenseType licenseType) {
    Assert.notNull(userId, "The given user id must not be null");
    Assert.notNull(custNr, "The given customer number must not be null");
    Assert.notNull(licenseType, "The given license type must not be null");

    final Sort sortedRequest = new Sort(Sort.Direction.ASC, SORTED_BEGIN_DATE_FIELD);
    final Pageable pageable = PageRequest.of(0, 1, sortedRequest);
    final Page<License> licenses =
        licenseRepo.findFirstAvailableLicense(Long.valueOf(custNr), licenseType.name(), pageable);
    if (CollectionUtils.isEmpty(licenses.getContent())) {
      throw new ValidationException("Cannot get available license of this user");
    }
    final License license = licenses.getContent().get(0);
    license.setQuantityUsed(license.getQuantityUsed() + 1);
    final Date now = DateUtils.getUTCDate(Calendar.getInstance().getTime());
    license.setLastUsed(now);
    license.setLastUpdate(now);
    license.setLastUpdateBy(String.valueOf(userId));
    licenseRepo.save(license);
  }

  @Override
  @Transactional
  public void redeemVinLicense(final String custNr, int searchCount) {
    final List<License> licenses =
        licenseRepo.findAllByCustomerNrAndTypeOfLicense(Long.valueOf(custNr),
            LicenseType.VIN.name());
    if (CollectionUtils.isEmpty(licenses)) {
      return;
    }

    Collections.sort(licenses, sortByBeginDate());
    final List<License> savedLicensees = new ArrayList<>();
    int redeemCount = searchCount;
    for (License license : licenses) {
      if (redeemCount <= 0) {
        break;
      }
      if (redeemCount > license.getQuantityUsed()) {
        redeemCount = redeemCount - license.getQuantityUsed();
        license.setQuantityUsed(0);
      } else {
        license.setQuantityUsed(license.getQuantityUsed() - redeemCount);
        redeemCount = 0;
      }
      savedLicensees.add(license);
    }
    if (!CollectionUtils.isEmpty(savedLicensees)) {
      licenseRepo.saveAll(savedLicensees);
    }
  }

  private static Comparator<License> sortByBeginDate() {
    return (left, right) -> (int) (left.getBeginDate().getTime() - right.getBeginDate().getTime());
  }

  @Override
  public Optional<HaynesProLicenseSettingDto> getHaynesProLicense(final String custNr) {
    if (custNr == null) {
      return Optional.empty();
    }
    final Optional<License> licensePackage = licenseRepo.findAvailableHaynesProLicense(
        Long.valueOf(custNr), PageUtils.FIRST_ITEM_PAGE).stream().findFirst();
    if (!licensePackage.isPresent()) {
      return Optional.empty();
    }
    return licenseSettingsRepo.findOneByPackId(licensePackage.get().getPackId())
        .map(item -> {
          final HaynesProLicenseSettingDto settings = new HaynesProLicenseSettingDto();
          settings.setLicenseType(HaynesProLicenseTypeEnum.fromPackageName(item.getPackName())
            .map(HaynesProLicenseTypeEnum::name).orElse(StringUtils.EMPTY));
          settings.setOptionalParameters(StringUtils.defaultString(item.getOptionalParameters()));
          return settings;
        });
  }

  @Override
  public List<LicenseSettingsDto> getAllVinLicenses() {
    return licenseSettingsRepo.searchLicenses(LicenseType.VIN.name());
  }

  @Override
  public Page<BackOfficeLicenseDto> getAllLicensesOfCustomer(String customerNr, Pageable pageable) {
    if (customerNr == null || pageable == null) {
      return Page.empty();
    }
    return licenseRepo.findAllByCustomerNr(Long.valueOf(customerNr), pageable)
        .map(LicenseConverters.licenseBackOfficeConverter());
  }

  @Override
  @Transactional
  public void deleteLicenseForCustomer(Long id) {
    Assert.notNull(id, "Id must be not null");
    licenseRepo.deleteById(id);
  }

  @Override
  @Transactional
  public void updateLicenseForCustomer(Long id, CustomerLicenseDto licenseModel,
      final UserInfo user) throws LicenseNotFoundException {
    Assert.notNull(id, "Id must be not null");
    Assert.notNull(licenseModel, "Request data is invalid");
    Assert.hasText(licenseModel.getBeginDate(), "Begin date must not be empty");
    Assert.hasText(licenseModel.getEndDate(), "End date must not be empty");
    final String packName = licenseModel.getPackName();
    Assert.hasText(packName, "Pack name must not be empty");

    Date beginDate = DateUtils.toDate(licenseModel.getBeginDate(), DateUtils.SWISS_DATE_PATTERN_3);
    Date endDate = DateUtils.toDate(licenseModel.getEndDate(), DateUtils.SWISS_DATE_PATTERN_3);
    Assert.isTrue(beginDate.before(endDate), "Invalid validity");

    LicenseSettings licenseSettings = licenseSettingsRepo.findOneByPackName(packName)
        .orElseThrow(() -> new LicenseNotFoundException(packName));

    License license = licenseRepo.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Not illegal license id for update: " + id));
    license.setTypeOfLicense(licenseSettings.getTypeOfLicense());
    license.setPackId(licenseSettings.getPackId());
    license.setPackName(licenseSettings.getPackName());
    license.setBeginDate(beginDate);
    license.setEndDate(endDate);
    license.setQuantity(licenseModel.getQuantity());
    license.setLastUpdate(Calendar.getInstance().getTime());
    license.setLastUpdateBy(user.getId().toString());
    licenseRepo.save(license);
  }

  @Override
  @Transactional
  public void assignLicenseToCustomer(final CustomerLicenseDto customerLicenseDto,
      final UserInfo user) throws LicenseNotFoundException {
    Assert.notNull(customerLicenseDto, "Request data is invalid");
    Assert.hasText(customerLicenseDto.getBeginDate(), "Begin date must not be empty");
    Assert.hasText(customerLicenseDto.getEndDate(), "End date must not be empty");
    final String packName = customerLicenseDto.getPackName();
    Assert.hasText(packName, "Pack name must not be empty");
    Assert.hasText(customerLicenseDto.getTypeOfLicense(), "Type of license must be not empty");
    Date beginDate =
        DateUtils.toDate(customerLicenseDto.getBeginDate(), DateUtils.SWISS_DATE_PATTERN_3);
    Date endDate =
        DateUtils.toDate(customerLicenseDto.getEndDate(), DateUtils.SWISS_DATE_PATTERN_3);
    Assert.notNull(beginDate, "Begin date must not be null");
    Assert.notNull(endDate, "End date must not be null");
    Assert.isTrue(beginDate.before(endDate), "Invalid validity");

    License license = new License();
    LicenseSettings licenseSettings = licenseSettingsRepo.findOneByPackName(packName)
        .orElseThrow(() -> new LicenseNotFoundException(packName));
    SagBeanUtils.copyProperties(customerLicenseDto, license);

    license.setQuantity(customerLicenseDto.getQuantity());
    license.setBeginDate(beginDate);
    license.setEndDate(endDate);
    license.setLastUpdate(Calendar.getInstance().getTime());
    license.setPackId(licenseSettings.getPackId());
    // #874: Set UserId by Id of User login
    license.setUserId(user.getId());
    license.setLastUpdateBy(user.getId().toString());
    licenseRepo.save(license);
  }

  @Override
  public Page<BackOfficeLicenseDto> search(LicenseSearchCriteria criteria) {
    log.debug("Search branches by criteria = {}", criteria);
    if (StringUtils.isEmpty(criteria.getAffiliate())) {
      return this.searchWithoutAffiliate(criteria);
    } else {
      return this.searchWithAffiliate(criteria);
    }
  }

  private Page<BackOfficeLicenseDto> searchWithoutAffiliate(LicenseSearchCriteria criteria) {
    Pageable pageable = PageUtils.defaultPageable(criteria.getPage(), criteria.getSize());
    final Specification<License> specForLicense = AdminLicenseSpecifications.searchByCriteria(criteria);
    return licenseRepo.findAll(specForLicense, pageable).map(LicenseConverters.licenseBackOfficeConverter());
  }

  private Page<BackOfficeLicenseDto> searchWithAffiliate(LicenseSearchCriteria criteria) {
    List<String> customers = orgCollectionRepo.findCustomersByCollectionShortName(criteria.getAffiliate());

    final Specification<License> specForLicense = AdminLicenseSpecifications.searchByCriteria(criteria);
    List<License> licenses = licenseRepo.findAll(specForLicense).stream()
        .filter(license -> customers.contains(String.valueOf(license.getCustomerNr())))
        .collect(Collectors.toList());

    final int start = criteria.getPage() * criteria.getSize();
    final int end = Math.min((start + criteria.getSize()), licenses.size());
    Page<License> licensePage = new PageImpl<>(
        licenses.subList(start, end),
        PageUtils.defaultPageable(criteria.getPage(), criteria.getSize()),
        licenses.size());

    return licensePage.map(LicenseConverters.licenseBackOfficeConverter());
  }

  @Override
  public ExportStreamedResult exportToCsvByCriteria(LicenseSearchCriteria criteria) throws ServiceException {
    List<BackOfficeLicenseDto> content = getExportContent(criteria);
    return LicenseExporter.builder().build().exportCsv(content);
  }

  @Override
  public ExportStreamedResult exportToExcelByCriteria(LicenseSearchCriteria criteria) throws LicenseExportException {
    List<BackOfficeLicenseDto> content = getExportContent(criteria);
    return LicenseExporter.builder().build().exportExcel(content);
  }

  private List<BackOfficeLicenseDto> getExportContent(LicenseSearchCriteria criteria) {
    final Predicate<License> filterByCondition ;
    final Specification<License> specForLicense = AdminLicenseSpecifications.searchByCriteria(criteria);
    if (!StringUtils.isBlank(criteria.getAffiliate())) {
      List<String> customers = orgCollectionRepo.findCustomersByCollectionShortName(criteria.getAffiliate());
      filterByCondition = license -> customers.contains(String.valueOf(license.getCustomerNr()));
    } else {
      filterByCondition = license -> true;
    }

    return licenseRepo.findAll(specForLicense).stream()
        .filter(filterByCondition )
        .map(LicenseConverters.licenseBackOfficeConverter())
        .collect(Collectors.toList());
  }
}
