package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.dest.DestOfferPersonProperty;
import com.sagag.services.copydb.repo.dest.DestOfferPersonPropertyRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@CopyDbProfile
public class DestOfferPersonPropertyWriter implements ItemWriter<DestOfferPersonProperty> {

  @Autowired
  private DestOfferPersonPropertyRepository destOfferPersonPropertyRepository;

  @Override
  public void write(List<? extends DestOfferPersonProperty> items) throws Exception {
    List<DestOfferPersonProperty> nonNullsItems = items.stream().filter(Objects::nonNull).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(nonNullsItems)) {
      return;
    }
    destOfferPersonPropertyRepository.saveAll(nonNullsItems);
  }
}
