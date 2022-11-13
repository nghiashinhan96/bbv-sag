package com.sagag.services.tools.batch.mapping_ebl.user;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.csv.CsvMigrationEshopUser;
import com.sagag.services.tools.domain.target.MappingUserIdEblConnect;
import com.sagag.services.tools.repository.target.MappingUserIdEblConnectRepository;
import com.sagag.services.tools.repository.target.MigrationOrganisationRepository;
import com.sagag.services.tools.repository.target.OrganisationRepository;
import com.sagag.services.tools.utils.CsvUtils;
import com.sagag.services.tools.utils.ToolConstants;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@OracleProfile
public class MappingUserIdTasklet extends AbstractTasklet {

  @Value("${filePath:/csv/users/users.csv}")
  private String filePath;

  @Autowired
  private MappingUserIdEblConnectRepository mappingUserIdEblConnectRepo;

  @Autowired
  private MigrationOrganisationRepository migrationOrganisationRepo;

  @Autowired
  private OrganisationRepository organisationRepo;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    log.info("START mapping user id tasklet");

    log.debug("File path = {}", filePath);

    final File file = new File(SystemUtils.getUserDir() + filePath);
    List<CsvMigrationEshopUser> migrationEshopUsers =
        CsvUtils.read(file, CsvMigrationEshopUser.class);
    if (CollectionUtils.isEmpty(migrationEshopUsers)) {
      throw new IllegalArgumentException("The given list of csv file have no content");
    }

    List<List<CsvMigrationEshopUser>> migrationEshopUserPartitionList =
      ListUtils.partition(migrationEshopUsers, ToolConstants.MAX_SIZE);

    Map<String, Long> migrationOrganisationsMap = migrationOrganisationRepo.findAll().stream()
      .collect(Collectors.toMap(item -> item.getOrgCode(), item -> Long.valueOf(item.getOrgId())));
    Map<String, Integer> connectOrgsMap = organisationRepo.findAll().stream()
      .collect(Collectors.toMap(item -> item.getOrgCode(), item -> item.getId()));

    Map<Long, Integer> combinedOrgMap = new HashMap<>();

    migrationOrganisationsMap.forEach((key, value) -> {
      combinedOrgMap.putIfAbsent(value, connectOrgsMap.get(key));
    });

    log.info("migrationOrganisationsMap = {}", migrationOrganisationsMap);
    log.info("connectOrgsMap = {}", connectOrgsMap);
    log.info("combinedOrgMap = {}", combinedOrgMap);

    List<MappingUserIdEblConnect> totalList = new ArrayList<>();
    for (List<CsvMigrationEshopUser> migrationEshopUserList : migrationEshopUserPartitionList) {
      int no = 1;
      List<MappingUserIdEblConnect> connects = new ArrayList<>();
      for (CsvMigrationEshopUser migrationEshopUser : migrationEshopUserList) {
        final Long eblUserId = migrationEshopUser.getUserId();
        final Long connectUserId = migrationEshopUser.getNewUserId();
        Long eblOrgId = migrationEshopUser.getOrgId();
        Integer connectOrgId = combinedOrgMap.get(eblOrgId);
        log.debug("Map user id of No = {} - EBL user id = {} to Connect user id = {} - EBL ORG ID = {} and CONNECT ORG ID = {}",
          no++, eblUserId, connectUserId, eblOrgId, connectOrgId);

        connects.add(new MappingUserIdEblConnect(eblUserId, connectUserId, eblOrgId, connectOrgId));
      }
      totalList.addAll(connects);

      mappingUserIdEblConnectRepo.saveAll(connects);
    }

    CsvUtils.write(SystemUtils.getUserDir() + "/csv/export/mapping_user.csv", totalList);

    log.info("END mapping user id tasklet");

    return finish(contribution);
  }

}
