package com.sagag.services.tools.batch.offer_feature.offer_person;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.csv.CsvAddress;
import com.sagag.services.tools.domain.csv.CsvRoleAssignment;
import com.sagag.services.tools.domain.target.OfferAddress;
import com.sagag.services.tools.domain.target.TargetOfferPerson;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.repository.target.OfferAddressRepository;
import com.sagag.services.tools.repository.target.TargetOfferPersonRepository;
import com.sagag.services.tools.support.CommonInitialDataLoader;
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
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
@OracleProfile
public class MigratePersonAddressFromCsvTasklet extends AbstractTasklet {

  private static final String TABLE = OfferTables.OFFER_ADDRESS.getTableName();

  private static final String NOT_EXISTING_ADDRESS_REPORT_FILE =
    SystemUtils.getUserDir() + "/csv/offer_address_not_migrated_data.csv";

  @Autowired
  private OfferAddressRepository offerAddressRepo;

  @Autowired
  private TargetOfferPersonRepository targetOfferPersonRepo;

  @Autowired
  protected CommonInitialDataLoader initialDataLoader;

  @Override
  public RepeatStatus execute(StepContribution contribution,
    ChunkContext chunkContext) throws BatchJobException {
    logActiveProfile();
    final List<TargetOfferPerson> people =
      Collections.unmodifiableList(targetOfferPersonRepo.findAll());
    log.debug("The size of person id list = {} records", people.size());

    // Update custom config to set exactly id
    SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    EntityManagerUtils.turnOnIdentityInsert(session, TABLE);

    final Map<Long, Long> orgAddrMap = initialDataLoader.loadOrganisationAddressMapping();
    final Map<Long, CsvAddress> addressMap = initialDataLoader.loadAddressMapping();
    final Map<Long, CsvRoleAssignment> roleAssignmentMap = initialDataLoader.loadRoleAssignment();

    final List<TargetOfferPerson> notExistingAddressPeople = new ArrayList<>();
    final List<OfferAddress> offerAddresses = people.stream()
      .peek(person -> log.debug("Source person = {}", person))
      .map(person -> {
        // Find orgId by role assignment
        Long personId = person.getId();
        final CsvRoleAssignment roleAssignment = roleAssignmentMap.get(personId);
        if (roleAssignment == null) {
          notExistingAddressPeople.add(person);
          return null;
        }
        Long finalCustomerOrgId = roleAssignment.getOrgId();

        // Find address by orgId of final customer
        Long addressId = orgAddrMap.get(finalCustomerOrgId);
        if (addressId == null) {
          notExistingAddressPeople.add(person);
          return null;
        }
        CsvAddress address = addressMap.get(addressId);
        if (address == null) {
          notExistingAddressPeople.add(person);
          return null;
        }
        return mapSourceToTarget(personId, address);
      })
      .filter(Objects::nonNull)
      .peek(target -> log.debug("Target address of person = {}", target))
      .collect(Collectors.toList());

    List<List<OfferAddress>> partitions =
      ListUtils.partition(offerAddresses, ToolConstants.MAX_SIZE);
    partitions.stream().forEach(offerAddressRepo::saveAll);

    if (!CollectionUtils.isEmpty(notExistingAddressPeople)) {
      CsvUtils.write(NOT_EXISTING_ADDRESS_REPORT_FILE, notExistingAddressPeople);
    }

    return finish(contribution);
  }

  private OfferAddress mapSourceToTarget(Long personId, CsvAddress source) {
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
