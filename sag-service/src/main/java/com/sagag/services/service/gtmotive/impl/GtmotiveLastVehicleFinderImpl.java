package com.sagag.services.service.gtmotive.impl;

import com.sagag.eshop.repo.api.VinLoggingRepository;
import com.sagag.eshop.repo.entity.VinLogging;
import com.sagag.services.gtmotive.api.GtmotiveService;
import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.gtmotive.domain.request.EstimateIdOperationMode;
import com.sagag.services.gtmotive.domain.request.GtmotiveRequestMode;
import com.sagag.services.gtmotive.domain.response.GtmotiveVehicleDto;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVehicleInfoCriteria;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVehicleSearchByVinRequest;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsThreeSearchRequest;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsThreeResponse;
import com.sagag.services.gtmotive.exception.GtmotiveXmlResponseProcessingException;
import com.sagag.services.gtmotive.utils.GtmotiveUtils;
import com.sagag.services.service.gtmotive.GtmotiveLastVehicleFinder;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.xpath.XPathExpressionException;

@Component
@GtmotiveProfile
public class GtmotiveLastVehicleFinderImpl implements GtmotiveLastVehicleFinder {

  private static final int OUTDATED_ESTIMATE_ID_ERROR = 12;

  private static final String PART_CODE_TEST = "4851R";

  @Autowired
  private GtmotiveService gtmotiveService;

  @Autowired
  private VinLoggingRepository vinLoggingRepo;

  @Override
  @Transactional
  public GtmotiveVehicleDto find(GtmotiveVehicleDto gtResponse,
      GtmotiveVehicleSearchByVinRequest request, String custNr) throws XPathExpressionException,
      GtmotiveXmlResponseProcessingException {

    GtmotivePartsThreeSearchRequest testItemSearchRequest = new GtmotivePartsThreeSearchRequest();
    testItemSearchRequest.setUmc(gtResponse.getUmc());
    testItemSearchRequest.setPartCode(PART_CODE_TEST);
    List<String> equipments = CollectionUtils.emptyIfNull(gtResponse.getEquipmentItems()).stream()
      .filter(GtmotiveUtils.startsWithIgnoreCaseSpecialEquipmentPrefixPredicate().negate())
      .collect(Collectors.toList());
    testItemSearchRequest.setEquipments(equipments);

    GtmotivePartsThreeResponse response =
      gtmotiveService.searchReferencesByPartCode(testItemSearchRequest);
    if (Objects.nonNull(response) && Objects.nonNull(response.getErrorCode())
      && response.getErrorCode() == OUTDATED_ESTIMATE_ID_ERROR) {
      String vin = gtResponse.getVin();
      String oldEstimateId = gtResponse.getEstimateId();
      String newEstimateId = GtmotiveUtils.generateEstimateId(custNr);
      GtmotiveVehicleInfoCriteria registerNewEstimateIdCriteria = new GtmotiveVehicleInfoCriteria();
      registerNewEstimateIdCriteria.setEstimateId(newEstimateId);
      registerNewEstimateIdCriteria.setOperation(EstimateIdOperationMode.CREATE);
      registerNewEstimateIdCriteria.setRequestMode(GtmotiveRequestMode.VIN);
      registerNewEstimateIdCriteria.setVin(vin);
      gtmotiveService.registerNewEstimateId(registerNewEstimateIdCriteria);

      request.setEstimateId(newEstimateId);
      Optional<GtmotiveVehicleDto> retryResponse = gtmotiveService.searchVehicleByVin(request);
      if (!retryResponse.isPresent()) {
        return null;
      }

      final Long customerNr = Long.valueOf(custNr);
      Optional<VinLogging> vinlogging = vinLoggingRepo.findVinLogByEstimateUsed(vin, customerNr);
      vinlogging.ifPresent(item -> {
        item.setEstimateID(newEstimateId);
        item.setDateOfLogEntry(Calendar.getInstance().getTime());
        vinLoggingRepo.save(item);

        List<VinLogging> oldVinLoggings =
          vinLoggingRepo.findAllByEstimateId(vin, customerNr, oldEstimateId);
        if (CollectionUtils.isNotEmpty(oldVinLoggings)) {
          vinLoggingRepo.deleteAll(oldVinLoggings);
        }
      });

      return retryResponse.get();
    }
    return gtResponse;
  }
}
