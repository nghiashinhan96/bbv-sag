package com.sagag.services.tools.batch.migration.customer_settings;

import com.sagag.services.tools.domain.source.SourceOrganisationProperty;
import com.sagag.services.tools.utils.QueryUtils;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.item.database.orm.JpaQueryProvider;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CustomerSettingsItemMigrationQueryProvider implements JpaQueryProvider {

  private EntityManager entityManager;

  @NonNull
  private List<String> types;

  @NonNull
  private List<String> custNrs;

  @Override
  public Query createQuery() {

    final List<String> distinctTrimedTypes = QueryUtils.getDistinctTrimedValues(types);
    final List<String> distinctTrimedCustNrs = QueryUtils.getDistinctTrimedValues(custNrs);

    if (CollectionUtils.isEmpty(distinctTrimedCustNrs)) {
      final String searchByType = "SELECT pro.ORGANISATION_ID AS ORGANISATION_ID, pro.TYPE AS TYPE, pro.VALUE AS VALUE "
          + "FROM SHOP.ORGANISATIONPROPERTY pro WHERE pro.TYPE IN (:types)";
      return this.entityManager.createNativeQuery(searchByType, SourceOrganisationProperty.class).setParameter("types", distinctTrimedTypes);
    }

    final String searchByTypeAndCustNr = "SELECT pro.ORGANISATION_ID AS ORGANISATION_ID, pro.TYPE AS TYPE, pro.VALUE AS VALUE "
        + "FROM SHOP.ORGANISATIONPROPERTY pro INNER JOIN SHOP.ORGANISATION org ON org.ID = pro.ORGANISATION_ID "
        + "WHERE org.ERPNUMBER IN (:custNrs) AND pro.TYPE IN (:types)";
    return this.entityManager.createNativeQuery(searchByTypeAndCustNr, SourceOrganisationProperty.class)
        .setParameter("types", QueryUtils.getDistinctTrimedValues(distinctTrimedTypes)).setParameter("custNrs", distinctTrimedCustNrs);
  }

  @Override
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
}
