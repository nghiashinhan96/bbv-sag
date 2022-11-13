package com.sagag.services.service.request.dms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DmsExportRequest implements Serializable {

  private static final long serialVersionUID = 1601016997023863650L;

  private List<DmsExportItem> orders;
  private String basePath;
  private String fileName;
  private Long customerNr;
  private String requestType;
  private String orderNumber;
  private String orderDate;
  private String totalPriceInclVat;
  private String totalPrice;
  private String deliveryType; // pickup or tour
  private String paymentMethod; // cash or credit
  private String companyName; // LAdresseFirma
  private String street; // LAdresseStrasse
  private String postCode; // LAdressePLZ
  private String city; // LAdresseOrt
  private String note; // Bemerkung
  private String version; // Version 3
  @NonNull
  private String dmsCommand;
}
