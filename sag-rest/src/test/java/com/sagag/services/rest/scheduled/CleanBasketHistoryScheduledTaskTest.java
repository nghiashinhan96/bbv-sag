package com.sagag.services.rest.scheduled;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.BasketHistoryRepository;
import com.sagag.eshop.repo.entity.BasketHistory;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.service.mail.ChangePasswordMailSender;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * UT for {@link ChangePasswordMailSender}.
 */
@EshopMockitoJUnitRunner
public class CleanBasketHistoryScheduledTaskTest {

  @InjectMocks
  private CleanBasketHistoryScheduledTask tasks;

  @Mock
  private Environment env;

  @Mock
  private BasketHistoryRepository basketHistoryRepo;

  @Before
  public void init() {
    when(env.getProperty("days.basket_histories.remove_old_items", Integer.class)).thenReturn(60);
    when(env.getProperty("max_result.basket_histories.remove_old_items", Integer.class))
        .thenReturn(10);
  }

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testClearOldBasketHistories() {
    when(basketHistoryRepo.findByUpdatedDateBefore(any(Date.class)))
        .thenReturn(createBasketHistories());
    doNothing().when(basketHistoryRepo).deleteInBatch(anyList());

    tasks.executeTask();

    verify(env, times(1)).getProperty("days.basket_histories.remove_old_items", Integer.class);
    verify(env, times(1)).getProperty("max_result.basket_histories.remove_old_items",
        Integer.class);
    verify(basketHistoryRepo, times(1)).findByUpdatedDateBefore(any(Date.class));
    verify(basketHistoryRepo, times(2)).deleteInBatch(anyList());
  }

  @Test
  public void testClearOldBasketHistories_WithEmptyBasketHistoies() {
    when(basketHistoryRepo.findByUpdatedDateBefore(any(Date.class)))
        .thenReturn(Collections.emptyList());
    doNothing().when(basketHistoryRepo).deleteInBatch(anyList());

    tasks.executeTask();

    verify(env, times(1)).getProperty("days.basket_histories.remove_old_items", Integer.class);
    verify(env, times(1)).getProperty("max_result.basket_histories.remove_old_items",
        Integer.class);
    verify(basketHistoryRepo, times(1)).findByUpdatedDateBefore(any(Date.class));
    verify(basketHistoryRepo, times(0)).deleteInBatch(anyList());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testClearOldBasketHistories_WithIllegalParameters_1() {
    when(env.getProperty("days.basket_histories.remove_old_items", Integer.class)).thenReturn(null);
    when(basketHistoryRepo.findByUpdatedDateBefore(any(Date.class)))
        .thenReturn(Collections.emptyList());
    doNothing().when(basketHistoryRepo).deleteInBatch(anyList());

    tasks.executeTask();

    verify(env, times(1)).getProperty("days.basket_histories.remove_old_items", Integer.class);
    verify(env, times(0)).getProperty("max_result.basket_histories.remove_old_items",
        Integer.class);
    verify(basketHistoryRepo, times(0)).findByUpdatedDateBefore(any(Date.class));
    verify(basketHistoryRepo, times(0)).deleteInBatch(anyList());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testClearOldBasketHistories_WithIllegalParameters_2() {
    when(env.getProperty("max_result.basket_histories.remove_old_items", Integer.class))
        .thenReturn(null);
    when(basketHistoryRepo.findByUpdatedDateBefore(any(Date.class)))
        .thenReturn(Collections.emptyList());
    doNothing().when(basketHistoryRepo).deleteInBatch(anyList());

    tasks.executeTask();

    verify(env, times(1)).getProperty("days.basket_histories.remove_old_items", Integer.class);
    verify(env, times(1)).getProperty("max_result.basket_histories.remove_old_items",
        Integer.class);
    verify(basketHistoryRepo, times(0)).findByUpdatedDateBefore(any(Date.class));
    verify(basketHistoryRepo, times(0)).deleteInBatch(anyList());
  }

  private static List<BasketHistory> createBasketHistories() {

    ArrayList<BasketHistory> basketHistories = new ArrayList<>();

    basketHistories.add(createBasketHistory(1L, DateTime.now().minusDays(1).toDate()));
    basketHistories.add(createBasketHistory(2L, DateTime.now().plusDays(1).toDate()));
    basketHistories.add(createBasketHistory(3L, DateTime.now().minusDays(2).toDate()));
    basketHistories.add(createBasketHistory(4L, DateTime.now().minusDays(3).toDate()));
    basketHistories.add(createBasketHistory(5L, DateTime.now().minusDays(4).toDate()));
    basketHistories.add(createBasketHistory(6L, DateTime.now().minusDays(5).toDate()));
    basketHistories.add(createBasketHistory(7L, DateTime.now().toDate()));
    basketHistories.add(createBasketHistory(8L, DateTime.now().toDate()));
    basketHistories.add(createBasketHistory(9L, DateTime.now().toDate()));
    basketHistories.add(createBasketHistory(10L, DateTime.now().toDate()));
    basketHistories.add(createBasketHistory(11L, DateTime.now().toDate()));

    return basketHistories;
  }

  private static BasketHistory createBasketHistory(Long id, Date updatedAt) {
    BasketHistory basketHistory = new BasketHistory();
    basketHistory.setId(id);
    basketHistory.setUpdatedDate(updatedAt);
    return basketHistory;
  }

}
