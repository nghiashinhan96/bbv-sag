package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the FEEDBACK_STATUS database table.
 * 
 */
@Entity
@Table(name = "FEEDBACK_STATUS")
@NamedQuery(name = "FeedbackStatus.findAll", query = "SELECT f FROM FeedbackStatus f")
@Data
public class FeedbackStatus implements Serializable {

  private static final long serialVersionUID = -1138343526487307518L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "STATUS_CODE")
  private String statusCode;

}
