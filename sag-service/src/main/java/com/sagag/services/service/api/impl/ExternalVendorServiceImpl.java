package com.sagag.services.service.api.impl;

import com.google.common.primitives.Longs;
import com.sagag.eshop.repo.api.DeliveryProfileRepository;
import com.sagag.eshop.repo.api.ExternalVendorRepository;
import com.sagag.eshop.repo.api.VExternalVendorRepository;
import com.sagag.eshop.repo.entity.ExternalVendor;
import com.sagag.eshop.repo.entity.VExternalVendor;
import com.sagag.eshop.repo.specification.ExternalVendorSpecifications;
import com.sagag.eshop.service.api.CountryService;
import com.sagag.eshop.service.converter.ExternalConverters;
import com.sagag.eshop.service.dto.CsvExternalVendorDto;
import com.sagag.eshop.service.exception.ExternalVendorValidationException;
import com.sagag.eshop.service.exception.ExternalVendorValidationException.ExternalVendorErrorCase;
import com.sagag.services.common.enums.AvailabilityTypeEnum;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.criteria.ExternalVendorSearchCriteria;
import com.sagag.services.domain.eshop.dto.externalvendor.CountryDto;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;
import com.sagag.services.domain.eshop.dto.externalvendor.SupportBrandDto;
import com.sagag.services.domain.eshop.dto.externalvendor.SupportExternalVendorDto;
import com.sagag.services.hazelcast.api.SupplierCacheService;
import com.sagag.services.hazelcast.api.impl.CacheDataProcessor;
import com.sagag.services.service.api.ExternalVendorService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExternalVendorServiceImpl implements ExternalVendorService {

  @Autowired
  private ExternalVendorRepository externaVendorRepo;
  @Autowired
  private VExternalVendorRepository vExternaVendorRepo;

  @Autowired
  private DeliveryProfileRepository deliveryProfileRepo;

  @Autowired
  @Qualifier("externalVendorCacheServiceImpl")
  private CacheDataProcessor processor;

  @Autowired
  private SupplierCacheService supplierCacheService;

  @Autowired
  private CountryService countryService;

  @Transactional
  @Override
  public void importExternalVendor(List<CsvExternalVendorDto> csvExternalVendors)
      throws ExternalVendorValidationException {
    if (CollectionUtils.isEmpty(csvExternalVendors)) {
      throw new ExternalVendorValidationException(ExternalVendorErrorCase.ODE_EMP_001,
          "The imported external vendor data is empty");
    }
    List<ExternalVendor> externalVendors = csvExternalVendors.stream()
        .map(ExternalConverters.externalConverter()).collect(Collectors.toList());

    List<SupportBrandDto> brands = supplierCacheService.findAllBrand().stream()
        .map(brand -> SagBeanUtils.map(brand, SupportBrandDto.class)).collect(Collectors.toList());
    boolean nonExistedBrand = externalVendors.stream().map(ExternalVendor::getBrandId)
        .filter(Objects::nonNull).anyMatch(ex -> !brands.stream().map(SupportBrandDto::getDlnrid)
            .collect(Collectors.toList()).contains(String.valueOf(ex)));
    if (nonExistedBrand) {
      throw new ExternalVendorValidationException(ExternalVendorErrorCase.ODE_EMP_005,
          "Csv file import is used to brand is not existed");
    }
    externaVendorRepo.deleteAll();
    externaVendorRepo.saveAll(externalVendors);
    processor.refreshCacheAll();
  }

  @Override
  public Page<VExternalVendor> searchExternalVendor(ExternalVendorSearchCriteria searchCriteria) {
    final List<CountryDto> countries = countryService.getSupportedCountries();
    Specification<VExternalVendor> spec =
        ExternalVendorSpecifications.searchExternalVendor(searchCriteria);
    final Pageable pageable = searchCriteria.buildPageable();
    return vExternaVendorRepo.findAll(spec, pageable)
        .map(ev -> externalVendorOperator(countries).apply(ev));
  }

  private static UnaryOperator<VExternalVendor> externalVendorOperator(
      final List<CountryDto> countries) {
    return ev -> {
      final VExternalVendor clonedEv = SagBeanUtils.map(ev, VExternalVendor.class);
      if (CollectionUtils.isEmpty(countries)) {
        clonedEv.setCountry(StringUtils.EMPTY);
        return clonedEv;
      }
      clonedEv.setCountry(countries.stream()
          .filter(c -> c.getCode().equalsIgnoreCase(ev.getCountry()))
          .findFirst()
          .map(CountryDto::getDescription)
          .orElse(StringUtils.EMPTY));
      return clonedEv;
    };
  }

  @Override
  public SupportExternalVendorDto getMasterDataExternalVendor() {
    List<String> availabilityType =
        Stream.of(AvailabilityTypeEnum.values()).map(Enum::name).collect(Collectors.toList());
    List<SupportBrandDto> brands = supplierCacheService.findAllBrand().stream()
        .map(brand -> SagBeanUtils.map(brand, SupportBrandDto.class)).collect(Collectors.toList());
    List<DeliveryProfileDto> deliveryProfileNames = deliveryProfileRepo.findDeliveryProfileName();
    List<CountryDto> countries = countryService.getSupportedCountries();
    return SupportExternalVendorDto.builder().countries(countries)
        .availabilityType(availabilityType).brands(brands).deliveryProfile(deliveryProfileNames)
        .build();
  }

  @Transactional
  @Override
  public void createExternalVendor(ExternalVendorDto request, Long userId)
      throws ExternalVendorValidationException {
    validateRequestDataExternalVendor(request);
    ExternalVendor externalVendors = ExternalConverters.dtoConvertToExternalVendor(request);
    externalVendors.setCreatedDate(Calendar.getInstance().getTime());
    externalVendors.setCreatedUserId(userId);
    externaVendorRepo.save(externalVendors);
    processor.refreshCacheAll();
  }

  private void validateRequestDataExternalVendor(ExternalVendorDto request)
      throws ExternalVendorValidationException {
    Assert.notNull(request, "Request body must not be null");

    Assert.notNull(request.getCountry(),
        "The country must be not null for external vendor request");
    Assert.notNull(request.getVendorPriority(),
        "Vendor priority must be not null for external vendor request");
    Assert.notNull(request.getDeliveryProfileId(),
        "Delivery profile must be not null for external vendor request");
    Assert.notNull(request.getAvailabilityTypeId(),
        "The availability type id must be not null for external vendor request");

    final String vendorId = request.getVendorId();
    if (StringUtils.isNotBlank(vendorId)
        && Longs.tryParse(vendorId) == null) {
      throw new ExternalVendorValidationException(ExternalVendorErrorCase.EVE_IVI_001,
          "Invalid vendor id");
    }
  }

  @Override
  @Transactional
  public void updateExternalVendor(ExternalVendorDto request, Long userId)
      throws ExternalVendorValidationException {
    Assert.notNull(request.getId(), "Request body must existed id when editable");
    validateRequestDataExternalVendor(request);
    ExternalVendor externalVendors = ExternalConverters.dtoConvertToExternalVendor(request);
    externalVendors.setModifiedUserId(userId);
    externalVendors.setModifiedDate(Calendar.getInstance().getTime());
    externaVendorRepo.save(externalVendors);
    processor.refreshCacheAll();
  }


  @Override
  public void delete(Integer id) {
    Assert.notNull(id, "The id must be not null");
    externaVendorRepo.deleteById(id);
  }
}
