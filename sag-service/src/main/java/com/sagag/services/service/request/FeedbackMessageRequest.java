package com.sagag.services.service.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sagag.eshop.service.dto.EmailAttachment;
import com.sagag.services.service.dto.feedback.FeedbackMessageContentDto;
import com.sagag.services.service.dto.feedback.FeedbackParentDataItem;
import com.sagag.services.service.dto.feedback.FeedbackSourceDto;
import com.sagag.services.service.dto.feedback.FeedbackTopicDto;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(Include.NON_NULL)
public class FeedbackMessageRequest implements Serializable {

  private static final long serialVersionUID = 7367517961135622947L;

  private String lang;

  private FeedbackTopicDto topic;

  private String affiliateStore;

  private FeedbackSourceDto source;

  private FeedbackMessageContentDto message;

  private FeedbackParentDataItem salesInfo;

  private FeedbackParentDataItem userData;

  private FeedbackParentDataItem technicalData;

  private String affiliate;

  @JsonIgnore
  private EmailAttachment[] attachments;
}
