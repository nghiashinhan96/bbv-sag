package com.sagag.services.tools.batch.migration.organation_property;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceOrganisationProperty;
import com.sagag.services.tools.domain.target.TargetOrganisationProperty;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.repository.source.SourceOrganisationPropertyRepository;
import com.sagag.services.tools.repository.target.TargetOrganisationPropertyRepository;
import com.sagag.services.tools.service.OrganisationService;
import com.sagag.services.tools.support.CommonInitialResource;
import com.sagag.services.tools.utils.DefaultUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@OracleProfile
@Slf4j
public class MigrateOrganisationPropertyFromOrclTasklet extends AbstractTasklet {

	@Autowired(required = false)
	private SourceOrganisationPropertyRepository sourceOrganisationPropertyRepo;

	@Autowired
	private TargetOrganisationPropertyRepository targetOrganisationPropertyRepo;

	@Autowired
	private OrganisationService organisationService;

	@Autowired
  protected CommonInitialResource commonInitialResource;

	@Override
	public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext)
			throws BatchJobException {
		final Set<Long> sourceOrgIdSet = Collections.unmodifiableSet(commonInitialResource.getSourceOrgSet());
		log.debug("The size of organisation id list = {} records", sourceOrgIdSet.size());

		List<TargetOrganisationProperty> targetList;
		for (final Long sourceOrgId : sourceOrgIdSet) {
			final Integer targetOrgId = commonInitialResource.getOrgId(sourceOrgId);
			if (Objects.isNull(targetOrgId)) {
				continue;
			}
			final List<SourceOrganisationProperty> sourceOrgProperties = sourceOrganisationPropertyRepo
					.findOfferPropByOrganisationId(sourceOrgId);

			if (CollectionUtils.isEmpty(sourceOrgProperties)) {
				continue;
			}
			targetList = sourceOrgProperties.stream().peek(item -> log.debug("Source organisation property = {}", item))
					.map(property -> mapSourceToTarget(property)).filter(Objects::nonNull)
					.peek(target -> log.debug("Target organisation property = {}", target))
					.collect(Collectors.toList());
			targetOrganisationPropertyRepo.saveAll(targetList);
		}
		return finish(contribution);
	}

	private TargetOrganisationProperty mapSourceToTarget(final SourceOrganisationProperty source) {
		final TargetOrganisationProperty target = new TargetOrganisationProperty();

		final Integer connectOrgId = commonInitialResource
				.getOrgId(source.getSourceOrganisationPropertyId().getOrganisationId());
		if (Objects.isNull(connectOrgId) || !organisationService.isBelongsToDatMatikAtMatikChAffiliate(connectOrgId)) {
			return null;
		}
		target.setOrganisationId(connectOrgId.longValue());
		target.setType(source.getSourceOrganisationPropertyId().getType());

		final String value = StringUtils.defaultString(source.getValue());
		target.setValue(DefaultUtils.toUtf8Value(value));
		return target;
	}

}
