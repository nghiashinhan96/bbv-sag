package com.sagag.eshop.repo.entity.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Entity mapping class to table FEEDBACK_DEPARTMENT.
 */
@Entity
@Table(name = "FEEDBACK_DEPARTMENT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDepartment implements Serializable {

  private static final long serialVersionUID = 3067202993565274129L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @NotNull
  private String departmentCode;

  private String description;

  private Integer supportedAffiliateId;
}
