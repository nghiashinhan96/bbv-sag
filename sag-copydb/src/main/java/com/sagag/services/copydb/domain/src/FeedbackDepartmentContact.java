package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * Entity mapping class to table FEEDBACK_DEPARTMENT_CONTACT.
 */
@Entity
@Table(name = "FEEDBACK_DEPARTMENT_CONTACT")
@NamedQuery(name = "FeedbackDepartmentContact.findAll", query = "SELECT f FROM FeedbackDepartmentContact f")
@Data
public class FeedbackDepartmentContact implements Serializable {

  private static final long serialVersionUID = -3842490982626845386L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "DEPARTMENT_ID")
  private Integer departmentId;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "VALUE")
  private String value;

}
