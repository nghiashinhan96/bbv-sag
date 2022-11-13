package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.dest.DestFinalCustomerOrderItem;
import com.sagag.services.copydb.repo.dest.DestFinalCustomerOrderItemRepository;
import com.sagag.services.copydb.utils.DbUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.internal.SessionImpl;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

@Component
@CopyDbProfile
public class DestFinalCustomerOrderItemWriter implements ItemWriter<DestFinalCustomerOrderItem> {

  @Autowired
  private DestFinalCustomerOrderItemRepository destFinalCustomerOrderItemRepository;

  @Autowired
  @Qualifier("targetEntityManager")
  private EntityManager targetEntityManager;

  @Override
  public void write(List<? extends DestFinalCustomerOrderItem> items) throws Exception {
    List<DestFinalCustomerOrderItem> nonNullsItems = items.stream().filter(Objects::nonNull).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(nonNullsItems)) {
      return;
    }
    SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    DbUtils.turnOnIdentityInsert(session, "FINAL_CUSTOMER_ORDER_ITEM");
    destFinalCustomerOrderItemRepository.saveAll(nonNullsItems);
  }
}
