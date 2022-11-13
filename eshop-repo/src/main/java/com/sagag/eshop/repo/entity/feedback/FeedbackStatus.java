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
 * Entity mapping class to table FEEDBACK_STATUS.
 */
@Entity
@Table(name = "FEEDBACK_STATUS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackStatus implements Serializable {

  private static final long serialVersionUID = -4060415583542889475L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @NotNull
  private String statusCode;

  private String description;

}
