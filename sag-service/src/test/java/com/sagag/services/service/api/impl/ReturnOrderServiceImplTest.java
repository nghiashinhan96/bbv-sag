package com.sagag.services.service.api.impl;

import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.sagag.eshop.repo.api.ReturnOrderReasonRepository;
import com.sagag.eshop.repo.entity.ReturnOrderReason;
import com.sagag.services.ax.exception.AxResultNotFoundException;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.sag.returnorder.ReturnOrderReasonDto;
import com.sagag.services.domain.sag.returnorder.TransactionReferenceDto;
import com.sagag.services.service.returnorder.ReturnTransactionReferencesFilter;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ReturnOrderServiceImplTest {

  private static final SupportedAffiliate AFF = SupportedAffiliate.DERENDINGER_AT;

  @InjectMocks
  private ReturnOrderServiceImpl service;

  @Mock
  private ReturnTransactionReferencesFilter returnTransactionReferencesFilter;

  @Mock
  private ReturnOrderReasonRepository returnOrderReasonRepo;

  @Test
  public void shouldSearchTransRefs_EmptyList_RefNr_3000018769761() {
    final String reference = "3000018769761";

    when(returnTransactionReferencesFilter
        .filterTransactionReferences(AFF, reference, StringUtils.EMPTY))
        .thenReturn(Collections.emptyList());

    final List<TransactionReferenceDto> result =
        service.searchTransactionReferences(AFF, reference, StringUtils.EMPTY);

    Assert.assertThat(result.size(), Matchers.equalTo(0));
  }

  @Test
  public void shouldSearchTransRefs_Successful_RefNr_300001876976() {
    final String reference = "300001876976";

    when(returnTransactionReferencesFilter
        .filterTransactionReferences(AFF, reference, StringUtils.EMPTY))
        .thenReturn(ImmutableList.of(new TransactionReferenceDto()));

    final List<TransactionReferenceDto> result =
        service.searchTransactionReferences(AFF, reference, StringUtils.EMPTY);

    Assert.assertThat(result.size(), Matchers.equalTo(1));
  }

  @Test
  public void shouldSearchTransRefs_Successful_OrderNr_AU3010788868() {
    final String reference = "AU3010788868";
    when(returnTransactionReferencesFilter
        .filterTransactionReferences(AFF, reference, StringUtils.EMPTY))
        .thenReturn(ImmutableList.of(new TransactionReferenceDto(), new TransactionReferenceDto()));

    final List<TransactionReferenceDto> result =
        service.searchTransactionReferences(AFF, reference, StringUtils.EMPTY);

    Assert.assertThat(result.size(), Matchers.equalTo(2));
  }

  @Test(expected = AxResultNotFoundException.class)
  public void shouldSearchTransRefs_NotFound_OrderNr_AU30107888681() {
    final String reference = "AU3010788868";

    AxResultNotFoundException ex = new AxResultNotFoundException("Not found result");
    when(returnTransactionReferencesFilter
        .filterTransactionReferences(AFF, reference, StringUtils.EMPTY))
        .thenThrow(ex);

   service.searchTransactionReferences(AFF, reference, StringUtils.EMPTY);
  }

  @Test
  public void shouldReturnAllReason() {
    when(returnOrderReasonRepo.findAll())
    .thenReturn(Arrays.asList(new ReturnOrderReason(), new ReturnOrderReason(),
        new ReturnOrderReason()));

    final List<ReturnOrderReasonDto> reason = service.getReturnOrderReason();
    Assert.assertThat(reason.size(), Matchers.is(3));
  }
}
