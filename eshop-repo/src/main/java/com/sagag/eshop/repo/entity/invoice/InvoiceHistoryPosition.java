package com.sagag.eshop.repo.entity.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity mapping to SAGSYS_INVOICE table.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "INVOICE_HISTORY_POSITION")
@Entity
public class InvoiceHistoryPosition implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "ID")
  private String id;

  private Long invoiceNr;

  private Integer packslipPosNr;

  @Column(name = "PACKSLIPS")
  private String packSlips;

  @Temporal(TemporalType.TIMESTAMP)
  private Date dateInvoiceCreated;

  private String currency;

  private Float price;

  private String articleErpNr;

  private Integer quantity;

  private String articleDescription;

}
