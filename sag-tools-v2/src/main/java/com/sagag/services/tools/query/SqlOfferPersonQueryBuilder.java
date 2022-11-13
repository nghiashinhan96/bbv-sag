package com.sagag.services.tools.query;

import com.sagag.services.tools.batch.offer_feature.offer_person.OfferPersonType;

import lombok.Builder;
import lombok.Data;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Data
@Builder(buildMethodName = "build")
public class SqlOfferPersonQueryBuilder {

  private EntityManager em;

  private String selector;

  private List<Long> vendorIds;

  private OfferPersonType personType;

  private Class<?> clazz;

  public Query createQuery() {
    return this.em.createQuery(getPersonByVendorIdsQuery(selector), clazz)
        .setParameter("type", personType.name())
        .setParameter("vendorIds", vendorIds);
  }

  private static String getPersonByVendorIdsQuery(String selector) {
    final StringBuilder sql = new StringBuilder();
    if ("*".equals(selector)) {
      sql.append("SELECT p ");
    } else {
      sql.append("SELECT p.").append(selector).append(StringUtils.SPACE);
    }
    sql.append("FROM SourcePerson p, ");
    sql.append("SourceLoginRoleAssignment lra, ");
    sql.append("SourceOrganisationLink ol, ");
    sql.append("SourceOrganisation org ");
    sql.append("WHERE lra.personId = p.id ");
    sql.append("AND ol.clientId = lra.orgId ");
    sql.append("AND org.id = ol.vendorId ");
    sql.append("AND org.id IN (:vendorIds)");
    sql.append("AND p.type= :type ");
    return sql.toString();
  }


}
