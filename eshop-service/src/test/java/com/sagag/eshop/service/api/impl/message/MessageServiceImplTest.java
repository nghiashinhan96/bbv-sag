package com.sagag.eshop.service.api.impl.message;

import static org.mockito.Mockito.times;

import com.sagag.eshop.repo.api.message.MessageHidingRepository;
import com.sagag.eshop.repo.api.message.MessageRepository;
import com.sagag.eshop.repo.entity.message.MessageHiding;
import com.sagag.eshop.service.api.impl.MessageServiceImpl;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.tests.utils.UserInfoTestUtils;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.common.EshopAuthority;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Test class for MessageServiceImpl.
 *
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class MessageServiceImplTest {

  @InjectMocks
  private MessageServiceImpl messageServiceImpl;

  @Mock
  private MessageHidingRepository messageHidingRepo;

  @Mock
  private MessageRepository messageRepo;

  @Test
  public void testHideMessages() {
    Mockito.when(messageHidingRepo.findHidingMessagesByUser(13L)).thenReturn(new ArrayList<>());
    messageServiceImpl.hideMessages(13L, "33, ");
    Mockito.verify(messageHidingRepo, times(1)).save(Mockito.any(MessageHiding.class));
  }

  @Test
  public void testGetMessages() {
    UserInfo user = UserInfoTestUtils.buildOfferUserInfo();
    user.setRoles(Arrays.asList(EshopAuthority.SALES_ASSISTANT.name()));
    final String affiliate = SupportedAffiliate.DERENDINGER_CH.getAffiliate();
    Mockito.when(messageRepo.findAuthedMessages(user.getRoles(), "de", affiliate,
        user.getCustNrStr())).thenReturn(new ArrayList<>());
    Mockito.when(messageHidingRepo.findHidingMessagesByUser(user.getId())).thenReturn(new ArrayList<>());

    messageServiceImpl.getAuthorizedMessages(user, affiliate);
    Mockito.verify(messageHidingRepo, times(1)).findHidingMessagesByUser(user.getId());
  }

}
