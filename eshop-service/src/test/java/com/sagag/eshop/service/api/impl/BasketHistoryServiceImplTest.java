package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.BasketHistoryRepository;
import com.sagag.eshop.repo.entity.BasketHistory;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

/**
 * UT to verify for {@link BasketHistoryServiceImpl}.
 */
@EshopMockitoJUnitRunner
public class BasketHistoryServiceImplTest {

  @InjectMocks
  private BasketHistoryServiceImpl basketHistoryService;

  @Mock
  private BasketHistoryRepository basketHistoryRepo;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    Mockito.when(basketHistoryRepo.findByUpdatedDateBefore(Mockito.any(Date.class)))
    .thenReturn(Collections.emptyList());
  }

  @Test
  public void testFindByUpdatedDateBefore() {
    List<BasketHistory> histories = basketHistoryRepo.findByUpdatedDateBefore(DateTime.now().toDate());

    Assert.assertThat(histories.size(), Matchers.greaterThanOrEqualTo(0));
  }

}
