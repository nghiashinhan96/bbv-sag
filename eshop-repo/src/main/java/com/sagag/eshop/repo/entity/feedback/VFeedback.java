package com.sagag.eshop.repo.entity.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity mapping class to table FEEDBACK.
 */
@Entity
@Table(name = "V_FEEDBACK")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VFeedback implements Serializable {

  private static final long serialVersionUID = 5954542586105492531L;

  @Id
  private long id;

  private Long userId;

  private String userName;

  private String email;

  private Long salesId;

  private String salesUserName;

  private String salesEmail;

  private Integer statusId;

  private String statusCode;

  private Integer topicId;

  private String topicCode;

  private Integer orgId;

  private String orgCode;

  private String orgName;

}
