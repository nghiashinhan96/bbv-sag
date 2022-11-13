package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the INVOICE_TYPE database table.
 * 
 */
@Entity
@Table(name = "INVOICE_TYPE")
@NamedQuery(name = "InvoiceType.findAll", query = "SELECT i FROM InvoiceType i")
@Data
public class InvoiceType implements Serializable {

  private static final long serialVersionUID = 8889570964653426511L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "INVOICE_TYPE_CODE")
  private String invoiceTypeCode;

  @Column(name = "INVOICE_TYPE_DESC")
  private String invoiceTypeDesc;

  @Column(name = "INVOICE_TYPE_NAME")
  private String invoiceTypeName;

}
