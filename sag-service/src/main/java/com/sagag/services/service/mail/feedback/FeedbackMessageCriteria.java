package com.sagag.services.service.mail.feedback;

import com.sagag.eshop.service.dto.EmailAttachment;
import com.sagag.services.service.dto.feedback.FeedbackMessageContentDto;
import com.sagag.services.service.dto.feedback.FeedbackParentDataItem;
import com.sagag.services.service.dto.feedback.FeedbackSourceDto;
import com.sagag.services.service.dto.feedback.FeedbackTopicDto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Locale;

@Data
@Builder
public class FeedbackMessageCriteria {

  @NonNull
  private String toEmail;

  @NonNull
  private String fromEmail;

  private String[] ccEmail;

  @NonNull
  private FeedbackTopicDto topic;

  @NonNull
  private String defaultBranch;

  @NonNull
  private Long feedbackId;

  @NonNull
  private FeedbackMessageContentDto message;

  private FeedbackParentDataItem salesInfo;
  
  @NonNull
  private FeedbackParentDataItem userData;

  private FeedbackParentDataItem userInfo;

  @NonNull
  private FeedbackParentDataItem technicalData;

  @NonNull
  private Locale locale;

  private String[] subjectParams;

  private FeedbackSourceDto source;

  private EmailAttachment[] attachments;
}
