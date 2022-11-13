package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the LEGAL_DOCUMENT_AFFILIATE_ASSIGNED_LOG database
 * table.
 * 
 */
@Entity
@Table(name = "LEGAL_DOCUMENT_AFFILIATE_ASSIGNED_LOG")
@NamedQuery(name = "DestLegalDocumentAffiliateAssignedLog.findAll", query = "SELECT l FROM DestLegalDocumentAffiliateAssignedLog l")
@Data
public class DestLegalDocumentAffiliateAssignedLog implements Serializable {

  private static final long serialVersionUID = 3557075964594904354L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "ACCEPTANCE_PERIOD_DAYS")
  private Integer acceptancePeriodDays;

  @Column(name = "AFFILIATE_ID")
  private Integer affiliateId;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "CREATED_USER_ID")
  private long createdUserId;

  @Column(name = "DATE_VALID_FROM")
  private Date dateValidFrom;

  @Column(name = "LEGAL_DOCUMENT_ID")
  private Integer legalDocumentId;

  @Column(name = "MODIFIED_DATE")
  private Date modifiedDate;

  @Column(name = "MODIFIED_USER_ID")
  private Long modifiedUserId;

  @Column(name = "[SORT]")
  private Integer sort;

  @Column(name = "STATUS")
  private String status;

}
