package com.sagag.services.tools.batch.offer_feature.offer_person;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceAddress;
import com.sagag.services.tools.domain.target.OfferAddress;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.repository.source.SourceAddressRepository;
import com.sagag.services.tools.repository.target.OfferAddressRepository;
import com.sagag.services.tools.repository.target.TargetOfferPersonRepository;
import com.sagag.services.tools.support.OfferTables;
import com.sagag.services.tools.utils.CsvUtils;
import com.sagag.services.tools.utils.DefaultUtils;
import com.sagag.services.tools.utils.EntityManagerUtils;
import com.sagag.services.tools.utils.ToolConstants;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.SystemUtils;
import org.hibernate.internal.SessionImpl;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@OracleProfile
@Slf4j
public class MigratePersonAddressFromOrclTasklet extends AbstractTasklet {

  private static final String TABLE = OfferTables.OFFER_ADDRESS.getTableName();

  private static final String NOT_EXISTING_ADDRESS_REPORT_FILE =
    SystemUtils.getUserDir() + "/csv/offer_address_not_migrated_data.csv";

  @Autowired(required = false)
  private SourceAddressRepository sourceAddressRepository;

  @Autowired
  private OfferAddressRepository offerAddressRepo;

  @Autowired
  private TargetOfferPersonRepository targetOfferPersonRepo;

  @Override
  public RepeatStatus execute(StepContribution contribution,
    ChunkContext chunkContext) throws BatchJobException {
    logActiveProfile();
    final List<Long> personIdList =
      Collections.unmodifiableList(targetOfferPersonRepo.findPersonIdList());
    log.debug("The size of person id list = {} records", personIdList.size());

    // Update custom config to set exactly id
    SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    EntityManagerUtils.turnOnIdentityInsert(session, TABLE);

    final List<Long> notExistingAddressPeople = new ArrayList<>();
    final List<OfferAddress> offerAddresses = new ArrayList<>();
    personIdList.stream()
      .distinct()
      .peek(personId -> log.debug("Source person Id = {}", personId))
      .forEach(personId -> {
        List<SourceAddress> sourceAddressList =
          sourceAddressRepository.findAddressByByPersonId(personId);
        log.debug("Source address list = {}", sourceAddressList);
        if (CollectionUtils.isEmpty(sourceAddressList)) {
          notExistingAddressPeople.add(personId);
        }

        offerAddresses.addAll(sourceAddressList.stream()
          .map(source -> mapSourceToTarget(personId, source)).collect(Collectors.toSet()));
      });

    offerAddresses.stream().filter(Objects::nonNull)
      .peek(target -> log.debug("Target address of person = {}", target))
      .collect(Collectors.toList());

    List<List<OfferAddress>> partitions =
      ListUtils.partition(offerAddresses, ToolConstants.MAX_SIZE);
    partitions.stream().forEach(offerAddressRepo::saveAll);

    CsvUtils.write(NOT_EXISTING_ADDRESS_REPORT_FILE, notExistingAddressPeople);

    return finish(contribution);
  }

  private OfferAddress mapSourceToTarget(Long personId, SourceAddress source) {
    OfferAddress target = new OfferAddress();
    target.setId(source.getId());
    target.setPersonId(personId);
    target.setLine1(DefaultUtils.toUtf8Value(source.getLine1()));
    target.setLine2(DefaultUtils.toUtf8Value(source.getLine2()));
    target.setLine3(DefaultUtils.toUtf8Value(source.getLine3()));
    target.setCountryIso(source.getCountryIso());
    target.setState(DefaultUtils.toUtf8Value(source.getState()));
    target.setCity(DefaultUtils.toUtf8Value(source.getCity()));
    target.setZipCode(source.getZipCode());
    target.setErpId(source.getErpId() != null ? String.valueOf(source.getErpId()) : null);
    target.setType(source.getType());
    target.setPoBox(DefaultUtils.toUtf8Value(source.getPoBox()));
    target.setCreatedUserId(source.getUserCreatedId());
    target.setModifiedUserId(DefaultUtils.defaultModifiedUserId(source.getUserModifiedId()));
    target.setCreatedDate(source.getDateCreated());
    target.setModifiedDate(source.getDateModified());
    target.setTecstate(source.getTecstate());
    target.setVersion(source.getVersion());
    return target;
  }


}
