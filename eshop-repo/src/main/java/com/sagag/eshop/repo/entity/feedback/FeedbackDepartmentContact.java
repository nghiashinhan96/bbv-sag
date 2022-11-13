package com.sagag.eshop.repo.entity.feedback;

import com.sagag.eshop.repo.enums.FeedbackDepartmentContactType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Entity mapping class to table FEEDBACK_DEPARTMENT_CONTACT.
 */
@Entity
@Table(name = "FEEDBACK_DEPARTMENT_CONTACT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDepartmentContact implements Serializable {

  private static final long serialVersionUID = -3842490982626845386L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotNull
  private Integer departmentId;

  @NotNull
  @Enumerated(EnumType.STRING)
  private FeedbackDepartmentContactType type;

  @NotNull
  private String value;

}
