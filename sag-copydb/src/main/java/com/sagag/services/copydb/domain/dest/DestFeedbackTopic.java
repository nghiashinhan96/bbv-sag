package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the FEEDBACK_TOPIC database table.
 * 
 */
@Entity
@Table(name = "FEEDBACK_TOPIC")
@NamedQuery(name = "DestFeedbackTopic.findAll", query = "SELECT f FROM DestFeedbackTopic f")
@Data
public class DestFeedbackTopic implements Serializable {

  private static final long serialVersionUID = 7528642665008640939L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "TOPIC_CODE")
  private String topicCode;

}
