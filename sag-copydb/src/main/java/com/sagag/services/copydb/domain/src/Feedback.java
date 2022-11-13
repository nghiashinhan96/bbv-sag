package com.sagag.services.copydb.domain.src;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * Entity mapping class to table FEEDBACK.
 */
@Entity
@Table(name = "FEEDBACK")
@NamedQuery(name = "Feedback.findAll", query = "SELECT f FROM Feedback f")
@Data
public class Feedback implements Serializable {

  private static final long serialVersionUID = 2062623739963950600L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "CREATED_USER_ID")
  private Long createdUserId;

  @Column(name = "FEEDBACK_MESSAGE")
  private String feedbackMessage;

  @Column(name = "MODIFIED_DATE")
  private Date modifiedDate;

  @Column(name = "MODIFIED_USER_ID")
  private Long modifiedUserId;

  @Column(name = "ORG_ID")
  private Integer orgId;

  @Column(name = "SALES_ID")
  private Long salesId;

  @Column(name = "SALES_INFORMATION")
  private String salesInformation;

  @Column(name = "SOURCE")
  private String source;

  @Column(name = "STATUS_ID")
  private Integer statusId;

  @Column(name = "TECHNICAL_INFORMATION")
  private String technicalInformation;

  @Column(name = "TOPIC_ID")
  private Integer topicId;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "USER_INFORMATION")
  private String userInformation;
}
