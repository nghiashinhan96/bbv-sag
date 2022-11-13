package com.sagag.services.tools.domain.customer;

import com.opencsv.bean.CsvBindByName;
import com.sagag.services.tools.support.SupportedAffiliate;

import lombok.Data;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@Data
public class CsvCustomerInfoData implements Serializable {

  private static final long serialVersionUID = -2523226050211511461L;

  @CsvBindByName(column = "Telefon", locale = "US")
  private String phoneNumber;

  @CsvBindByName(column = "Konto", locale = "US")
  private String customerNr;

  @CsvBindByName(column = "AP", locale = "US")
  private String ap;

  @CsvBindByName(column = "Name", locale = "US")
  private String name;

  @CsvBindByName(column = "Adresse", locale = "US")
  private String address;

  @CsvBindByName(column = "Debitorengruppe", locale = "US")
  private String customerGroup;

  @CsvBindByName(column = "Lieferart", locale = "US")
  private String deliveryMethod;

  @CsvBindByName(column = "Zahlungsmethode", locale = "US")
  private String paymentMethod;

  @CsvBindByName(column = "Lagerort", locale = "US")
  private String storageLocation;

  @CsvBindByName(column = "Währung", locale = "US")
  private String currency;

  @CsvBindByName(column = "Adresse2", locale = "US")
  private String address2;

  @CsvBindByName(column = "Verkaufsgruppe", locale = "US")
  private String salesGroup;

  @CsvBindByName(column = "Spalte1", locale = "US")
  private String col1;

  @CsvBindByName(column = "Spalte2", locale = "US")
  private String col2;

  private SupportedAffiliate affiliate;

  @Override
  public String toString() {
    return new ToStringBuilder(getCustomerNr())
        .append(getAddress())
        .append(getDeliveryMethod())
        .append(affiliate.getCompanyName())
        .toString();
  }
}
