package com.sagag.services.copydb.domain.src;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the LEGAL_DOCUMENT_MASTER database table.
 * 
 */
@Entity
@Table(name = "LEGAL_DOCUMENT_MASTER")
@NamedQuery(name = "LegalDocumentMaster.findAll", query = "SELECT l FROM LegalDocumentMaster l")
@Data
public class LegalDocumentMaster implements Serializable {

  private static final long serialVersionUID = -8086363946565191229L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "COUNTRY")
  private String country;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "CREATED_USER_ID")
  private Long createdUserId;

  @Column(name = "DOCUMENT")
  private String document;

  @Column(name = "DOCUMENT_NAME")
  private String documentName;

  @Column(name = "DOCUMENT_REF")
  private String documentRef;

  @Column(name = "[LANGUAGE]")
  private String language;

  @Column(name = "MODIFIED_DATE")
  private Date modifiedDate;

  @Column(name = "MODIFIED_USER_ID")
  private Long modifiedUserId;

  @Column(name = "PDF_URL")
  private String pdfUrl;

  @Column(name = "SUMMARY")
  private String summary;

}
