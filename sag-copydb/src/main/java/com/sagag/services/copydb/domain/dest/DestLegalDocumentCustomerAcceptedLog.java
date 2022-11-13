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
 * The persistent class for the LEGAL_DOCUMENT_CUSTOMER_ACCEPTED_LOG database
 * table.
 * 
 */
@Entity
@Table(name = "LEGAL_DOCUMENT_CUSTOMER_ACCEPTED_LOG")
@NamedQuery(name = "DestLegalDocumentCustomerAcceptedLog.findAll", query = "SELECT l FROM DestLegalDocumentCustomerAcceptedLog l")
@Data
public class DestLegalDocumentCustomerAcceptedLog implements Serializable {

  private static final long serialVersionUID = 3622627812909768147L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "AFFILIATE_ID")
  private Integer affiliateId;

  @Column(name = "CUSTOMER_ID")
  private Integer customerId;

  @Column(name = "LEGAL_DOCUMENT_ID")
  private Integer legalDocumentId;

  @Column(name = "TIME_ACCEPTED")
  private Date timeAccepted;

  @Column(name = "USER_ID")
  private Long userId;

}
