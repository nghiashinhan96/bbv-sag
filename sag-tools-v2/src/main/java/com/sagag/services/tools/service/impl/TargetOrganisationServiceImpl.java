package com.sagag.services.tools.service.impl;

import com.sagag.services.tools.domain.target.Organisation;
import com.sagag.services.tools.service.TargetOrganisationService;
import com.sagag.services.tools.support.ConnectQueryFactory;
import com.sagag.services.tools.utils.ToolConstants;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service
@Slf4j
public class TargetOrganisationServiceImpl implements TargetOrganisationService {

  private static final String MANAGE_OFFERS_URL = "/offer**";

  @Autowired
  @Qualifier("targetEntityManager")
  private EntityManager targetEntityManager;

  @Autowired
  private ConnectQueryFactory queryFactory;

  @SuppressWarnings("unchecked")
  @Override
  public List<Integer> findOrganisationIdHasOfferPermission(final List<String> customerNumberList) {
    Assert.notEmpty(customerNumberList, "");
    if (CollectionUtils.size(customerNumberList) > ToolConstants.MAX_SIZE) {
      throw new IllegalArgumentException("");
    }
    final StringBuilder queryBuilder =
      queryFactory.createOrganisationHasOfferPermissionQueryBuilder();

    final Query query =
      targetEntityManager.createNativeQuery(queryBuilder.toString(), Organisation.class);
    query.setParameter("orgCodeList", customerNumberList);
    query.setParameter("relativeUrl", MANAGE_OFFERS_URL);
    query.setParameter("allowed", Boolean.TRUE);

    final List<Organisation> orgs = query.getResultList();
    final List<Integer> ids = orgs.stream().map(Organisation::getId).distinct()
      .collect(Collectors.toList());
    log.debug("The org id allow offer feature = {}", ids);
    return ids;
  }
}
