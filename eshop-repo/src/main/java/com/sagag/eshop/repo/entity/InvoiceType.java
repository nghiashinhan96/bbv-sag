package com.sagag.eshop.repo.entity;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "INVOICE_TYPE")
@Entity
@NamedQueries(
    value = {
        @NamedQuery(name = "InvoiceType.findAll", query = "SELECT i FROM InvoiceType i"),
        @NamedQuery(name = "InvoiceType.findById",
            query = "select i from InvoiceType i where i.id= :id"),
        @NamedQuery(
            name = "InvoiceType.findByInvoiceTypeCode",
            query = "select i from InvoiceType i where i.invoiceTypeCode= :invoiceTypeCode") })
@Data
public class InvoiceType implements Serializable {

  private static final long serialVersionUID = 4881089878120476452L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String invoiceTypeCode;

  private String invoiceTypeName;

  private String invoiceTypeDesc;

}
