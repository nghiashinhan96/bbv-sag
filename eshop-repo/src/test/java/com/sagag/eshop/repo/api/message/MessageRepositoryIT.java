package com.sagag.eshop.repo.api.message;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.message.dto.MessageDto;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Integration test class for role {@link MessageRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class MessageRepositoryIT {

  @Autowired
  private MessageRepository messageRepository;

  @Test
  public void testFindNoAuthedMessagesOK() throws Exception {
    List<MessageDto> messages = messageRepository
        .findNoAuthedMessages(Locale.GERMAN.getLanguage(),
            SupportedAffiliate.DERENDINGER_AT.getAffiliate());
    Assert.assertTrue(CollectionUtils.isNotEmpty(messages));
  }

  @Test
  public void testFindNoAuthedMessagesFailed() throws Exception {
    List<MessageDto> messages = messageRepository
        .findNoAuthedMessages(Locale.GERMAN.getLanguage(),
            SupportedAffiliate.TECHNOMAG.getAffiliate());
    Assert.assertTrue(CollectionUtils.isEmpty(messages));
  }

  @Test
  public void testFindMessagesByAffiliateAndCustomerOK() throws Exception {
    String customerNr = "1111101";
    List<MessageDto> messages =
        messageRepository.findAuthedMessages(Arrays.asList(EshopAuthority.USER_ADMIN.name()),
            Locale.GERMAN.getLanguage(),
            SupportedAffiliate.DERENDINGER_AT.getAffiliate(), customerNr);
    Assert.assertTrue(CollectionUtils.isNotEmpty(messages));
  }

  @Test
  public void testFindMessagesByAffiliateAndCustomerFailed() throws Exception {
    String customerNr = "12345";
    List<MessageDto> messages = messageRepository
        .findAuthedMessages(Arrays.asList(EshopAuthority.USER_ADMIN.name()),
            Locale.GERMAN.getLanguage(),
            SupportedAffiliate.TECHNOMAG.getAffiliate(), customerNr);
    Assert.assertTrue(CollectionUtils.isEmpty(messages));
  }

  @Test
  public void isValidPeriod_shouldValidPeriod_givenTheFutureDate() throws Exception {
    Date fromDate = new GregorianCalendar(2021, Calendar.JANUARY, 11).getTime();
    Date toDate = new GregorianCalendar(2021, Calendar.FEBRUARY, 11).getTime();
    boolean isValidPeriod = messageRepository
        .isValidPeriod(1, Arrays.asList(SupportedAffiliate.DERENDINGER_AT.getAffiliate()), 1, 4, 1, fromDate, toDate);
    Assert.assertTrue(isValidPeriod);
  }

  @Test
  public void isValidPeriod_shouldValidPeriod_givenThePassDate() throws Exception {
    Date fromDate = new GregorianCalendar(2018, Calendar.JANUARY, 11).getTime();
    Date toDate = new GregorianCalendar(2018, Calendar.FEBRUARY, 11).getTime();
    boolean isValidPeriod = messageRepository
        .isValidPeriod(1, Arrays.asList(SupportedAffiliate.DERENDINGER_AT.getAffiliate()), 1, 4, 1, fromDate, toDate);
    Assert.assertTrue(isValidPeriod);
  }

  @Test
  public void isValidPeriod_shouldInValidPeriod_givenOverlapDate() throws Exception {
    Date fromDate = new GregorianCalendar(2019, Calendar.JUNE, 11).getTime();
    Date toDate = new GregorianCalendar(2019, Calendar.JULY, 11).getTime();
    boolean isValidPeriod = messageRepository
        .isValidPeriod(1, Arrays.asList(SupportedAffiliate.DERENDINGER_AT.getAffiliate()), 1, 4, 1, fromDate, toDate);
    Assert.assertFalse(isValidPeriod);
  }
}
