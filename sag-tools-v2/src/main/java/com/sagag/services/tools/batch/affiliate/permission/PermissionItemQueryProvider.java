package com.sagag.services.tools.batch.affiliate.permission;

import com.sagag.services.tools.domain.target.EshopGroup;

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
public class PermissionItemQueryProvider implements JpaQueryProvider {

  @NonNull
  private Integer affliateId;

  @NonNull
  List<String> excludedCustNrs;

  private EntityManager entityManager;

  @Override
  public Query createQuery() {
    if (CollectionUtils.isNotEmpty(excludedCustNrs)) {
      String customerList = "(" + String.join(",", excludedCustNrs) + ")";
      final String excludedCustomerQuery = "select gr from EshopGroup gr join gr.organisationGroups orgGr "
          + "join orgGr.organisation org where org.parentId =:affliateId and org.orgCode not in " + customerList;
      return this.entityManager.createQuery(excludedCustomerQuery, EshopGroup.class).setParameter("affliateId", affliateId);
    }

    final String query = "select gr from EshopGroup gr join gr.organisationGroups orgGr " + "join orgGr.organisation org where org.parentId =:affliateId";
    return this.entityManager.createQuery(query, EshopGroup.class).setParameter("affliateId", affliateId);
  }

  @Override
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
}
