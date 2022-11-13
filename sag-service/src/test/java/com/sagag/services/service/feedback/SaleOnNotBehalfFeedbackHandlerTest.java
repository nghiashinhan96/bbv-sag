package com.sagag.services.service.feedback;

import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.entity.feedback.Feedback;
import com.sagag.eshop.repo.entity.feedback.FeedbackTopic;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.service.dto.feedback.FeedBackSalesNotOnBehalfUserDataDto;
import com.sagag.services.service.feedback.mail.SalesNotOnBehalfFeedbackSendingEmailProcessor;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class SaleOnNotBehalfFeedbackHandlerTest extends AbstractFeedbackHandlerTest {

  @InjectMocks
  private SalesNotOnBehalfFeedbackHandler salesNotOnBehalfFeedbackHandler;

  @Mock
  private SalesNotOnBehalfFeedbackSendingEmailProcessor salesNotOnBehalfFeedbackSendingEmailProcessor;

  @Mock
  private LocaleContextHelper localeContextHelper;

  @Test
  public void create_shouldCreateSuccessfully_givenUserInfoAndRequestCriteria() throws Exception {
    FeedbackTopic topic = FeedbackTopic.builder().id(1).topicCode("PRICING").build();
    when(feedbackTopicRepo.findByTopicCode(anyString())).thenReturn(topic);
    when(vUserDetailRepo.findOrgIdByUserId(anyLong())).thenReturn(Optional.of(1));
    when(feedbackStatusRepo.findIdByStatusCode(anyString())).thenReturn(Optional.of(1));

    Feedback feedback = Feedback.builder().id(1).build();
    when(feedbackRepo.save(any(Feedback.class))).thenReturn(feedback);
    salesNotOnBehalfFeedbackHandler.create(buildUserInfo(), buildFeedbackMessageRequest());
  }

  @Test(expected = IllegalArgumentException.class)
  public void create_shouldThrowException_givenNullRequestCriteria() throws Exception {
    salesNotOnBehalfFeedbackHandler.create(buildUserInfo(), null);
  }

  @Test(expected = NoSuchElementException.class)
  public void create_shouldThrowException_givenCriteriaWithNotFoundTopicCode() throws Exception {
    when(feedbackTopicRepo.findByTopicCode(anyString())).thenReturn(null);
    salesNotOnBehalfFeedbackHandler.create(buildUserInfo(), buildFeedbackMessageRequest());
  }

  @Test(expected = NoSuchElementException.class)
  public void create_shouldThrowException_givenCriteriaWithNotFoundOrgId() throws Exception {
    FeedbackTopic topic = FeedbackTopic.builder().id(1).topicCode("PRICING").build();
    when(feedbackTopicRepo.findByTopicCode(anyString())).thenReturn(topic);
    when(vUserDetailRepo.findOrgIdByUserId(anyLong())).thenReturn(Optional.empty());
    salesNotOnBehalfFeedbackHandler.create(buildUserInfo(), buildFeedbackMessageRequest());
  }

  @Test(expected = NoSuchElementException.class)
  public void create_shouldThrowException_givenCriteriaWithNotFoundStatus() throws Exception {
    FeedbackTopic topic = FeedbackTopic.builder().id(1).topicCode("PRICING").build();
    when(feedbackTopicRepo.findByTopicCode(anyString())).thenReturn(topic);
    when(vUserDetailRepo.findOrgIdByUserId(anyLong())).thenReturn(Optional.of(1));
    when(feedbackStatusRepo.findIdByStatusCode(anyString())).thenReturn(Optional.empty());
    salesNotOnBehalfFeedbackHandler.create(buildUserInfo(), buildFeedbackMessageRequest());
  }

  @Test
  public void getUserData_shouldGetMasterData_givenUserInfo() throws Exception {
    List<String> topices = Arrays.asList("PRICING", "EASE_OF_USE");
    when(feedbackTopicRepo.findSortedTopicCodes()).thenReturn(topices);
    when(eshopUserRepository.findPhoneById(anyLong())).thenReturn(Optional.of("01234567"));
    FeedBackSalesNotOnBehalfUserDataDto userData =
        salesNotOnBehalfFeedbackHandler.getUserData(buildUserInfo());
    assertThat(userData.getUserPhone(), Matchers.is("01234567"));
  }
}
