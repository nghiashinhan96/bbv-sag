package com.sagag.services.copydb.domain.src;

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
@NamedQuery(name = "FeedbackTopic.findAll", query = "SELECT f FROM FeedbackTopic f")
@Data
public class FeedbackTopic implements Serializable {

  private static final long serialVersionUID = 8098526873096596863L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "TOPIC_CODE")
  private String topicCode;

}
