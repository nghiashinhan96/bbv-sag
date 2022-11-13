package com.sagag.eshop.repo.entity.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity mapping class to table FEEDBACK.
 */
@Entity
@Table(name = "FEEDBACK")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Feedback implements Serializable {

  private static final long serialVersionUID = 6594796802554361346L;

  private static final int MAX_FEEDBACK_MSG_LENGTH = 3000;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private Long userId;

  private Long salesId;

  private Integer statusId;

  private Integer topicId;

  private Integer orgId;

  private String source;
  
  private String salesInformation;

  private String userInformation;

  private String technicalInformation;

  @NotNull
  @Size(max = MAX_FEEDBACK_MSG_LENGTH)
  private String feedbackMessage;

  @NotNull
  private Date createdDate;

  @NotNull
  private Long createdUserId;

  private Date modifiedDate;

  private Long modifiedUserId;
}
