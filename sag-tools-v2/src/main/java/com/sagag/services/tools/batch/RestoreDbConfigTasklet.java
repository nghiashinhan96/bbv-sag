package com.sagag.services.tools.batch;

import com.sagag.services.tools.support.OfferTables;
import com.sagag.services.tools.utils.EntityManagerUtils;

import org.hibernate.internal.SessionImpl;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class RestoreDbConfigTasklet extends AbstractTasklet {

  @Autowired
  @Qualifier("targetEntityManager")
  private EntityManager targetEntityManager;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    EntityManagerUtils.turnOffIdentityInsert(session, OfferTables.SHOP_ARTICLE.getTableName());
    EntityManagerUtils.turnOffIdentityInsert(session, OfferTables.OFFER_PERSON.getTableName());
    EntityManagerUtils.turnOffIdentityInsert(session, OfferTables.OFFER_ADDRESS.getTableName());
    EntityManagerUtils.turnOffIdentityInsert(session, OfferTables.OFFER.getTableName());
    EntityManagerUtils.turnOffIdentityInsert(session, OfferTables.OFFER_POSITION.getTableName());
    return finish(contribution);
  }
}
