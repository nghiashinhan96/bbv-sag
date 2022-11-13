package com.sagag.services.tools.domain.source;

import com.sagag.services.tools.domain.SourceBaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "SHOP.ADDRESS")
public class SourceAddress extends SourceBaseObject {

  private static final long serialVersionUID = -183402028666982759L;

  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "LINE1")
  private String line1;

  @Column(name = "LINE2")
  private String line2;

  @Column(name = "LINE3")
  private String line3;

  @Column(name = "COUNTRYISO")
  private String countryIso;

  @Column(name = "STATE")
  private String state;

  @Column(name = "CITY")
  private String city;

  @Column(name = "ZIPCODE")
  private String zipCode;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "ERPID")
  private Long erpId;

  @Column(name = "POBOX")
  private String poBox;
}
