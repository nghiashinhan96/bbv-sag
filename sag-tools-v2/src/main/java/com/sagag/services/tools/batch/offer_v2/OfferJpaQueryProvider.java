package com.sagag.services.tools.batch.offer_v2;

import org.springframework.batch.item.database.orm.JpaQueryProvider;

import javax.persistence.EntityManager;

public abstract class OfferJpaQueryProvider implements JpaQueryProvider {

  private EntityManager em;

  @Override
  public void setEntityManager(EntityManager entityManager) {
    this.em = entityManager;
  }

  public EntityManager getEntityManager() {
    return this.em;
  }
}
