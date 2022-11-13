package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.dest.DestMessageType;
import com.sagag.services.copydb.repo.dest.DestMessageTypeRepository;
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
public class DestMessageTypeWriter implements ItemWriter<DestMessageType> {

  @Autowired
  private DestMessageTypeRepository destMessageTypeRepository;

  @Autowired
  @Qualifier("targetEntityManager")
  private EntityManager targetEntityManager;

  @Override
  public void write(List<? extends DestMessageType> items) throws Exception {
    List<DestMessageType> nonNullsItems = items.stream().filter(Objects::nonNull).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(nonNullsItems)) {
      return;
    }
    SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    DbUtils.turnOnIdentityInsert(session, "MESSAGE_TYPE");
    destMessageTypeRepository.saveAll(nonNullsItems);
  }
}
