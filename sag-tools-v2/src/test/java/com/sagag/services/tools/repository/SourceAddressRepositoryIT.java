package com.sagag.services.tools.repository;

import com.sagag.services.tools.domain.source.SourceAddress;
import com.sagag.services.tools.repository.source.SourceAddressRepository;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Integration test class for source address.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@ActiveProfiles("test")
public class SourceAddressRepositoryIT {

  private static final long TEST_PERSON_ID = 282495555L;

  @Autowired
  private SourceAddressRepository sourceAddressRepository;

  @Test
  @Ignore("enable the test with configuration")
  public void testFindAddressByByPersonId() {
    final List<SourceAddress> addresses = sourceAddressRepository.findAddressByByPersonId(TEST_PERSON_ID);
    log.debug("Result = {}", addresses);
    Assert.assertThat(addresses, Matchers.not(Matchers.empty()));
  }

}
