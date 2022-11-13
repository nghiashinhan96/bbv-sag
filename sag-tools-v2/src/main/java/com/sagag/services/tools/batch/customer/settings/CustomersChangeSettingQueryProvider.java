package com.sagag.services.tools.batch.customer.settings;

import com.sagag.services.tools.domain.target.CustomerSettings;
import com.sagag.services.tools.support.SupportedAffiliate;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@RequiredArgsConstructor
@Slf4j
public class CustomersChangeSettingQueryProvider implements JpaQueryProvider {

  private EntityManager entityManager;

  @NonNull
  private String affiliate;

  @Override
  public Query createQuery() {
    log.info("Creating the query to fetch all customer by affiliate = {}", affiliate);
    Assert.hasText(affiliate, "The given affiliate must not be empty");
    SupportedAffiliate supportedAffiliate = SupportedAffiliate.fromDesc(affiliate)
      .orElseThrow(() -> new IllegalArgumentException("Not support this affiliate"));

    final StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select cs.* from dbo.CUSTOMER_SETTINGS cs where cs.ID IN ( ");
    queryBuilder.append("select org.ORDER_SETTINGS_ID from dbo.ORGANISATION org where org.PARENT_ID = ( ");
    queryBuilder.append("select pOrg.ID from dbo.ORGANISATION pOrg where pOrg.SHORTNAME = :affiliate)) ");
    return this.entityManager.createNativeQuery(queryBuilder.toString(), CustomerSettings.class)
      .setParameter("affiliate", supportedAffiliate.getAffiliate());
  }

  @Override
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
}
