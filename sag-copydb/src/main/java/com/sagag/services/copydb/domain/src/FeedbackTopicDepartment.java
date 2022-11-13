package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the FEEDBACK_TOPIC_DEPARTMENT database table.
 * 
 */
@Entity
@Table(name = "FEEDBACK_TOPIC_DEPARTMENT")
@NamedQuery(name = "FeedbackTopicDepartment.findAll", query = "SELECT f FROM FeedbackTopicDepartment f")
@Data
public class FeedbackTopicDepartment implements Serializable {

  private static final long serialVersionUID = 7413037776044161828L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DEPARTMENT_ID")
  private Integer departmentId;

  @Column(name = "TOPIC_ID")
  private Integer topicId;

}
