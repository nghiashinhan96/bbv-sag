package com.sagag.services.tools.batch.mapping_ebl.organisation;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceOrganisation;
import com.sagag.services.tools.domain.target.MappingUserIdEblConnect;
import com.sagag.services.tools.domain.target.Organisation;
import com.sagag.services.tools.repository.source.SourceOrganisationRepository;
import com.sagag.services.tools.repository.target.MappingUserIdEblConnectRepository;
import com.sagag.services.tools.repository.target.OrganisationRepository;
import com.sagag.services.tools.utils.ToolConstants;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@OracleProfile
@Slf4j
public class MappingOrgIdTasklet extends AbstractTasklet {

	@Autowired
	private MappingUserIdEblConnectRepository mappingUserIdEblConnectRepo;

	@Autowired
	private OrganisationRepository connectOrgRepo;

	@Autowired(required = false)
	private SourceOrganisationRepository eblOrgRepo;

	@Override
	public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
		log.info("START mapping org id tasklet");

		final List<Organisation> connectOrg = connectOrgRepo.findAll();

		final List<List<Organisation>> orgPartitionList = ListUtils.partition(connectOrg, ToolConstants.MAX_CHUNK_SIZE);

		final Map<String, Integer> connectOrgsMap = connectOrg.stream()
				.collect(Collectors.toMap(item -> item.getOrgCode(), item -> item.getId()));

		for (final List<Organisation> orgList : orgPartitionList) {
			final List<MappingUserIdEblConnect> connects = new ArrayList<>();
			orgList.parallelStream().forEach(org -> {
				final String orgCode = org.getOrgCode();
				final Optional<SourceOrganisation> sourceOrg = eblOrgRepo.findOneByErpNumber(Long.parseLong(orgCode));
				final MappingUserIdEblConnect mappingObj = createMappingUserIdEblConnect(
						sourceOrg.isPresent() ? sourceOrg.get().getId() : null, connectOrgsMap.get(orgCode));
				log.debug(
						"Map org Id - EBL user id = {} to Connect user id = {} - EBL ORG ID = {} and CONNECT ORG ID = {}",
						mappingObj.getEblUserId(), mappingObj.getConnectUserId(), mappingObj.getEblOrgId(),
						mappingObj.getConnectOrgId());
				connects.add(mappingObj);
			});
			mappingUserIdEblConnectRepo.saveAll(connects);
		}

		log.info("END mapping org id tasklet");

		return finish(contribution);
	}

	private MappingUserIdEblConnect createMappingUserIdEblConnect(final Long eblOrgId, final Integer connectOrgId) {
		return MappingUserIdEblConnect
				.builder()
				.eblUserId(NumberUtils.LONG_ONE)
				.connectUserId(NumberUtils.LONG_ONE)
				.eblOrgId(eblOrgId)
				.connectOrgId(connectOrgId)
				.build();
	}
}
