package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the FEEDBACK_DEPARTMENT database table.
 * 
 */
@Entity
@Table(name = "FEEDBACK_DEPARTMENT")
@NamedQuery(name = "FeedbackDepartment.findAll", query = "SELECT f FROM FeedbackDepartment f")
@Data
public class FeedbackDepartment implements Serializable {

  private static final long serialVersionUID = -3134647083941576418L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DEPARTMENT_CODE")
  private String departmentCode;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "SUPPORTED_AFFILIATE_ID")
  private Integer supportedAffiliateId;

}
