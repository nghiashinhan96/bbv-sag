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
@Table(name = "INVOICE_HISTORY")
@Entity
public class InvoiceHistory implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "ID")
  private String id;

  private Long invoiceNr; // FAKTURANR

  private Long orderNr; // AUFTRAGSNR

  private Long deliveryNr; // LIEFERSCHEINNR

  private Integer customerNr; // KUNDENNR

  private String companyName; // FIRMENNAME

  private String lastName; // NACHNAME

  private String firstName; // VORNAME

  private String searchTerm; // SUCHBEGR

  private String street; // STRASSE

  private Integer postCode; // PLZ

  private String place; // ORT

  private String country; // LAND

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate; // FAKTDAT

  private String currency; // WAEHRUNG

  private Float price; // FWBETRINCL

  private String invoiceType; // FAKTURAART

  private String cashCredit; // BARKREDIT

  private String organisation; // ORGANISATION

  private String docId; // DOCID

  private String sourceFrom;
}
