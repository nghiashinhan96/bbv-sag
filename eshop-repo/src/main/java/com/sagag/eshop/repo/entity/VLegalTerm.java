package com.sagag.eshop.repo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Legal term view class which maps to view V_LEGAL_TERM.
 */
@Data
@Entity
@Table(name = "V_LEGAL_TERM")
@NoArgsConstructor
public class VLegalTerm implements Serializable {

  private static final long serialVersionUID = 8005585562892695474L;

  @Id
  private Long termId;

  private String name;

  private String summary;

  private String content;

  private String pdfUrl;

  private Long userId;

  private String timeAccepted;

  private String dateValidFrom;

  private Integer acceptancePeriodDays;

  private Integer sort;

  private Long customerId;

  private String language;

  private Integer status;

}
