package com.sagag.services.tools.batch.offer_feature.offer_status;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.TargetOffer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@StepScope
@Slf4j
@OracleProfile
public class AdjustOfferStatusItemProcessor implements ItemProcessor<TargetOffer, TargetOffer> {

  @Override
  public TargetOffer process(TargetOffer source) {
    Assert.notNull(source, "The given source offer must not be null");
    final String eblOfferStatus = source.getStatus();
    log.debug("Source offer status of id = {} is {}", source.getId(), eblOfferStatus);
    final ConnectOfferStatus connectOfferStatus = mapSourceToTargetStatus(eblOfferStatus);
    if (connectOfferStatus == null) {
      // Ignoring
      log.warn("Ignoring this record");
      return source;
    }
    log.debug("Target offer status is = {}", connectOfferStatus);
    source.setStatus(connectOfferStatus.name());
    return source;
  }

  private static ConnectOfferStatus mapSourceToTargetStatus(String status) {
    Assert.hasText(status, "The given status must not be empty");
    return valueOf(EblOfferStatus.valueOf(status));
  }

  private static ConnectOfferStatus valueOf(EblOfferStatus eblOfferStatus) {
    Assert.notNull(eblOfferStatus, "The EBL offer status must not be null");
    switch (eblOfferStatus) {
      case NEW:
      case INPROCESS:
        return ConnectOfferStatus.OPEN;

      case ORDERED:
      case PAID:
      case INDELIVERY:
      case DELIVERED:
        return ConnectOfferStatus.ORDERED;

      default:
        return null;
    }
  }

}
