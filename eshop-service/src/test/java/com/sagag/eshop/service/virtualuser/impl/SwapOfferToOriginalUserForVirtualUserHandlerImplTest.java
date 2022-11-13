package com.sagag.eshop.service.virtualuser.impl;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.offer.OfferAddressRepository;
import com.sagag.eshop.repo.api.offer.OfferPersonRepository;
import com.sagag.eshop.repo.api.offer.OfferPositionRepository;
import com.sagag.eshop.repo.api.offer.OfferRepository;
import com.sagag.eshop.repo.entity.VVirtualUser;
import com.sagag.eshop.repo.entity.offer.Offer;
import com.sagag.eshop.repo.entity.offer.OfferAddress;
import com.sagag.eshop.repo.entity.offer.OfferPerson;
import com.sagag.eshop.repo.entity.offer.OfferPosition;
import com.sagag.eshop.service.DataProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
public class SwapOfferToOriginalUserForVirtualUserHandlerImplTest {

  @InjectMocks
  private SwapOfferToOriginalUserForVirtualUserHandlerImpl handler;

  @Mock
  private OfferRepository offerRepo;

  @Mock
  private OfferPersonRepository offerPersonRepo;

  @Mock
  private OfferAddressRepository offerAddressRepo;

  @Mock
  private OfferPositionRepository offerPositionRepo;

  @Test
  public void givenVVirtualUserListShouldSwapOfferData() {
    executeTestCase(DataProvider.buildVirtualUserList(), 1);
  }

  @Test
  public void givenEmptyVVirtualUserListShouldNonSwapOfferData() {
    handler.accept(Collections.emptyList());
  }

  private void executeTestCase(List<VVirtualUser> users, int numberOfTimes) {
    final List<Offer> offers = Arrays.asList(new Offer());
    when(offerRepo.findByCreatedUserIds(anySet())).thenReturn(offers);
    when(offerRepo.findByModifiedUserIds(anySet())).thenReturn(offers);
    when(offerRepo.saveAll(anyList())).thenReturn(offers);

    final List<OfferPosition> positions = Arrays.asList(new OfferPosition());
    when(offerPositionRepo.findByCreatedUserIds(anySet())).thenReturn(positions);
    when(offerPositionRepo.findByModifiedUserIds(anySet())).thenReturn(positions);
    when(offerPositionRepo.saveAll(anyList())).thenReturn(positions);

    final List<OfferAddress> addrs = Arrays.asList(new OfferAddress());
    when(offerAddressRepo.findByCreatedUserIds(anySet())).thenReturn(addrs);
    when(offerAddressRepo.findByModifiedUserIds(anySet())).thenReturn(addrs);
    when(offerAddressRepo.saveAll(anyList())).thenReturn(addrs);

    final List<OfferPerson> people = Arrays.asList(new OfferPerson());
    when(offerPersonRepo.findByCreatedUserIds(anySet())).thenReturn(people);
    when(offerPersonRepo.findByModifiedUserIds(anySet())).thenReturn(people);
    when(offerPersonRepo.saveAll(anyList())).thenReturn(people);

    handler.accept(users);

    verify(offerRepo, times(numberOfTimes)).findByCreatedUserIds(anySet());
    verify(offerRepo, times(numberOfTimes)).findByModifiedUserIds(anySet());
    verify(offerRepo, times(2)).saveAll(anyList());

    verify(offerPositionRepo, times(numberOfTimes)).findByCreatedUserIds(anySet());
    verify(offerPositionRepo, times(numberOfTimes)).findByModifiedUserIds(anySet());
    verify(offerPositionRepo, times(2)).saveAll(anyList());

    verify(offerAddressRepo, times(numberOfTimes)).findByCreatedUserIds(anySet());
    verify(offerAddressRepo, times(numberOfTimes)).findByModifiedUserIds(anySet());
    verify(offerAddressRepo, times(2)).saveAll(anyList());

    verify(offerPersonRepo, times(numberOfTimes)).findByCreatedUserIds(anySet());
    verify(offerPersonRepo, times(numberOfTimes)).findByModifiedUserIds(anySet());
    verify(offerPersonRepo, times(2)).saveAll(anyList());
  }
}
