package com.sagag.eshop.repo.entity.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "VIEW_OFFER_PERSON")
public class ViewOfferPerson implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private Long id;

  private Integer organisationId;

  private String displayName;

  private String companyName;

  private String firstName;

  private String lastName;

  private String line1;

  private String zipcode;

  private String city;

  private String phone;

  private String fax;

  private String email;

  private String salutation;

}
