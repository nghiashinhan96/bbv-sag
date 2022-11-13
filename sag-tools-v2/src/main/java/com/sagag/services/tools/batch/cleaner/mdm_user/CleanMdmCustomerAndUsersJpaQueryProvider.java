package com.sagag.services.tools.batch.cleaner.mdm_user;

import com.sagag.services.tools.domain.target.CustomExtOrganisation;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.database.orm.JpaQueryProvider;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Slf4j
public class CleanMdmCustomerAndUsersJpaQueryProvider implements JpaQueryProvider {
  
  private EntityManager entityManager;
  
  private List<String> customerNrs;
  
  public CleanMdmCustomerAndUsersJpaQueryProvider(final List<String> customerNrs) {
    super();
    this.customerNrs = ListUtils.emptyIfNull(customerNrs).stream().map(StringUtils::trim)
        .collect(Collectors.toList());
  }
  
  @Override
  public Query createQuery() {
    final StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("select exOrg.ID AS ID, ");
    sqlBuilder.append("exOrg.EXTERNAL_CUSTOMER_ID AS EXT_CUSTOMER_ID, ");
    sqlBuilder.append("exOrg.EXTERNAL_CUSTOMER_NAME AS EXT_CUST_NAME, ");
    sqlBuilder.append("org.ID AS ORG_ID, ");
    sqlBuilder.append("parent.NAME AS COMPANY_NAME ");
    sqlBuilder.append("from dbo.EXTERNAL_ORGANISATION exOrg ");
    sqlBuilder.append("inner join dbo.ORGANISATION org on org.ID = exOrg.ORG_ID ");
    sqlBuilder.append("inner join dbo.ORGANISATION parent on parent.ID = org.PARENT_ID ");
    sqlBuilder.append("where org.ORG_CODE IN (:custNrs)");
    log.info("custNrs = {}", customerNrs);
    return this.entityManager.createNativeQuery(sqlBuilder.toString(), CustomExtOrganisation.class)
        .setParameter("custNrs", customerNrs);
  }

  @Override
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

}
