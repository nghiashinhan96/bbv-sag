package com.sagag.services.service.feedback;

import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.VUserDetailRepository;
import com.sagag.eshop.repo.api.feedback.FeedbackRepository;
import com.sagag.eshop.repo.api.feedback.FeedbackStatusRepository;
import com.sagag.eshop.repo.api.feedback.FeedbackTopicRepository;
import com.sagag.eshop.repo.entity.feedback.Feedback;
import com.sagag.eshop.repo.entity.feedback.FeedbackTopic;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.service.dto.feedback.FeedbackParentDataItem;
import com.sagag.services.service.dto.feedback.FeedbackSourceDto;
import com.sagag.services.service.feedback.mail.FeedbackSendingEmailProcessor;
import com.sagag.services.service.request.FeedbackMessageRequest;
import com.sagag.services.service.resource.FeedbackMasterDataResource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public abstract class AbstractFeedbackHandler<T> implements FeedbackHandler<T> {

  private static final String DF_FEEDBACK_STATUS = "NOT_STARTED";

  @Autowired
  private FeedbackRepository feedbackRepo;

  @Autowired
  protected FeedbackTopicRepository feedbackTopicRepo;

  @Autowired
  private FeedbackStatusRepository feedbackStatusRepo;

  @Autowired
  private VUserDetailRepository vUserDetailRepo;

  @Autowired
  protected EshopUserRepository eshopUserRepository;

  @Autowired
  private LocaleContextHelper localeContextHelper;

  protected abstract FeedbackSendingEmailProcessor getSendingEmailProcessor();

  protected abstract T getUserData(UserInfo user);

  @Override
  public void create(UserInfo user, FeedbackMessageRequest request) throws UserValidationException {
    validRequest(request);
    final String topicCode = request.getTopic().getTopicCode();
    final FeedbackTopic topic = feedbackTopicRepo.findByTopicCode(topicCode);
    if (topic == null) {
      final String msg = String.format("The given topic code = %s is not existing", topicCode);
      throw new NoSuchElementException(msg);
    }

    final int topicId = topic.getId();
    final Locale locale = localeContextHelper.toLocale(request.getLang());
    Long salesId = findSalesId(user);
    Long createdFeedbackId = storeFeedback(findCreatedUserId(user), salesId, topicId, request);

    String salesEmail = StringUtils.EMPTY;
    if (Objects.nonNull(salesId)) {
      salesEmail = eshopUserRepository.findEmailById(salesId).orElse(StringUtils.EMPTY);
    }
    getSendingEmailProcessor().processThenSendingEmail(user, topicId, createdFeedbackId, request, locale, salesEmail);
  }

  private Long findSalesId(UserInfo user) {
    if (user.isSalesNotOnBehalf()) {
      return user.getId();
    }
    if (user.isSaleOnBehalf()) {
      return user.getSalesId();
    }
    return null;
  }

  private Long findCreatedUserId(UserInfo user) {
    if (user.isSaleOnBehalf()) {
      return user.getSalesId();
    }
    return user.getId();
  }

  private long storeFeedback(Long userId, Long salesId, Integer topicId,
      FeedbackMessageRequest request) {
    final Feedback feedback = new Feedback();
    feedback.setUserId(userId);
    feedback.setSalesId(salesId);
    feedback.setTopicId(topicId);
    feedback.setCreatedDate(Calendar.getInstance().getTime());
    feedback.setCreatedUserId(userId);
    feedback.setUserInformation(request.getUserData().toJson());
    feedback.setFeedbackMessage(request.getMessage().getContent());
    feedback.setTechnicalInformation(request.getTechnicalData().toJson());
    feedback.setSalesInformation(Optional.ofNullable(request.getSalesInfo())
        .map(FeedbackParentDataItem::toJson).orElse(null));
    feedback.setSource(
        Optional.ofNullable(request.getSource()).map(FeedbackSourceDto::getCode).orElse(null));

    feedback.setOrgId(vUserDetailRepo.findOrgIdByUserId(userId).orElse(null));

    Optional<Integer> statusId = feedbackStatusRepo.findIdByStatusCode(DF_FEEDBACK_STATUS);
    if (!statusId.isPresent()) {
      final String msg =
          String.format("The default feedback status %s is not existing", DF_FEEDBACK_STATUS);
      throw new NoSuchElementException(msg);
    }
    feedback.setStatusId(statusId.get());
    Feedback createFeedback = feedbackRepo.save(feedback);
    return createFeedback.getId();
  }

  @Override
  public FeedbackMasterDataResource<T> getFeedbackUserData(UserInfo user) {
    FeedbackMasterDataResource<T> resource = new FeedbackMasterDataResource<>();
    resource.setTopicCodes(feedbackTopicRepo.findSortedTopicCodes());
    resource.setUserData(getUserData(user));
    return resource;
  }

  private void validRequest(FeedbackMessageRequest request) {
    Assert.notNull(request, "The given feedback request must not be null");
    Assert.notNull(request.getTopic(), "The given topic must not be null");
    Assert.notNull(request.getMessage(), "The given message must not be null");
    Assert.notNull(request.getTechnicalData(), "The given technical data must not be null");
    Assert.notNull(request.getAffiliateStore(), "The given affilliate store must not be null");
    Assert.notNull(request.getUserData(), "The given user data must not be null");
  }
}
