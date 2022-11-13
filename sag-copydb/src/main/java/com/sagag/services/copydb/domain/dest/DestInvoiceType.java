package com.sagag.services.copydb.domain.dest;

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
@NamedQuery(name = "DestInvoiceType.findAll", query = "SELECT i FROM DestInvoiceType i")
@Data
public class DestInvoiceType implements Serializable {

  private static final long serialVersionUID = 4535080509793381258L;

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
