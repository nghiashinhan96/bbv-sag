package com.sagag.services.tools.domain.target;

import com.sagag.services.tools.domain.TargetBaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "OFFER_ADDRESS")
public class OfferAddress extends TargetBaseObject {

  private static final long serialVersionUID = 9017855967057634733L;

  @Id
  @GeneratedValue(generator = "specificIdGenerator")
  @GenericGenerator(name = "specificIdGenerator", strategy = "com.sagag.services.tools.support.SpecificIdentityGenerator")
  private Long id;

  @Column(name = "PERSON_ID")
  private Long personId;

  @Column(name = "COUNTRYISO")
  private String countryIso;

  @Column(name = "CITY")
  private String city;

  @Column(name = "LINE1")
  private String line1;

  @Column(name = "LINE2")
  private String line2;

  @Column(name = "LINE3")
  private String line3;

  @Column(name = "STATE")
  private String state;

  @Column(name = "ZIPCODE")
  private String zipCode;

  @Column(name = "ERP_ID")
  private String erpId;

  @Column(name = "PO_BOX")
  private String poBox;

  @Column(name = "TYPE")
  private String type;
}
