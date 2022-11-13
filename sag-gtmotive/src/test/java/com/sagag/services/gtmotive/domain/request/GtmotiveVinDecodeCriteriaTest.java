package com.sagag.services.gtmotive.domain.request;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class GtmotiveVinDecodeCriteriaTest {

  @Test
  public void testModifiedVin() {

    final String expVin = "zfa19800004361472";
    final String[] vinList = { "zfa19800004361472", "zfa198oooo4361472", "zfa198OOOO4361472",
        "zfa198oOoO4361472", "zfa198OOoo4361472", "zfa198Oo004361472" };

    final GtmotiveVinDecodeCriteria criteria = new GtmotiveVinDecodeCriteria();
    for (String vin : vinList) {
      log.debug("VIN = {}", vin);
      criteria.setVin(vin);
      Assert.assertThat(criteria.getModifiedVin(), Matchers.equalTo(expVin));
    }

  }


}
