package com.sagag.services.copydb.domain.dest;

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
@NamedQuery(name = "DestFeedbackStatus.findAll", query = "SELECT f FROM DestFeedbackStatus f")
@Data
public class DestFeedbackStatus implements Serializable {

  private static final long serialVersionUID = -331255784735639178L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "STATUS_CODE")
  private String statusCode;

}
